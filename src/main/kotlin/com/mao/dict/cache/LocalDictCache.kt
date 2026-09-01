package com.mao.dict.cache

import com.mao.common.util.TreeUtils
import com.mao.dict.entity.*
import com.mao.dict.mapper.DictItemViewMapper
import com.mao.dict.repository.DictItemRepository
import com.mao.dict.repository.DictTypeRepository
import com.mao.dict.repository.IndustryRepository
import com.mao.dict.repository.ProvinceCityDistrictRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener

/**
 * ## 本地字典数据缓存
 * 适用于单实例系统
 *
 * 本实现采用“不可变快照 + 原子替换”的方式维护缓存：
 * - 查询直接读取已经构建完成的快照，不需要加锁；
 * - 普通字典发生更新时，在互斥区内根据旧快照创建新快照，完成后一次性替换；
 * - 省市区和行业是固定数据，只在应用启动或首次访问时加载一次。
 *
 * 快照中的集合在发布后不会再被本类修改，因此调用方不会读取到只更新了一部分的缓存数据。
 */
class LocalDictCache(
    private val dictTypeRepository: DictTypeRepository,
    private val dictItemRepository: DictItemRepository,
    private val regionRepository: ProvinceCityDistrictRepository,
    private val industryRepository: IndustryRepository
) : DictCache {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * 预加载使用独立的 IO 协程作用域，避免数据库读取阻塞 Spring 事件发布线程。
     * SupervisorJob 保证某一类缓存预加载失败时，不会取消另外两类缓存的加载任务。
     */
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * 三类缓存使用相互独立的锁，防止加载省市区数据时阻塞行业或普通字典缓存。
     *
     * dictMutex 还负责串行化普通字典的增量更新，避免并发更新基于同一个旧快照计算，
     * 从而发生“后写入的快照覆盖先写入更新”的问题。
     */
    private val dictMutex = Mutex()
    private val regionMutex = Mutex()
    private val industryMutex = Mutex()

    /**
     * @Volatile 保证快照引用在线程之间的可见性。
     * 它只负责安全发布已经构建完成的快照；初始化和更新时的复合操作由对应 Mutex 保护。
     */
    @Volatile
    private var dictSnapshot: DictSnapshot? = null

    @Volatile
    private var regionSnapshot: RegionSnapshot? = null

    @Volatile
    private var industrySnapshot: IndustrySnapshot? = null

    /**
     * 程序启动成功后立即异步加载三类缓存。
     *
     * 三个加载任务彼此独立；其中一个失败不会影响其他任务。加载失败也不会永久破坏缓存，
     * 后续业务首次访问对应缓存时，懒加载逻辑会再次尝试从数据库读取。
     */
    @EventListener(ApplicationReadyEvent::class)
    fun preload() {
        preload("字典") { getDictSnapshot() }
        preload("省市区") { getRegionSnapshot() }
        preload("行业") { getIndustrySnapshot() }
    }

    /**
     * 返回“大项名称 -> 全部有效细项列表”的普通字典快照。
     *
     * 这里的“全部”包含启用和禁用两种状态；是否启用只在 isActiveDictItem 中判断。
     */
    override suspend fun getDictMap(): Map<String, List<DictItemVo>> {
        return getDictSnapshot().dictMap
    }

    /**
     * 在指定字典大项的范围内，使用预先生成的启用细项 ID 集合进行 O(1) 校验。
     *
     * 本方法只读取内存快照，不执行数据库访问，因此可以供同步的 Bean Validation 调用。
     * 当缓存尚未完成预加载、itemId 为 null、字典大项名称不存在、细项不属于该大项
     * 或细项未启用时均返回 false。
     * 增加大项范围检查后，即使不同大项之间错误地传递了细项 ID，也不会通过校验。
     */
    override fun isActiveDictItem(itemId: Int?, dictType: String): Boolean {
        return itemId != null && itemId in dictSnapshot?.activeItemIdsByType?.get(dictType).orEmpty()
    }

    /**
     * 增量新增或更新字典大项。
     *
     * 更新期间保留所有细项原始数据，再统一重建派生的字典映射和启用 ID 索引。
     * 因此字典大项改名后，旧名称会立即消失，新名称会自动关联原有细项。
     */
    override suspend fun addOrUpdateDict(dictType: DictTypeDo) {
        val type = CachedDictType(
            id = requireNotNull(dictType.id) { "字典大项 ID 不能为空" },
            name = requireNotNull(dictType.name) { "字典大项名称不能为空" }
        )
        dictMutex.withLock {
            // 若启动预加载尚未完成，则先在同一把锁内完成初始化，再执行本次更新。
            val current = dictSnapshot ?: loadDictSnapshot()
            // Map 的 + 操作创建新 Map，不直接修改正在被查询线程使用的旧快照。
            dictSnapshot = buildDictSnapshot(current.types + (type.id to type), current.items)
        }
    }

    /**
     * 增量新增或更新字典细项。
     *
     * 快照保留启用和禁用的全部有效细项；对外字典映射同样返回两种状态的数据，
     * 但有效 ID 集合仅包含 status == 1 的细项。
     * 这样同一个方法即可正确处理新增、改名、移动所属大项、禁用和重新启用。
     */
    override suspend fun addOrUpdateDictItem(dictItem: DictItemDo) {
        val item = DictItemViewMapper.map(dictItem).also {
            requireNotNull(it.id) { "字典细项 ID 不能为空" }
            requireNotNull(it.pid) { "字典细项所属大项 ID 不能为空" }
            requireNotNull(it.name) { "字典细项名称不能为空" }
            requireNotNull(it.status) { "字典细项状态不能为空" }
        }
        dictMutex.withLock {
            val current = dictSnapshot ?: loadDictSnapshot()
            // 使用 ID 覆盖旧值，然后从完整数据重建所有派生索引，保证它们始终相互一致。
            dictSnapshot = buildDictSnapshot(current.types, current.items + (item.id!! to item))
        }
    }

    /**
     * 省市区为固定数据，直接返回初始化完成的树快照
     */
    override suspend fun getProvinceCityDistrictTree(): List<ProvinceCityDistrictVo> {
        return getRegionSnapshot().tree
    }

    /**
     * 校验 ID 是否存在，并且实际树层级与调用方要求的行政区域类型一致。
     * 本方法只读取内存快照；缓存尚未完成预加载时返回 false。
     */
    override fun isProvinceCityDistrict(id: Int?, regionType: RegionType): Boolean {
        return id != null && regionSnapshot?.types?.get(id) == regionType
    }

    /**
     * 行业为固定数据，直接返回初始化完成的四级分类树快照。
     */
    override suspend fun getIndustryTree(): List<IndustryVo> = getIndustrySnapshot().tree

    /**
     * 只有位于行业树第四层的“小类”ID才允许被业务数据引用。
     * 本方法只读取内存快照；缓存尚未完成预加载时返回 false。
     */
    override fun isIndustry(id: Int?): Boolean {
        return id != null && id in industrySnapshot?.smallCategoryIds.orEmpty()
    }

    /**
     * 启动一个独立预加载任务，并统一记录成功或失败日志。
     */
    private fun preload(name: String, loader: suspend () -> Unit) {
        scope.launch {
            runCatching { loader() }
                .onSuccess { log.info("{}缓存预加载完成", name) }
                .onFailure { log.error("{}缓存预加载失败，首次访问时将重试", name, it) }
        }
    }

    /**
     * 获取普通字典快照，使用双重检查避免每次读取都进入互斥区：
     * 快照已存在时直接返回；只有尚未初始化时才竞争锁并访问数据库。
     */
    private suspend fun getDictSnapshot(): DictSnapshot {
        dictSnapshot?.let { return it }
        return dictMutex.withLock {
            // 等锁期间其他协程可能已经完成初始化，因此进入锁后必须再次检查。
            dictSnapshot ?: loadDictSnapshot().also { dictSnapshot = it }
        }
    }

    /**
     * 从数据库加载字典大项和全部细项，并构建第一份完整快照。
     */
    private suspend fun loadDictSnapshot(): DictSnapshot {
        // 字典大项名称会作为 Map 的 key，因此只缓存 ID 和名称都不为 null 的有效大项。
        val types = dictTypeRepository.findAll().toList().mapNotNull { dictType ->
            val id = dictType.id
            val name = dictType.name
            if (id == null || name == null) {
                null
            } else {
                id to CachedDictType(id, name)
            }
        }.toMap()

        // 字典细项的 ID、父级 ID、名称和状态都是构成有效缓存数据的必要字段。
        // 任一字段为 null 时跳过该条记录，而不是将 null 数据放入对外缓存。
        val items = dictItemRepository.findAll().toList().mapNotNull { dictItem ->
            val item = DictItemViewMapper.map(dictItem)
            if (item.id == null || item.pid == null || item.name == null || item.status == null) {
                null
            } else {
                item.id to item
            }
        }.toMap()
        return buildDictSnapshot(types, items)
    }

    private fun buildDictSnapshot(
        types: Map<Int, CachedDictType>,
        items: Map<Int, DictItemVo>
    ): DictSnapshot {
        // 大项名称是 getDictMap 的 key，重复名称会导致数据被静默覆盖，因此主动拒绝脏数据。
        require(types.values.map(CachedDictType::name).distinct().size == types.size) {
            "字典大项名称不能重复"
        }
        // 先按所属大项筛选全部有效细项，包含 status == 0 的禁用数据。
        // 父大项不存在的孤立细项不放入 getDictMap，也不可能通过启用状态校验。
        val itemsByType = items.values
            .filter { it.pid in types }
            .groupBy(DictItemVo::pid)

        // getDictMap 返回大项下的全部有效细项，启用状态不在这里过滤。
        val dictMap = types.values.associate { type ->
            type.name to itemsByType[type.id].orEmpty()
        }

        // 单独构建启用 ID 索引，仅供 isActiveDictItem 使用。
        val activeItemsByType = itemsByType.mapValues { (_, itemsOfType) ->
            itemsOfType.filter { it.status == 1 }
        }
        return DictSnapshot(
            types = types,
            items = items,
            dictMap = dictMap,
            activeItemIdsByType = types.values.associate { type ->
                type.name to activeItemsByType[type.id].orEmpty().mapNotNull(DictItemVo::id).toSet()
            }
        )
    }

    /**
     * 获取省市区固定快照；锁保证并发首次访问最多执行一次数据库加载。
     */
    private suspend fun getRegionSnapshot(): RegionSnapshot {
        regionSnapshot?.let { return it }
        return regionMutex.withLock {
            regionSnapshot ?: loadRegionSnapshot().also { regionSnapshot = it }
        }
    }

    /**
     * 加载省市区数据，构建树结构，同时生成用于类型校验的 ID 层级索引。
     */
    private suspend fun loadRegionSnapshot(): RegionSnapshot {
        val nodes = regionRepository.findAll().toList()
            .sortedBy(ProvinceCityDistrictDo::id)
            .map { ProvinceCityDistrictVo(it.id, it.pid, it.code, it.name) }
        val tree = TreeUtils.buildTree(nodes)
        val types = mutableMapOf<Int, RegionType>()

        // 根节点为省，第二层为市，第三层为区县；递归遍历时同步记录每个 ID 的类型。
        fun index(nodesAtLevel: List<ProvinceCityDistrictVo>, level: Int) {
            val type = RegionType.entries.getOrNull(level)
                ?: error("省市区数据层级不能超过三级")
            nodesAtLevel.forEach { node ->
                val id = requireNotNull(node.id) { "省市区 ID 不能为空" }
                types[id] = type
                if (node.children.isNotEmpty()) {
                    index(node.children, level + 1)
                }
            }
        }
        index(tree, 0)
        return RegionSnapshot(tree, types)
    }

    /**
     * 获取行业固定快照；锁保证并发首次访问最多执行一次数据库加载。
     */
    private suspend fun getIndustrySnapshot(): IndustrySnapshot {
        industrySnapshot?.let { return it }
        return industryMutex.withLock {
            industrySnapshot ?: loadIndustrySnapshot().also { industrySnapshot = it }
        }
    }

    /**
     * 加载行业数据，构建四级行业树，并单独索引允许业务引用的第四层“小类”。
     */
    private suspend fun loadIndustrySnapshot(): IndustrySnapshot {
        val nodes = industryRepository.findAllByOrderByPidAscIdAsc().toList().map {
            IndustryVo(it.id, it.pid, it.code, it.name, it.description)
        }
        val tree = TreeUtils.buildTree(nodes)
        val smallCategoryIds = mutableSetOf<Int>()

        // 层级从 0 开始：门类=0、大类=1、中类=2、小类=3。
        fun index(nodesAtLevel: List<IndustryVo>, level: Int) {
            require(level <= INDUSTRY_SMALL_CATEGORY_LEVEL) { "行业数据层级不能超过四级" }
            nodesAtLevel.forEach { node ->
                val id = requireNotNull(node.id) { "行业 ID 不能为空" }
                if (level == INDUSTRY_SMALL_CATEGORY_LEVEL) {
                    smallCategoryIds += id
                }
                if (node.children.isNotEmpty()) {
                    index(node.children, level + 1)
                }
            }
        }
        index(tree, 0)
        return IndustrySnapshot(tree, smallCategoryIds)
    }

    /**
     * 普通字典大项在缓存计算中只需要 ID 和作为映射 key 的名称。
     */
    private data class CachedDictType(val id: Int, val name: String)

    /**
     * 普通字典完整快照：
     * - types/items 是增量更新下一份快照所需的源数据；
     * - dictMap 是按大项名称组织的细项列表；
     * - activeItemIdsByType 是按大项名称组织的启用细项 ID 集合，用于带范围的快速校验。
     */
    private data class DictSnapshot(
        val types: Map<Int, CachedDictType>,
        val items: Map<Int, DictItemVo>,
        val dictMap: Map<String, List<DictItemVo>>,
        val activeItemIdsByType: Map<String, Set<Int>>
    )

    /**
     * 省市区树及其“ID -> 行政区域层级”校验索引。
     */
    private data class RegionSnapshot(
        val tree: List<ProvinceCityDistrictVo>,
        val types: Map<Int, RegionType>
    )

    /**
     * 行业树及允许业务引用的小类 ID 索引。
     */
    private data class IndustrySnapshot(
        val tree: List<IndustryVo>,
        val smallCategoryIds: Set<Int>
    )

    private companion object {
        /** 门类为第 0 层，小类为第 3 层。 */
        const val INDUSTRY_SMALL_CATEGORY_LEVEL = 3
    }

}
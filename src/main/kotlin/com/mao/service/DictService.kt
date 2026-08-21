package com.mao.service

import com.mao.entity.*
import com.mao.ex.AppException
import com.mao.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 字典业务服务。
 *
 * 行政区和行业在应用启动时直接构建并缓存树结构，运行期间不再访问数据库。
 * 可维护字典缓存分组结果，字典写入成功后清空缓存，下一次查询时重新加载。
 */
@Service
class DictService(
    private val dictTypeRepository: DictTypeRepository,
    private val dictItemRepository: DictItemRepository,
    private val regionRepository: ProvinceCityDistrictRepository,
    private val industryRepository: IndustryRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    @Volatile
    private var dictCache: List<DictGroupVo>? = null
    private val regionTree = CompletableDeferred<List<ProvinceCityDistrictVo>>()
    private val industryTree = CompletableDeferred<List<IndustryVo>>()

    @EventListener(ApplicationReadyEvent::class)
    fun preload() {
        // 只加载一次并直接缓存最终树结构；接口调用只等待这两个永久快照。
        scope.launch {
            runCatching {
                buildTree(regionRepository.findAll().toList().map {
                    ProvinceCityDistrictVo(it.id, it.pid, it.code, it.name)
                })
            }.onSuccess(regionTree::complete).onFailure(regionTree::completeExceptionally)
            runCatching {
                buildTree(industryRepository.findAllByOrderByPidAscIdAsc().toList().map {
                    IndustryVo(it.id, it.pid, it.code, it.name, it.description)
                })
            }.onSuccess(industryTree::complete).onFailure(industryTree::completeExceptionally)
        }
    }

    private suspend fun getDictGroups(): List<DictGroupVo> {
        dictCache?.let { return it }
        val types = dictTypeRepository.findAll().toList()
        val items = dictItemRepository.findAll().toList()
        return types.map { type ->
            DictGroupVo(toTypeVo(type), items.filter { it.pid == type.id }.map(::toItemVo))
        }.also { dictCache = it }
    }

    suspend fun listDictGroups(): List<DictGroupVo> = getDictGroups()

    suspend fun listProvinceCityDistrict(): List<ProvinceCityDistrictVo> = regionTree.await()

    suspend fun listIndustry(): List<IndustryVo> = industryTree.await()

    @Transactional
    suspend fun createType(request: DictTypeAddQo): DictTypeVo {
        val name = request.name ?: throw AppException(ErrorCode.BAD_REQUEST)
        if (dictTypeRepository.findByName(name) != null) throw AppException(ErrorCode.BAD_REQUEST, "字典名称已存在")
        return toTypeVo(dictTypeRepository.save(DictTypeDo(name = name, description = request.description))).also { evictTypes() }
    }

    @Transactional
    suspend fun updateType(request: DictTypeUpdateQo): DictTypeVo {
        val old = dictTypeRepository.findById(request.id ?: throw AppException(ErrorCode.BAD_REQUEST))
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val name = request.name ?: throw AppException(ErrorCode.BAD_REQUEST)
        val duplicate = dictTypeRepository.findByName(name)
        if (duplicate?.id != old.id) throw AppException(ErrorCode.BAD_REQUEST, "字典名称已存在")
        old.name = name; old.description = request.description
        return toTypeVo(dictTypeRepository.save(old)).also { evictTypes() }
    }

    @Transactional
    suspend fun deleteType(id: Int?): Tips {
        val typeId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        dictTypeRepository.findById(typeId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        dictTypeRepository.deleteById(typeId)
        getDictGroups().firstOrNull { it.type.id == typeId }?.items
            ?.forEach { item -> item.id?.let { dictItemRepository.deleteById(it) } }
        evictAll(); return Tips("数据删除成功")
    }

    @Transactional
    suspend fun createItem(request: DictItemAddQo): DictItemVo {
        val pid = request.pid ?: throw AppException(ErrorCode.BAD_REQUEST)
        ensureType(pid)
        val name = request.name ?: throw AppException(ErrorCode.BAD_REQUEST)
        if (getDictGroups().any { it.type.id == pid && it.items.any { item -> item.name == name } })
            throw AppException(ErrorCode.BAD_REQUEST, "同一字典下名称已存在")
        return toItemVo(dictItemRepository.save(DictItemDo(pid = pid, name = name))).also { evictItems() }
    }

    @Transactional
    suspend fun updateItem(request: DictItemUpdateQo): DictItemVo {
        val item = dictItemRepository.findById(request.id ?: throw AppException(ErrorCode.BAD_REQUEST))
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val pid = request.pid ?: throw AppException(ErrorCode.BAD_REQUEST); ensureType(pid)
        val name = request.name ?: throw AppException(ErrorCode.BAD_REQUEST)
        if (getDictGroups().any { it.type.id == pid && it.items.any { candidate -> candidate.id != item.id && candidate.name == name } })
            throw AppException(ErrorCode.BAD_REQUEST, "同一字典下名称已存在")
        item.pid = pid; item.name = name
        return toItemVo(dictItemRepository.save(item)).also { evictItems() }
    }

    @Transactional
    suspend fun deleteItem(id: Int?): Tips {
        val itemId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        dictItemRepository.findById(itemId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        dictItemRepository.deleteById(itemId); evictItems(); return Tips("数据删除成功")
    }

    private suspend fun ensureType(id: Int) { if (dictTypeRepository.findById(id) == null) throw AppException(ErrorCode.BAD_REQUEST, "父字典不存在") }
    private fun toTypeVo(v: DictTypeDo) = DictTypeVo(v.id, v.name, v.description)
    private fun toItemVo(v: DictItemDo) = DictItemVo(v.id, v.pid, v.name)
    private fun evictTypes() { dictCache = null }
    private fun evictItems() { dictCache = null }
    private fun evictAll() { dictCache = null }

}
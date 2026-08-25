package com.mao.service

import com.mao.entity.*
import com.mao.ex.AppException
import com.mao.repository.DictItemRepository
import com.mao.repository.DictTypeRepository
import com.mao.repository.IndustryRepository
import com.mao.repository.ProvinceCityDistrictRepository
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
    suspend fun createItem(request: DictItemAddQo): DictItemVo {
        val pid = request.pid ?: throw AppException(ErrorCode.BAD_REQUEST)
        if (dictTypeRepository.findById(pid) == null) {
            throw AppException(ErrorCode.BAD_REQUEST, "父字典不存在")    
        }
        val name = request.name ?: throw AppException(ErrorCode.BAD_REQUEST, "请传递字典名称")
        // 重名检测
        if (dictItemRepository.findByPidAndName(pid, name) != null) {
            throw AppException(ErrorCode.BAD_REQUEST, "同一字典下名称已存在")
        }
        // 数据保存
        val dictItem = dictItemRepository.save(DictItemDo(pid = pid, name = name, status = 1))
        // 清空缓存
        evictDictCache()
        // 返回结果
        return toItemVo(dictItem)
    }

    @Transactional
    suspend fun updateItem(request: DictItemUpdateQo): DictItemVo {
        // 参数校验
        val id = request.id ?: throw AppException(ErrorCode.BAD_REQUEST)
        val item = dictItemRepository.findById(id) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val name = request.name ?: throw AppException(ErrorCode.BAD_REQUEST, "请传递字典名称")
        // 重名检测
        if (dictItemRepository.findByPidAndNameAndIdNot(item.pid!!, name, id) != null) {
            throw AppException(ErrorCode.BAD_REQUEST, "同一字典下名称已存在")
        }
        // 数据保存
        val dictItem = dictItemRepository.save(DictItemDo(id = id, pid = item.pid, name = name, status = item.status))
        // 清空缓存
        evictDictCache()
        // 返回结果
        return toItemVo(dictItem)
    }

    @Transactional
    suspend fun disableItem(id: Int?): Tips {
        val itemId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        val item = dictItemRepository.findById(itemId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        // 数据保存
        item.status = 0
        dictItemRepository.save(item)
        // 清空缓存
        evictDictCache()
        return Tips("数据禁用成功")
    }

    private fun toTypeVo(v: DictTypeDo) = DictTypeVo(v.id, v.name, v.description)

    private fun toItemVo(v: DictItemDo) = DictItemVo(v.id, v.pid, v.name, v.status)
    
    private fun evictDictCache() { dictCache = null }

}
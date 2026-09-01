package com.mao.dict.service

import com.mao.common.entity.ErrorCode
import com.mao.common.entity.Tips
import com.mao.common.ex.AppException
import com.mao.dict.cache.DictCache
import com.mao.dict.entity.*
import com.mao.dict.mapper.DictItemViewMapper
import com.mao.dict.repository.DictItemRepository
import com.mao.dict.repository.DictTypeRepository
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
    private val dictCache: DictCache
) {

    suspend fun searchAllDict(): Map<String, List<DictItemVo>> = dictCache.getDictMap()

    suspend fun searchRegionTree(): List<ProvinceCityDistrictVo> = dictCache.getProvinceCityDistrictTree()

    suspend fun searchIndustryTree(): List<IndustryVo> = dictCache.getIndustryTree()

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
        // 更新缓存
        dictCache.addOrUpdateDictItem(dictItem)
        // 返回结果
        return DictItemViewMapper.map(dictItem)
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
        // 更新缓存
        dictCache.addOrUpdateDictItem(dictItem)
        // 返回结果
        return DictItemViewMapper.map(dictItem)
    }

    @Transactional
    suspend fun disableItem(id: Int?): Tips {
        val itemId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        val item = dictItemRepository.findById(itemId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        // 数据保存
        item.status = 0
        dictItemRepository.save(item)
        // 更新缓存
        dictCache.addOrUpdateDictItem(item)
        return Tips("数据禁用成功")
    }

}
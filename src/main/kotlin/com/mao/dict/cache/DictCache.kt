package com.mao.dict.cache

import com.mao.dict.entity.*

/**
 * ## 字典数据的缓存
 * 字典数据包括三类：
 * - Dict + DictItem：基本字典数据，可配置，包含大项和细项
 * - ProvinceCityDistrict：省市区数据，树形结构，当前为固定数据，无法更改
 * - Industry：行业数据，树形结构，当前为固定数据，无法更改
 *
 * 字典数据频繁访问，该类作用是对字典数据进行缓存，缓存方式由实现类决定。
 * 同时该类提供了对字典数据的更新逻辑（采用增量更新）和字典数据正确性检测逻辑。
 */
interface DictCache {

    /**
     * ## 获取所有字典数据
     * 大项name为key，细项列表数据为value。
     * 返回启用和禁用的全部有效细项，但不返回包含必要字段 null 值的无效数据。
     */
    suspend fun getDictMap() : Map<String, List<DictItemVo>>

    /**
     * ## 判断指定大项下的细项ID是否正确且启用
     * 用于对引用字典ID的数据进行检测。
     * 只有字典大项名称存在、细项属于该大项并且细项处于启用状态时才返回 true。
     */
    fun isActiveDictItem(itemId: Int?, dictType: String) : Boolean

    /**
     * ## 新增或更新字典大项缓存
     * 当字典大项数据更新时，缓存同步更新。
     * **对于删除**：字典大项不允许删除
     */
    suspend fun addOrUpdateDict(dictType: DictTypeDo)

    /**
     * ## 新增或更新字典细项缓存
     * 当字典细项数据更新时，缓存同步更新。
     * **对于删除**：字典细项只能进行启用和禁用，无法删除
     */
    suspend fun addOrUpdateDictItem(dictItem: DictItemDo)

    /**
     * ## 获取所有省市区树结构数据
     */
    suspend fun getProvinceCityDistrictTree() : List<ProvinceCityDistrictVo>

    /**
     * ## 判断是否为正确的省市区数据ID
     * 用于对引用省市区ID的数据进行检测
     */
    fun isProvinceCityDistrict(id: Int?, regionType: RegionType) : Boolean

    /**
     * ## 获取所有行业树结构数据
     * 行业数据采用2017版《国民经济行业分类》（GB/T 4754-2017）标准数据源，
     * 其分类体系分为4个层次（门类、大类、中类、小类）
     */
    suspend fun getIndustryTree() : List<IndustryVo>

    /**
     * ## 判断是否为正确的行业数据ID
     * 用于对引用行业ID的数据进行检测。
     * 可引用的行业ID只能是**小类**。
     */
    fun isIndustry(id: Int?) : Boolean

}
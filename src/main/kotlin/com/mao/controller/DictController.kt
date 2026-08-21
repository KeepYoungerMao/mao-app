package com.mao.controller

import com.mao.entity.*
import com.mao.extension.OperationLog
import com.mao.service.DictService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/dict")
@OperationLog(module = OperationModule.DICT)
class DictController(private val dictService: DictService) {
    
    @PostMapping("all")
    @OperationLog(operation = Operation.ALL)
    suspend fun groups(): List<DictGroupVo> = dictService.listDictGroups()

    @PostMapping("region/tree")
    @OperationLog(operation = Operation.ALL)
    suspend fun regionTree(): List<DictTreeVo<ProvinceCityDistrictVo>> = dictService.listProvinceCityDistrict()

    @PostMapping("industry/tree")
    @OperationLog(operation = Operation.ALL)
    suspend fun industryTree(): List<DictTreeVo<IndustryVo>> = dictService.listIndustry()

    @PostMapping("type/create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createType(@Valid @RequestBody request: DictTypeAddQo): DictTypeVo = dictService.createType(request)
    
    @PostMapping("type/update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateType(@Valid @RequestBody request: DictTypeUpdateQo): DictTypeVo = dictService.updateType(request)
    
    @PostMapping("type/delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteType(@Valid @RequestBody request: IdQo<Int>): Tips = dictService.deleteType(request.id)
    
    @PostMapping("item/create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createItem(@Valid @RequestBody request: DictItemAddQo): DictItemVo = dictService.createItem(request)
    
    @PostMapping("item/update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateItem(@Valid @RequestBody request: DictItemUpdateQo): DictItemVo = dictService.updateItem(request)
    
    @PostMapping("item/delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteItem(@Valid @RequestBody request: IdQo<Int>): Tips = dictService.deleteItem(request.id)
}
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
    suspend fun regionTree(): List<ProvinceCityDistrictVo> = dictService.listProvinceCityDistrict()

    @PostMapping("industry/tree")
    @OperationLog(operation = Operation.ALL)
    suspend fun industryTree(): List<IndustryVo> = dictService.listIndustry()
    
    @PostMapping("item/create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createItem(@Valid @RequestBody request: DictItemAddQo): DictItemVo = dictService.createItem(request)
    
    @PostMapping("item/update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateItem(@Valid @RequestBody request: DictItemUpdateQo): DictItemVo = dictService.updateItem(request)
    
    @PostMapping("item/disable")
    @OperationLog(operation = Operation.DELETE)
    suspend fun disableItem(@Valid @RequestBody request: IdQo<Int>): Tips = dictService.disableItem(request.id)

}
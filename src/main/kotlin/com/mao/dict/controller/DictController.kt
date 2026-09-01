package com.mao.dict.controller

import com.mao.common.entity.IdQo
import com.mao.common.entity.Tips
import com.mao.common.handler.OperationLog
import com.mao.dict.entity.*
import com.mao.dict.service.DictService
import com.mao.log.entity.Operation
import com.mao.log.entity.OperationModule
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/dict")
@OperationLog(module = OperationModule.DICT)
class DictController(private val dictService: DictService) {

    @PostMapping("all")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchAllDict(): Map<String, List<DictItemVo>> = dictService.searchAllDict()

    @PostMapping("region/tree")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchRegionTree(): List<ProvinceCityDistrictVo> = dictService.searchRegionTree()

    @PostMapping("industry/tree")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchIndustryTree(): List<IndustryVo> = dictService.searchIndustryTree()

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
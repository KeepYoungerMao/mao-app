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

    @PostMapping("tree")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchDictTree(): Map<String, List<DictItemVo>> = dictService.searchDictTree()

    @PostMapping("type/all")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchAllDictType(): List<DictTypeVo> = dictService.searchAllDictType()

    @PostMapping("type/create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createDictType(@Valid @RequestBody request: DictTypeAddQo): DictTypeVo = dictService.createDictType(request)

    @PostMapping("type/update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateDictType(@Valid @RequestBody request: DictTypeUpdateQo): DictTypeVo = dictService.updateDictType(request)

    @PostMapping("item/all")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchAllDictItem(@Valid @RequestBody request: IdQo<Int>): List<DictItemVo> = dictService.searchAllDictItem(request.id)

    @PostMapping("item/create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createDictItem(@Valid @RequestBody request: DictItemAddQo): DictItemVo = dictService.createDictItem(request)

    @PostMapping("item/update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateDictItem(@Valid @RequestBody request: DictItemUpdateQo): DictItemVo = dictService.updateDictItem(request)

    @PostMapping("item/disable")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun disableDictItem(@Valid @RequestBody request: IdQo<Int>): Tips = dictService.disableDictItem(request.id)

}

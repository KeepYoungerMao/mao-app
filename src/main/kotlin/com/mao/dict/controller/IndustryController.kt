package com.mao.dict.controller

import com.mao.common.handler.OperationLog
import com.mao.dict.entity.IndustryVo
import com.mao.dict.service.IndustryService
import com.mao.log.entity.Operation
import com.mao.log.entity.OperationModule
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/industry")
@OperationLog(module = OperationModule.INDUSTRY)
class IndustryController(
    private val industryService: IndustryService
) {

    @PostMapping("tree")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchIndustryTree(): List<IndustryVo> = industryService.searchIndustryTree()

}
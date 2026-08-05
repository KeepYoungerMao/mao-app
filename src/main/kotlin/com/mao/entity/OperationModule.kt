package com.mao.entity

enum class OperationModule(val scope: OperationScope) {

    USER(OperationScope.SYSTEM),
    ROLE(OperationScope.SYSTEM),
    PERMISSION(OperationScope.SYSTEM),
    OPERATION_LOG(OperationScope.SYSTEM),
    DICT(OperationScope.SYSTEM),
    DEPARTMENT(OperationScope.SYSTEM),
    COMPANY(OperationScope.SYSTEM),

    ANCIENT_BOOK(OperationScope.DATA),
    CHINESE_SURNAME(OperationScope.DATA),
    CRUDE_DRUG(OperationScope.DATA),
    LIVE(OperationScope.DATA),
    LOL(OperationScope.DATA),
    PICTURE(OperationScope.DATA),
    POEM(OperationScope.DATA),
    POET(OperationScope.DATA),

    ERROR(OperationScope.SYSTEM),

}
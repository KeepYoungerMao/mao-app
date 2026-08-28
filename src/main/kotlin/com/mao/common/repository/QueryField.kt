package com.mao.common.repository

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class QueryField(val name: String = "", val type: Type = Type.EQUAL) {

    enum class Type {
        EQUAL, LIKE, GREATER_THAN, LESS_THAN, IN
    }

}

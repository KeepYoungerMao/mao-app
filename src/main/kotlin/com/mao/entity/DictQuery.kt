package com.mao.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class DictTypeAddQo(
    @field:NotBlank
    @field:Length(max = 50)
    val name: String? = null,
    @field:Length(max = 256)
    val description: String? = null,
)

data class DictTypeUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:NotBlank
    @field:Length(max = 50)
    val name: String? = null,
    @field:Length(max = 256)
    val description: String? = null,
)

data class DictItemAddQo(
    @field:NotNull
    val pid: Int? = null,
    @field:NotBlank
    @field:Length(max = 100)
    val name: String? = null,
)

data class DictItemUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:NotNull
    val pid: Int? = null,
    @field:NotBlank
    @field:Length(max = 100)
    val name: String? = null,
)
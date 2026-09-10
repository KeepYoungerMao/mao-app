package com.mao.dict.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class DictTypeUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:NotBlank
    @field:Length(max = 50)
    val name: String? = null,
    @field:Length(max = 256)
    val description: String? = null
)

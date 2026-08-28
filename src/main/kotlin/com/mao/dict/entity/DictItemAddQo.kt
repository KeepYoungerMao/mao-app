package com.mao.dict.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class DictItemAddQo(
    @field:NotNull
    val pid: Int? = null,
    @field:NotBlank
    @field:Length(max = 100)
    val name: String? = null
)

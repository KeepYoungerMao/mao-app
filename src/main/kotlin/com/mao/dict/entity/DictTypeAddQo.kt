package com.mao.dict.entity

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class DictTypeAddQo(
    @field:NotBlank
    @field:Length(max = 50)
    val name: String? = null,
    @field:Length(max = 256)
    val description: String? = null
)

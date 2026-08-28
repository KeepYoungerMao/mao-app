package com.mao.common.util

object DesensitizeUtils {

    @JvmStatic
    fun desensitizePhone(phone: String?): String? {
        if (phone.isNullOrEmpty() || phone.length != 11) return null
        return "${phone.substring(0,3)}****${phone.substring(7)}"
    }

    fun desensitizeIdCardNumber(idCardNumber: String?): String? {
        if (idCardNumber.isNullOrEmpty()) return null
        return when (idCardNumber.length) {
            18 -> "${idCardNumber.substring(0, 3)}************${idCardNumber.substring(15)}"
            15 -> "${idCardNumber.substring(0, 3)}*********${idCardNumber.substring(12)}"
            else -> idCardNumber
        }
    }

}
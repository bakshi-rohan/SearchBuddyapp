package com.searchbuddy.searchbuddy.model

data class RequestCompanyResponse(
    val companyName: String,
    val designation: String,
    val endDate: Any,
    val isPresent: String,
    val location: String,
    val startDate: String,
    val function: Functio,
    val noticePeriod: noticePerio


    )
data class Functio(
    val codeValueType: CodeValueType,
    val id: Int,
    val value: String
)
data class noticePerio(
    val id: Int,
    val value: String
)
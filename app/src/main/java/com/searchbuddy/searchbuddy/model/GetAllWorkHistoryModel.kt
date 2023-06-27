package com.searchbuddy.searchbuddy.model

data class GetAllWorkHistoryModelItem(
    val companyName: String,
    val designation: String,
    val endDate: String,
    val expMonths: Int,
    val function: Functi,
    val isPresent: Boolean,
    val location: String,
    val noticePeriod: NoticePeriod,
    val primarySkills: List<String>,
    val startDate: String
)
data class Functi(
    val codeValueType: CodeValueT,
    val id: Int,
    val value: String
)
data class CodeValueT(
    val id: Int,
    val name: String
)
data class NoticePeriod(
    val id: Int,
    val value: String
)
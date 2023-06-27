package com.searchbuddy.searchbuddy.model

data class ProfessionalDetailModel(
    val experienceMonths: Int,
    val function: Function,
    val id: Int,

    val professionalDetails: ProfessionalDetailsx
)
data class Function(
    val codeValueType: CodeValueTypeX,
    val id: Int,
    val value: String
)
data class ProfessionalDetailsx(
    val industry: String,
    val level: Level,
    val annualSalary: Int,
    val expectedSalary: Int,
    val noticePeriod: NoticePeriodX,
    val primarySkills: List<String>,
    val summary: String,
    val workHistory: List<WorkHistory>

)
data class NoticePeriodX(
    val id: Int,
    val value: String
)

data class CodeValueTypeX(
    val id: Int,
    val name: String
)
data class Level(
    val id: Int,
    val value: String
)
data class WorkHistory(
    val companyName: String,
    val designation: String,
    val endDate: String,
    val isPresent: String,
    val location: String,
    val startDate: String
)
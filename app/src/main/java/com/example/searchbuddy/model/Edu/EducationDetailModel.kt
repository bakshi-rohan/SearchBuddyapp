package com.example.searchbuddy.model.Edu

data class EducationDetailModel(
    val id: Int,
    val professionalDetails: ProfessionalDetailsed
)
data class ProfessionalDetailsed(
    val educationDetails: EducationDetails
)
data class EducationDetails(
    val graduation: Graduation,
    val intermediate: Intermediate,
    val metric: Metric,
    val postGraduation: PostGraduation
)
data class PostGraduation(
    val degree: String,
    val passingYear: Any,
    val percentage: Any,
    val specialization: Any,
    val startYear: Any,
    val university: String
)
data class Graduation(
    val degree: String,
    val passingYear: Int,
    val percentage: Any,
    val specialization: Any,
    val startYear: Int,
    val university: String
)
data class Intermediate(
    val board: Boar,
    val school: Any,
    val percentage: Any,
    val startYear: Int,
    val passingYear: Int
)
data class Metric(
    val board: Boar,
    val school: Any,
    val percentage: Any,
    val startYear: Int,
    val passingYear: Int
)
data class Boar(
    val id: Int,
    val value: String
)
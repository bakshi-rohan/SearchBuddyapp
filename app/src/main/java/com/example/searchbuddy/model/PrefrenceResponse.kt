package com.example.searchbuddy.model

data class PrefrenceResponse(
    val id: Int,
    val professionalDetails: PrefProfessionalDetails
)
data class PrefProfessionalDetails(
    val preferences: Preference
)
data class Preference(
    val employementType: List<String>,
    val location: List<String>,
    val role: List<String>,
    val workType: List<String>
)
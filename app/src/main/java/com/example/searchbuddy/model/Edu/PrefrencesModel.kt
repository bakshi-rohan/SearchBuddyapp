package com.example.searchbuddy.model.Edu


data class PrefrencesModel(
    val id: Int,
    val professionalDetails: ProfessionalDetails
)
data class Preferences(
    val employementType: List<String>,
    val location: List<String>,
    val role: List<String>,
    val workType: List<String>
)
data class ProfessionalDetails(
    val preferences: Preferences
)
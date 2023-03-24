package com.example.searchbuddy.model

data class RequestCompanyResponse(
    val companyName: String,
    val designation: String,
    val endDate: Any,
    val isPresent: String,
    val location: String,
    val startDate: String
)

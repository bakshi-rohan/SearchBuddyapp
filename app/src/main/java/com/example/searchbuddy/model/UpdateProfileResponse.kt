package com.example.searchbuddy.model

data class UpdateProfileResponse(
    val candidateId: Int,
    val designation: String,
    val email: String,
    val id: Int,
    val mobileNo: String,
    val name: String,
    val profilePicName: String
)
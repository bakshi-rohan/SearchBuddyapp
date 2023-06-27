package com.searchbuddy.searchbuddy.model

data class RequestProfileResponse(
    val designation: String,
    val email: String,
    val id: Int,
    val isActive: Boolean,
    val mobileNo: String,
    val name: String,
    var profilePicName:String
)
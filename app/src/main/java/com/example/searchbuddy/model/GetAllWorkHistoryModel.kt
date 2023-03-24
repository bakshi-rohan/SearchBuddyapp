package com.example.searchbuddy.model

import com.google.gson.annotations.SerializedName
data class GetAllWorkHistoryModelItem(
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("designation")
    val designation: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("isPresent")
    val isPresent: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("startDate")
    val startDate: String
)
package com.searchbuddy.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class UploadResumeResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: Int
)
data class Data(
    @SerializedName("college_name")
    val college_name: Any,
    @SerializedName("company_names")
    val company_names: Any,
    @SerializedName("degree")
    val degree: List<String>,
    @SerializedName("designation")
    val designation: List<String>,
    @SerializedName("email")
    val email: String,
    @SerializedName("experience")
    val experience: List<String>,
    @SerializedName("mobile_number")
    val mobile_number: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("no_of_pages")
    val no_of_pages: Int,
    @SerializedName("skills")
    val skills: List<String>,
    @SerializedName("total_experience")
    val total_experience: Double
)
package com.example.searchbuddy.model

data class RecommendedjobData(
    val recommendedJobs: List<RecommendedJob>
)

    data class RecommendedJob(
    val companyName: String,
    val encryptedId: String,
    val expFrm: Int,
    val expTo: Int,
    val url:String,
    val location: List<String>,
    val positionId: Int,
    val positionName: String,
    val salary: List<Int>,
    val skills: List<String>

    )
package com.searchbuddy.searchbuddy.model

data class RecommendedjobData(
    var profilePercentage:Int,
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
    val logo: String,
    val positionName: String,
    val positionSaved: Boolean,
    val postedDay: Int,
    val salary: List<Int>,
    val skills: List<String>

    )
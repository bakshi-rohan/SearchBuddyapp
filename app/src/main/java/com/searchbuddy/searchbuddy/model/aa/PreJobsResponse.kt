package com.searchbuddy.searchbuddy.model.aa

data class PreJobsResponse(
    val length: Int,
    val positions: List<PrePosition>
)
data class PrePosition(
    val clientName: String,
    val expFrom: Int,
    val expTo: Int,
    val function: String,
    val jobtype: String,
    val level: String,
    val location: List<String>,
    val positionAge: Int,
    val positionName: String,
    val skills: List<String>,
    val totalApplied: Int,
    val url: String
)
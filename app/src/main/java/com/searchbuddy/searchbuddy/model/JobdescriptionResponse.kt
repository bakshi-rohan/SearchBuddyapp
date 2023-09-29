package com.searchbuddy.searchbuddy.model

data class JobdescriptionResponse(
    val aboutOrg: String,
    val client: String,
    val applied: Boolean,
    val expFrom: String,
    val expTo: String,
    val function: String,
    val industry: String,
    val kra: String,
    val level: String,
    val url: String,
    val logo: String,
    val positionSaved: Boolean,
    val location: List<String>,
    val name: String,
    val roleDesc: String,
    val id: String,
    val totalViewed: Int,
    val totalApplied: String,
    val skills: List<String>
)
package com.example.searchbuddy.model

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
    val location: List<String>,
    val name: String,
    val roleDesc: String,
    val skills: List<String>
)
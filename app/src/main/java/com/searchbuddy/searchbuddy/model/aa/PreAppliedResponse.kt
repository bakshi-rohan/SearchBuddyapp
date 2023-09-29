package com.searchbuddy.searchbuddy.model.aa

data class PreAppliedResponse(
    val length: Int,
    val payLoad: List<PayLoad>
)
data class PayLoad(
    val clientName: String,
    val date: String,
    val expFrom: Int,
    val expTo: Int,
    val location: List<String>,
    val positionName: String,
    val salary: List<Int>,
    val stage: String,
    val status: String,
    val level: String
)
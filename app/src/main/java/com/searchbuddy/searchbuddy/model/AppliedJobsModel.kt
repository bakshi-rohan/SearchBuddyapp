package com.searchbuddy.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class AppliedJobsModel(
    @SerializedName("length")
    val length: Int,
    @SerializedName("positions")
    val positions: List<Positio>
)
data class Positio(
    val appliedOn: String,
    val createdBy: String,
    val encryptedId: String,
    val location: List<String>,
    val positionId: Int,
    val logo: String,
    val stage: String,
    val positionName: String,
    val skills: List<String>,
    val status: String
)
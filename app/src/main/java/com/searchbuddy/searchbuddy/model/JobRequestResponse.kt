package com.searchbuddy.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class JobRequestResponse(
    @SerializedName("length")
    var length: Int,
    @SerializedName("positions")
    var positions: List<Positions>
)

data class Positions(
    var JD: String,
    var description: String,
    var expFrm: Int,
    val expTo: Int,
    val positionSaved: Boolean,
    val function: String,
    val logo: String,
    val id: String,
    val industry: String,
    val location: List<String>,
    val organisationName: String,
    val positionName: String,
    val postedDay: Int,
    val salary: List<Int>,
    val shortName: String,
    val skills: List<String>,
    val url: String,
val applied:Boolean

)
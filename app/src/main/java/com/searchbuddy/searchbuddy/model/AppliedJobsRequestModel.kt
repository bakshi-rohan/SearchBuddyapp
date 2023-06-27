package com.searchbuddy.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class AppliedJobsRequestModel(

val condition: Any


)

data class Conditions(
    @SerializedName("index")
    val index: Int,

    @SerializedName("pagesize")
    val pagesize: Int
)


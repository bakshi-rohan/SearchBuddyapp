package com.searchbuddy.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class JobRequestModel(
    val condition: Any,
    var exp_from:Int,
    var exp_to:Int,
    var salary_from:Int,
    var salary_to:Int,
    var location :Any,
    var postedIn :String,
    var functions:Any,
    var keyword:Array<String>,
//    var jobType: Any


)

data class Condition(
    @SerializedName("index")
    val index: Int,

    @SerializedName("pagesize")
    val pagesize: Int
)
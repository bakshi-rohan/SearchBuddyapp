package com.searchbuddy.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class LogOutRequest(
    @SerializedName("token")
    var token: String
)


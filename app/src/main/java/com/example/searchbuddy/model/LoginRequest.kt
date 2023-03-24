package com.example.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("password")
    var password: String,
    @SerializedName("username")
    var username: String,
)

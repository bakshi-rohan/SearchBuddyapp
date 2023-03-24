package com.example.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("username")
    var username: String
)

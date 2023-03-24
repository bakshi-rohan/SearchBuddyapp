package com.example.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)
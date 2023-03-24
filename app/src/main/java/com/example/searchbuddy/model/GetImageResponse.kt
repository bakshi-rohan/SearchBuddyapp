package com.example.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class GetImageResponse(
    @SerializedName("image")
    var image:ByteArray
)

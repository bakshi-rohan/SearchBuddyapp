package com.searchbuddy.searchbuddy.model

import com.google.gson.annotations.SerializedName
import java.io.File

data class UploadResumeRequest(
    @SerializedName("resume")
    var resume : File
)

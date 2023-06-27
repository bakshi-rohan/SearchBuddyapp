package com.searchbuddy.searchbuddy.model.Edu

import com.searchbuddy.searchbuddy.model.CodeValueType

data class BasicDetailUpdateRequest(
    var differentlyAbled: Boolean,
    var dob: String,
    var gender: Any,
    var id: Int,
    var location: String,
    var userDTO: Any
)
data class UserDTs(
    var designation: String,
    var email: String,
    var mobileNo: String,
    var name: String
)
data class Gender(
    var codeValueType: CodeValueType,
    var id: Int,
    var value: String
)
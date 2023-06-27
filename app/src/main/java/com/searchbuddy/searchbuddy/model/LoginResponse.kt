package com.searchbuddy.searchbuddy.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("entitlements")
    val entitlements: List<Entitlement>,
    @SerializedName("jwttoken")
    val jwttoken: String,
    @SerializedName("userDTO")
    val userDTO: UserDTO,

    )

data class Role(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("role")
    val role: String
)

data class UserDTO(
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("candidateId")
    val candidateId: Int,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("designation")
    val designation: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("ids")
    val id: Int,
    @SerializedName("mobileNo")
    val mobileNo: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("referralCode")
    val referralCode: String,
    @SerializedName("profilePicName")
    var profilePicName:String
)

data class EntitlementX(
    val iconName: String,
    val id: Int,
    val pageName: String,
    val path: String
)

data class Entitlement(
    val entitlement: EntitlementX,
    val id: Int,
    val role: Role
)
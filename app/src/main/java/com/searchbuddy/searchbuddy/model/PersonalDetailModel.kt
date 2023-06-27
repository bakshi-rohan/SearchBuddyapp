package com.searchbuddy.searchbuddy.model

import ProfessionalDetail
import com.google.gson.annotations.SerializedName


data class PersonalDetailModel(
    val cv: String,
    val differentlyAbled: Boolean,
    val dob: String,
    val gender: GenderX,
    val highestDegree: String,
    val id: Int,
    val location: String,
    val professionalDetails: ProfessionalDetail,
    val userDTO: UserDTOx,
    val videoProfile: String,
    var cvPresent: Boolean,
    var videoPresent: Boolean,
    var educationDetailPresent: Boolean,
    var workExperiencePresent: Boolean
)

data class GenderX(
    val codeValueType: CodeValueType,
    val id: Int,
    val value: String
)

//data class ProfessionalDetail(
//    val noticePeriod: NoticePeriod
//)
data class UserDTOx(
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
    val profilePicName: String,
)

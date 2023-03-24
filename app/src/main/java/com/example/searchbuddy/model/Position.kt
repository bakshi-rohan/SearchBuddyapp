package com.example.searchbuddy.model

data class Position(
    val createdBy: String,
    val createdOn: String,
    val encryptedId: String,
    val id: Int,
    val positionId: Int,
    val positionName: String,
    val totalApplied: Int
)
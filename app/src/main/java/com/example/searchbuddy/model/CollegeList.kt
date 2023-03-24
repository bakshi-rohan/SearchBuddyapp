package com.example.searchbuddy.model

class CollegeList : ArrayList<CollegeListItem>()
data class CollegeListItem(
    val category1: String,
    val category2: String,
    val college: String,
    val id: String
)
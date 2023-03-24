package com.example.searchbuddy.model

class TopcomapniesResponse : ArrayList<TopcomapniesResponseItem>()
data class TopcomapniesResponseItem(
    val companyName: String,
    val location: String,
    val logo:String,
    val total: Int
)
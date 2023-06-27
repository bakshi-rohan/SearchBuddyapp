package com.searchbuddy.searchbuddy.model

 class IndustryList : ArrayList<IndustryListItem>()
data class IndustryListItem(
    val _class: String,
    val _id: String,
    val subIndustries: List<String>
)
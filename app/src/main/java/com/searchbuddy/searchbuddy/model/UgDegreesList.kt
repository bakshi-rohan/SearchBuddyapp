package com.searchbuddy.searchbuddy.model

class UgDegreesList : ArrayList<UgDegreesListItem>()
data class UgDegreesListItem(
    val UG: String,
    val specialisation:ArrayList<Any>
)
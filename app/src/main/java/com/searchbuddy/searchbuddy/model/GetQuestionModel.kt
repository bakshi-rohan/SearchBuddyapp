package com.searchbuddy.searchbuddy.model

data class GetQuestionModel(
    val organisationName: String,
    val positionName: String,
    val questions: List<Question>
)

data class Question(
    val id: Int,
    val multiple: Boolean,
    val mustHave: Boolean,
    val options: List<String>,
    val positionName: String,
    val question: String,
    val questionType: String
)
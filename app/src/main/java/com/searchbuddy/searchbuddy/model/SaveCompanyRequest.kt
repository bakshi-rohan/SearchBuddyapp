package com.searchbuddy.searchbuddy.model

data class SaveCompanyRequest(
    var companyName :String,
    var location:String,
    var startDate:String,
    var isPresent:String,
    var endDate:String,
    var designation:String,
    var function:Any,
    var noticePeriod: Any
)
//"companyName": "Infosys Ltd",
//
//"location": "Adilabad - Telangana",
//
//"startDate": "2022-08-31T18:30:00.000Z",
//
//"isPresent": "true",
//
//"endDate": null,
//
//"designation": "Manager"
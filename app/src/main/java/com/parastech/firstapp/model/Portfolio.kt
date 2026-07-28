package com.parastech.firstapp.model

data class Portfolio(
    var fullName: String = "",
    var profession: String = "",
    var about: String = "",
    var imageUri: String = "",

    var email: String = "",
    var phone: String = "",
    var location: String = "",
    var linkedin: String = "",
    var github: String = "",

    var skills: MutableList<String> = mutableListOf(),

    var college: String = "",
    var degree: String = "",
    var branch: String = "",
    var graduationYear: String = "",

    var projects: MutableList<Project> = mutableListOf(),

    var achievements: MutableList<Achievement> = mutableListOf()
)
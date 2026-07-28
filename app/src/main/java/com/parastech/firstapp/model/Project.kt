package com.parastech.firstapp.model

data class Project(
    var title: String = "",
    var description: String = "",
    var technologies: String = "",
    var github: String = "",
    var liveDemo: String = ""
)
data class Achievement(
    var title: String = "",
    var category: String = "Certificate",
    var organization: String = "",
    var issueDate: String = "",
    var credentialUrl: String = "",
    var description: String = ""
)
package com.parastech.firstapp.navigation

sealed class Screen(val route:String){

    object Home:Screen("home")

    object Projects:Screen("projects")

    object Skills:Screen("skills")

    object Achievements:Screen("achievements")

    object Contact:Screen("contact")

    object CreatePortfolio : Screen("create_portfolio")

    object ContactInfo : Screen("contact_info")

    object SkillsEducation : Screen("skills_education")

    object ProjectsBuilder : Screen("projects_builder")

    object AchievementsBuilder : Screen("achievements_builder")

    object Preview : Screen("preview")
}

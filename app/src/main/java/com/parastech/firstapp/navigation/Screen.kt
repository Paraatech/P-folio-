package com.parastech.firstapp.navigation

sealed class Screen(val route:String){

    object Home:Screen("home")

    object Projects:Screen("projects")

    object Skills:Screen("skills")

    object Achievements:Screen("achievements")

    object Contact:Screen("contact")

}
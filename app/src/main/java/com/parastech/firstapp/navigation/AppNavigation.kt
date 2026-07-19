package com.parastech.firstapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.parastech.firstapp.components.BottomBar
import com.parastech.firstapp.screens.AchievementsScreen
import com.parastech.firstapp.screens.ContactScreen
import com.parastech.firstapp.screens.HomeScreen
import com.parastech.firstapp.screens.ProjectsScreen
import com.parastech.firstapp.screens.SkillsScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Screen.Home.route) {
                HomeScreen(
                    onViewProjects = {
                        navController.navigate(Screen.Projects.route)
                    },
                    onContactMe = {
                        navController.navigate(Screen.Contact.route)
                    }
                )
            }
            composable(Screen.Projects.route) {
                ProjectsScreen()
            }

            composable(Screen.Skills.route) {
                SkillsScreen()
            }

            composable(Screen.Achievements.route) {
                AchievementsScreen()
            }

            composable(Screen.Contact.route) {
                ContactScreen()
            }
        }
    }
}
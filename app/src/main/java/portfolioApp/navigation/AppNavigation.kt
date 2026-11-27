package portfolioApp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import portfolioApp.components.BottomBar

import portfolioApp.screens.AchievementsScreen
import portfolioApp.screens.ContactScreen
import portfolioApp.screens.HomeScreen
import portfolioApp.screens.ProjectsScreen
import portfolioApp.screens.SkillsScreen
import portfolioApp.screens.CreatePortfolioScreen
import portfolioApp.screens.ContactInfoScreen
import portfolioApp.screens.AchievementBuilderScreen
import portfolioApp.screens.PreviewScreen
import portfolioApp.screens.ProjectsBuilderScreen
import portfolioApp.screens.SkillsEducationScreen
import portfolioApp.viewmodel.PortfolioViewModel
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val portfolioViewModel: PortfolioViewModel = viewModel()

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
                    },
                    onAddYours = {
                        navController.navigate(Screen.CreatePortfolio.route)
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
            composable(Screen.ContactInfo.route) {
                ContactInfoScreen(
                    viewModel = portfolioViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = {
                        navController.navigate(Screen.SkillsEducation.route)
                    }
                )
            }
            composable(Screen.CreatePortfolio.route) {
                CreatePortfolioScreen(
                    viewModel = portfolioViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = {
                        navController.navigate(Screen.ContactInfo.route)
                    }
                )
            }
            composable(Screen.SkillsEducation.route) {
                SkillsEducationScreen(
                    viewModel = portfolioViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = {
                        navController.navigate(Screen.ProjectsBuilder.route)
                    }
                )
            }
            composable(Screen.ProjectsBuilder.route) {
                ProjectsBuilderScreen(
                    viewModel = portfolioViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = {
                        navController.navigate(Screen.AchievementsBuilder.route)
                    }
                )
            }
            composable(Screen.AchievementsBuilder.route) {
                AchievementBuilderScreen(
                    viewModel = portfolioViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = {
                        navController.navigate(Screen.Preview.route)
                    }
                )
            }

            composable(Screen.Preview.route) {
                PreviewScreen(viewModel = portfolioViewModel)
            }

        }
    }
}




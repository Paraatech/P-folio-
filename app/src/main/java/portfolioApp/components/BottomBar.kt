package portfolioApp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import portfolioApp.navigation.Screen

@Composable
fun BottomBar(navController: NavController){

    val items=listOf(

        Screen.Home,

        Screen.Projects,

        Screen.Skills,

        Screen.Achievements,

        Screen.Contact

    )

    NavigationBar {

        val backStack=navController.currentBackStackEntryAsState()

        val current=backStack.value?.destination?.route

        items.forEach{

            NavigationBarItem(

                selected=current==it.route,

                onClick={
                    navController.navigate(it.route)
                },

                icon={

                    when(it){

                        Screen.Home-> Icon(Icons.Default.Home,null)

                        Screen.Projects-> Icon(Icons.Default.Work,null)

                        Screen.Skills-> Icon(Icons.Default.Build,null)

                        Screen.Achievements-> Icon(Icons.Default.Star,null)

                        Screen.Contact-> Icon(Icons.Default.Person,null)

                        Screen.CreatePortfolio -> Icon(Icons.Default.Add, null)

                        Screen.ContactInfo -> Icon(Icons.Default.Info, null)

                        Screen.SkillsEducation -> Icon(Icons.Default.School, null)

                        Screen.ProjectsBuilder -> Icon(Icons.Default.Settings, null)

                        Screen.AchievementsBuilder -> Icon(Icons.Default.Settings, null)
                        Screen.Preview -> Icon(Icons.Default.Settings, null)

                    }

                },

                label = {
                    Text(
                        when (it) {
                            Screen.Home -> "Home"
                            Screen.Projects -> "Projects"
                            Screen.Skills -> "Skills"
                            Screen.Achievements -> "Awards"
                            Screen.Contact -> "Me"
                            Screen.CreatePortfolio -> "Create"
                            Screen.ContactInfo -> "Info"
                            Screen.SkillsEducation -> "Edu"
                            Screen.ProjectsBuilder -> "Setup"
                            Screen.AchievementsBuilder -> "Setup"
                            Screen.Preview -> "Preview"
                        }
                    )
                },alwaysShowLabel = false
            )

        }

    }

}


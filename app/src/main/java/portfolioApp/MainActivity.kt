package portfolioApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import portfolioApp.ui.theme.FirstAppTheme
import portfolioApp.navigation.AppNavigation
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FirstAppTheme {
                AppNavigation()
            }
        }
    }
}


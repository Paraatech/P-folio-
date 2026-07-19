package com.parastech.firstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.parastech.firstapp.ui.theme.FirstAppTheme
import com.parastech.firstapp.navigation.AppNavigation
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
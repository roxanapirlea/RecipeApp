package com.roxana.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color.Companion.Transparent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.roxana.recipeapp.ui.theme.RecipeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppView()
        }
    }
}

@Composable
fun RecipeAppView() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Transparent,
            darkIcons = useDarkIcons
        )
        systemUiController.setStatusBarColor(
            color = Transparent,
            darkIcons = useDarkIcons
        )
        systemUiController.setNavigationBarColor(
            color = Transparent,
            darkIcons = useDarkIcons
        )

        onDispose {}
    }
    RecipeTheme {
        RecipeNavigation()
    }
}

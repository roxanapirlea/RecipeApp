package com.roxana.recipeapp.home

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun HomeScreen(onNavAddRecipe: () -> Unit = {}) {
    Scaffold(
        topBar = {
            AppBar(title = stringResource(R.string.home_title))
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavAddRecipe() }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }
    ) {
        Text(text = "Welcome home")
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    RecipeTheme {
        HomeScreen()
    }
}

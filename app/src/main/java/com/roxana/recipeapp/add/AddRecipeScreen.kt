package com.roxana.recipeapp.add

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun AddRecipeScreen(
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.add_title),
                icon = R.drawable.ic_arrow_back,
                onIconClick = { onBack() }
            )
        }
    ) {
        Text(text = "Add recipe")
    }
}

@Preview
@Composable
fun AddRecipeScreenPreview() {
    RecipeTheme {
        AddRecipeScreen()
    }
}

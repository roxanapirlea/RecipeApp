package com.roxana.recipeapp.add

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.ui.AddRecipeTextField
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.DividerAlpha40
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun AddRecipeScreen(
    addRecipeViewModel: AddRecipeViewModel,
    onBack: () -> Unit = {}
) {
    val state by addRecipeViewModel.state.collectAsState()
    AddRecipeView(state) {
        when (it) {
            Back -> onBack()
            is TitleChanged -> addRecipeViewModel.onTitleChanged(it.name)
        }
    }
}

@Composable
fun AddRecipeView(
    state: AddRecipeViewState,
    onAction: (AddRecipeViewAction) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.add_title),
                icon = R.drawable.ic_arrow_back
            ) { onAction(Back) }
        }
    ) { innerPadding ->
        val padding = PaddingValues(
            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
        )
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 8.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                AddRecipeTextField(
                    value = state.title.name,
                    onValueChange = { onAction(TitleChanged(it)) },
                    placeholder = stringResource(id = R.string.add_recipe_title_hint),
                    textStyle = MaterialTheme.typography.h5,
                    imeAction = ImeAction.Next,
                    modifier = Modifier.padding(padding)
                )
            }
            item { DividerAlpha40() }
        }
    }
}

@Preview
@Composable
fun AddRecipeScreenPreview() {
    RecipeTheme {
        AddRecipeView(AddRecipeViewState())
    }
}

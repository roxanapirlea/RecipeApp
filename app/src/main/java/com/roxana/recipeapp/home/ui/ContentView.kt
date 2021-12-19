package com.roxana.recipeapp.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.home.HomeViewAction
import com.roxana.recipeapp.home.HomeViewState
import com.roxana.recipeapp.home.RecipeDetail

@Composable
fun ContentView(
    state: HomeViewState.Content,
    onAction: (HomeViewAction) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp, start = 16.dp)
    ) {
        itemsIndexed(state.recipes) { index, recipe ->
            RecipeItem(recipeState = recipe, index = index, onClick = { onAction(RecipeDetail(it)) })
        }
    }
}

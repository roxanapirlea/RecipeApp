package com.roxana.recipeapp.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.home.HomeViewState

@Composable
fun ContentView(
    state: HomeViewState.Content,
    onFiltersClicked: () -> Unit,
    onRecipeSelected: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp, start = 16.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onFiltersClicked) {
                    BadgedBox(
                        badge = {
                            if (state.filtersSelectionCount != 0)
                                Badge(
                                    backgroundColor = MaterialTheme.colors.secondary,
                                    contentColor = MaterialTheme.colors.onSecondary
                                ) { Text(state.filtersSelectionCount.toString()) }
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_filters),
                            contentDescription = stringResource(R.string.all_filters)
                        )
                    }
                }
            }
        }
        if (state.recipes.isEmpty()) {
            item { Text(stringResource(R.string.home_empty_search)) }
        } else
            items(items = state.recipes, key = { recipe -> recipe.id }) { recipe ->
                RecipeItem(
                    recipeState = recipe,
                    index = recipe.id,
                    onClick = onRecipeSelected
                )
            }
    }
}

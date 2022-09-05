package com.roxana.recipeapp.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.home.HomeViewState
import com.roxana.recipeapp.home.RecipeState
import com.roxana.recipeapp.ui.FilterIcon
import com.roxana.recipeapp.ui.SearchTextField
import com.roxana.recipeapp.ui.basecomponents.Detail
import com.roxana.recipeapp.ui.theme.RecipeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListView(
    state: HomeViewState,
    modifier: Modifier = Modifier,
    onFiltersClicked: () -> Unit = {},
    onSearchQueryModified: (String) -> Unit = {},
    onRecipeSelected: (Int) -> Unit = {},
    onRandomRecipe: () -> Unit = {},
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        item {
            Row(Modifier.fillMaxWidth()) {
                SearchTextField(
                    value = state.query,
                    modifier = Modifier.weight(1f),
                    onValueChange = onSearchQueryModified
                )
                IconButton(onClick = onFiltersClicked) {
                    BadgedBox(
                        badge = {
                            if (state.filtersSelectionCount != 0)
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ) { Text(state.filtersSelectionCount.toString()) }
                        }
                    ) { FilterIcon() }
                }
            }
        }
        if (state.recipes.isEmpty()) {
            item { Detail(stringResource(R.string.home_empty_search)) }
        } else {
            item {
                RecipeRandomItem(
                    onClick = onRandomRecipe,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            items(items = state.recipes, key = { recipe -> recipe.id }) { recipe ->
                RecipeItem(
                    recipeState = recipe,
                    onClick = onRecipeSelected,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListPreview() {
    RecipeTheme {
        RecipeListView(
            HomeViewState(listOf(RecipeState(1, "Recipe 1"), RecipeState(2, "Recipe 2")))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListFilterPreview() {
    RecipeTheme {
        RecipeListView(
            HomeViewState(
                listOf(RecipeState(1, "Recipe 1"), RecipeState(2, "Recipe 2")),
                filtersSelectionCount = 2
            )
        )
    }
}

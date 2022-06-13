package com.roxana.recipeapp.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.home.HomeViewState
import com.roxana.recipeapp.ui.secondaryOutlineTextFiledColors

@Composable
fun ContentView(
    state: HomeViewState.Content,
    onFiltersClicked: () -> Unit,
    onSearchQueryModified: (String) -> Unit = {},
    onRecipeSelected: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp, start = 16.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth()) {
                SearchTextField(
                    value = state.filtersState.query,
                    modifier = Modifier.weight(1f),
                    onValueChange = onSearchQueryModified
                )
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

@Composable
fun SearchTextField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(stringResource(R.string.home_search))
        },
        leadingIcon = {
            Icon(Icons.Rounded.Search, null)
        },
        trailingIcon = {
            IconButton(onClick = { onValueChange("") }) {
                Icon(painterResource(R.drawable.ic_cross), stringResource(R.string.all_clear))
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Search
        ),
        singleLine = true,
        colors = secondaryOutlineTextFiledColors(),
        modifier = modifier
    )
}

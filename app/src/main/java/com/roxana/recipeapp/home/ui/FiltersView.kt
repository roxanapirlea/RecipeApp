package com.roxana.recipeapp.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.roxana.recipeapp.R
import com.roxana.recipeapp.home.FiltersState
import com.roxana.recipeapp.ui.SelectableCategory
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun FiltersView(
    state: FiltersState,
    modifier: Modifier = Modifier,
    onCategoryClicked: (UiCategoryType) -> Unit = {},
    onTotalTimeSelected: (Int) -> Unit = {},
    onPreparationTimeSelected: (Int) -> Unit = {},
    onCookingTimeSelected: (Int) -> Unit = {},
    onResetFiltersClicked: () -> Unit = {},
    onCloseFiltersClicked: () -> Unit = {}
) {
    Box(modifier.fillMaxSize()) {
        BackHandler(onBack = onCloseFiltersClicked)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                FlowRow(
                    mainAxisAlignment = FlowMainAxisAlignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    state.categories.forEach { category ->
                        SelectableCategory(
                            categoryType = category,
                            isSelected = state.selectedCategory == category,
                            onCategoryClicked = onCategoryClicked
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            if (state.maxTotalTime != null) {
                item {
                    TimeSlider(
                        label = stringResource(R.string.filters_select_time_total),
                        selected = state.selectedTotalTime!!,
                        max = state.maxTotalTime,
                        onSelected = onTotalTimeSelected,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            if (state.maxCookingTime != null) {
                item {
                    TimeSlider(
                        label = stringResource(R.string.filters_select_time_cooking),
                        selected = state.selectedCookingTime!!,
                        max = state.maxCookingTime,
                        onSelected = onCookingTimeSelected,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            if (state.maxPreparationTime != null) {
                item {
                    TimeSlider(
                        label = stringResource(R.string.filters_select_time_preparation),
                        selected = state.selectedPreparationTime!!,
                        max = state.maxPreparationTime,
                        onSelected = onPreparationTimeSelected
                    )
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
                ) {
                    FilledTonalButton(onClick = onResetFiltersClicked) {
                        Text(stringResource(R.string.all_reset))
                    }
                    FilledTonalButton(onClick = onCloseFiltersClicked) {
                        Text(stringResource(R.string.all_ok))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FiltersViewPreview() {
    RecipeTheme {
        FiltersView(
            FiltersState(
                maxTotalTime = 4,
                maxCookingTime = 4,
                maxPreparationTime = 4,
                categories = listOf(UiCategoryType.Breakfast, UiCategoryType.Dinner),
                selectedTotalTime = 1,
                selectedCookingTime = 2,
                selectedPreparationTime = 3,
                selectedCategory = UiCategoryType.Breakfast
            )
        )
    }
}

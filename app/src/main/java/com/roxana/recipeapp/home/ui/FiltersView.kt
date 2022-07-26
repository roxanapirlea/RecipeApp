package com.roxana.recipeapp.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.roxana.recipeapp.R
import com.roxana.recipeapp.home.FiltersState
import com.roxana.recipeapp.ui.SelectableCategory
import com.roxana.recipeapp.ui.button.TwoButtonRow
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            FlowRow(mainAxisAlignment = FlowMainAxisAlignment.Center) {
                state.categories.forEachIndexed { index, category ->
                    SelectableCategory(
                        categoryType = category,
                        isSelected = state.selectedCategory == category,
                        index = index,
                        onCategoryClicked = onCategoryClicked
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (state.maxTotalTime != null) {
                TimeSlider(
                    label = stringResource(
                        R.string.filters_select_time_total,
                        state.selectedTotalTime!!
                    ),
                    selected = state.selectedTotalTime,
                    max = state.maxTotalTime,
                    onSelected = onTotalTimeSelected,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            if (state.maxCookingTime != null) {
                Spacer(modifier = Modifier.height(8.dp))
                TimeSlider(
                    label = stringResource(
                        R.string.filters_select_time_cooking,
                        state.selectedCookingTime!!
                    ),
                    selected = state.selectedCookingTime,
                    max = state.maxCookingTime,
                    onSelected = onCookingTimeSelected,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            if (state.maxPreparationTime != null) {
                TimeSlider(
                    label = stringResource(
                        R.string.filters_select_time_preparation,
                        state.selectedPreparationTime!!
                    ),
                    selected = state.selectedPreparationTime,
                    max = state.maxPreparationTime,
                    onSelected = onPreparationTimeSelected
                )
            }
        }
        TwoButtonRow(
            modifier = Modifier.align(Alignment.BottomCenter),
            textStartButton = stringResource(R.string.all_reset),
            onClickStartButton = onResetFiltersClicked,
            textEndButton = stringResource(R.string.all_ok),
            onClickEndButton = onCloseFiltersClicked
        )
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

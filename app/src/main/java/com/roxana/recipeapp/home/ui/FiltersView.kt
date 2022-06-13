package com.roxana.recipeapp.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.roxana.recipeapp.ui.FlatSecondaryButton
import com.roxana.recipeapp.ui.SelectableCategory
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType
import kotlin.math.roundToInt

@Composable
fun FiltersView(
    state: FiltersState,
    onCategoryClicked: (UiCategoryType) -> Unit = {},
    onTotalTimeSelected: (Int) -> Unit = {},
    onPreparationTimeSelected: (Int) -> Unit = {},
    onCookingTimeSelected: (Int) -> Unit = {},
    onResetFiltersClicked: () -> Unit = {},
    onCloseFiltersClicked: () -> Unit = {}
) {
    Box(Modifier.fillMaxSize()) {
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
                        R.string.filters_max_time,
                        state.selectedTotalTime!!
                    ),
                    selected = state.selectedTotalTime,
                    max = state.maxTotalTime,
                    onSelected = onTotalTimeSelected
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (state.maxCookingTime != null) {
                Spacer(modifier = Modifier.height(8.dp))
                TimeSlider(
                    label = stringResource(
                        R.string.filters_max_time_cooking,
                        state.selectedCookingTime!!
                    ),
                    selected = state.selectedCookingTime,
                    max = state.maxCookingTime,
                    onSelected = onCookingTimeSelected
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (state.maxPreparationTime != null) {
                TimeSlider(
                    label = stringResource(
                        R.string.filters_max_time_preparation,
                        state.selectedPreparationTime!!
                    ),
                    selected = state.selectedPreparationTime,
                    max = state.maxPreparationTime,
                    onSelected = onPreparationTimeSelected
                )
            }
        }
        Surface(
            elevation = 4.dp,
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                FlatSecondaryButton(
                    onClick = onResetFiltersClicked,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    Text(stringResource(R.string.all_reset))
                }
                FlatSecondaryButton(
                    onClick = onCloseFiltersClicked,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    Text(stringResource(R.string.all_ok))
                }
            }
        }
    }
}

@Composable
fun TimeSlider(
    label: String,
    selected: Int,
    max: Int,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit = {}
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(label)
        Slider(
            value = selected.toFloat(),
            onValueChange = { onSelected(it.roundToInt()) },
            valueRange = 0f..max.toFloat()
        )
    }
}

@Preview
@Composable
fun FiltersViewPreview() {
    RecipeTheme {
        FiltersView(FiltersState())
    }
}

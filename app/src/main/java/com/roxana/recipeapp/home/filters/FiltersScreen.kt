package com.roxana.recipeapp.home.filters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.roxana.recipeapp.R
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.FlatSecondaryButton
import com.roxana.recipeapp.ui.SelectableCategory
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType
import kotlin.math.roundToInt

@Composable
fun FiltersScreen(
    filtersViewModel: FiltersViewModel,
    onBack: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(filtersViewModel.state)
        .collectAsState(FiltersViewState())

    FiltersView(
        state,
        onBack = onBack,
        onCategoryClicked = filtersViewModel::onCategoryClicked,
        onTotalTimeSelected = filtersViewModel::onTimeTotalSelected,
        onPreparationTimeSelected = filtersViewModel::onTimePreparationSelected,
        onCookingTimeSelected = filtersViewModel::onTimeCookingSelected,
        onApplyClicked = filtersViewModel::onApply,
        onResetClicked = filtersViewModel::onReset,
    )
}

@Composable
fun FiltersView(
    state: FiltersViewState,
    onBack: () -> Unit = {},
    onCategoryClicked: (UiCategoryType) -> Unit = {},
    onTotalTimeSelected: (Int) -> Unit = {},
    onPreparationTimeSelected: (Int) -> Unit = {},
    onCookingTimeSelected: (Int) -> Unit = {},
    onApplyClicked: () -> Unit = {},
    onResetClicked: () -> Unit = {},
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                navigationIcon =
                {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.all_cross),
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                },
                title = { Text(text = stringResource(R.string.all_filters)) },
                backgroundColor = MaterialTheme.colors.primarySurface
            )
        }
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
                        onSelected = { onTotalTimeSelected(it) }
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
                        onSelected = { onCookingTimeSelected(it) }
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
                        onSelected = { onPreparationTimeSelected(it) }
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
                        onClick = { onResetClicked() },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                    ) {
                        Text(stringResource(R.string.all_reset))
                    }
                    FlatSecondaryButton(
                        onClick = { onApplyClicked() },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                    ) {
                        Text(stringResource(R.string.filters_apply))
                    }
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
        FiltersView(FiltersViewState())
    }
}

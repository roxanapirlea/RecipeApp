package com.roxana.recipeapp.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.basecomponents.Detail
import com.roxana.recipeapp.ui.basecomponents.Label
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlin.math.roundToInt

@Composable
fun TimeSlider(
    label: String,
    selected: Int,
    max: Int,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit = {}
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Label(label, modifier = Modifier.padding(top = 32.dp))
        Detail(stringResource(R.string.filters_select_time, selected))
        Slider(
            value = selected.toFloat(),
            onValueChange = { onSelected(it.roundToInt()) },
            valueRange = 0f..max.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.tertiary,
                activeTrackColor = MaterialTheme.colorScheme.tertiary,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
    }
}

@Preview
@Composable
fun TimeSliderPreview() {
    RecipeTheme {
        TimeSlider(label = "Time slider", selected = 6, max = 10)
    }
}

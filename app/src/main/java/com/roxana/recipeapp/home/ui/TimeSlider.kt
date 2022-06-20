package com.roxana.recipeapp.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun TimeSliderPreview() {
    RecipeTheme {
        TimeSlider(label = "Time slider", selected = 6, max = 10)
    }
}

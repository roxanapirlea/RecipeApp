package com.roxana.recipeapp.settings.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.uimodel.UiTemperature

@Composable
fun ToggleTemperatures(
    items: List<UiTemperature>,
    selected: UiTemperature,
    modifier: Modifier = Modifier,
    onItemSelected: (UiTemperature) -> Unit = {}
) {
    Row(
        modifier = modifier
            .border(1.dp, MaterialTheme.colors.onBackground.copy(alpha = 0.3f), shape = CircleShape)
    ) {
        items.forEach { item ->
            ToggleButton(
                text = stringResource(item.text),
                checked = item == selected,
                onCheckedChange = { isChecked -> if (isChecked) onItemSelected(item) }
            )
        }
    }
}

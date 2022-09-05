package com.roxana.recipeapp.settings.ui

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.basecomponents.ToggleTextButton
import com.roxana.recipeapp.ui.theme.RecipeTheme
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
            .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f), shape = CircleShape)
    ) {
        items.forEach { item ->
            ToggleTextButton(
                text = stringResource(item.text),
                checked = item == selected,
                onCheckedChange = { isChecked -> if (isChecked) onItemSelected(item) }
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun ToggleTemperaturesPreviewLight() {
    RecipeTheme {
        ToggleTemperatures(
            items = listOf(UiTemperature.Celsius, UiTemperature.Fahrenheit),
            selected = UiTemperature.Celsius
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun ToggleTemperaturesPreviewDark() {
    RecipeTheme {
        ToggleTemperatures(
            items = listOf(UiTemperature.Celsius, UiTemperature.Fahrenheit),
            selected = UiTemperature.Celsius
        )
    }
}

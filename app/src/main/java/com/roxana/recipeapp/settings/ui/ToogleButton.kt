package com.roxana.recipeapp.settings.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun ToggleButton(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val fontWeight =
        if (checked) FontWeight.Black else FontWeight.Normal
    val background =
        if (checked) MaterialTheme.colors.secondary else MaterialTheme.colors.background

    IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.background(background, shape = CircleShape)
    ) {
        Text(
            text,
            fontWeight = fontWeight,
            color = contentColorFor(backgroundColor = background),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun ToggleButtonPreviewLight() {
    RecipeTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            ToggleButton(text = "Selected", checked = true)
            ToggleButton(text = "Unselected 1", checked = false)
            ToggleButton(text = "Unselected 2", checked = false)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun ToggleButtonPreviewDark() {
    RecipeTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            ToggleButton(text = "Selected", checked = true)
            ToggleButton(text = "Unselected 1", checked = false)
            ToggleButton(text = "Unselected 2", checked = false)
        }
    }
}

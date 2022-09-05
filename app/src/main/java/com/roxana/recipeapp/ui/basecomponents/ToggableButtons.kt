package com.roxana.recipeapp.ui.basecomponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ToggleTextButton(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val fontWeight =
        if (checked) FontWeight.Black else FontWeight.Normal

    FilledIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier
    ) {
        Text(
            text,
            fontWeight = fontWeight,
            modifier = Modifier.padding(8.dp)
        )
    }
}

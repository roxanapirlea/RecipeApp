package com.roxana.recipeapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckableItem(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    onCheckChanged: (Boolean) -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onCheckChanged(!isChecked) }.padding(end = 16.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckChanged
        )
        content()
    }
}

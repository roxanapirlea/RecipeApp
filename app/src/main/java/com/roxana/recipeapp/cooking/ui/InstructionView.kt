package com.roxana.recipeapp.cooking.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.basecomponents.CheckableText

@Composable
fun InstructionView(
    instruction: String,
    isChecked: Boolean,
    onCheckChanged: (Boolean) -> Unit
) {
    val decoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
    CheckableText(
        checked = isChecked,
        onCheckedChange = onCheckChanged,
        Modifier.padding(vertical = 4.dp)
    ) {
        Text(instruction, style = MaterialTheme.typography.bodyMedium, textDecoration = decoration)
    }
}

package com.roxana.recipeapp.cooking.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.roxana.recipeapp.ui.CheckableItem

@Composable
fun InstructionView(
    instruction: String,
    isChecked: Boolean,
    isCurrent: Boolean,
    onCheckChanged: (Boolean) -> Unit
) {
    val color = if (isCurrent) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
    val weight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Normal
    CheckableItem(
        isChecked = isChecked,
        onCheckChanged = onCheckChanged
    ) {
        Text(instruction, color = color, fontWeight = weight)
    }
}

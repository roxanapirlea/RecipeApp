package com.roxana.recipeapp.cooking.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.common.utilities.formatIngredient
import com.roxana.recipeapp.cooking.IngredientState
import com.roxana.recipeapp.ui.basecomponents.CheckableText

@Composable
fun IngredientView(
    ingredient: IngredientState,
    modifier: Modifier = Modifier,
    onCheckChanged: (Boolean) -> Unit = {}
) {
    val formattedIngredient = formatIngredient(
        ingredient.name,
        ingredient.quantityForSelectedPortion,
        ingredient.quantityType
    )
    val decoration = if (ingredient.isChecked) TextDecoration.LineThrough else TextDecoration.None
    CheckableText(
        checked = ingredient.isChecked,
        onCheckedChange = onCheckChanged,
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Text(
            formattedIngredient,
            textDecoration = decoration,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

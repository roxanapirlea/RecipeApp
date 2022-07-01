package com.roxana.recipeapp.cooking.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.roxana.recipeapp.cooking.IngredientState
import com.roxana.recipeapp.misc.formatIngredient
import com.roxana.recipeapp.ui.CheckableItem

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
    CheckableItem(
        isChecked = ingredient.isChecked,
        onCheckChanged = onCheckChanged,
        modifier = modifier
    ) {
        Text(text = formattedIngredient)
    }
}

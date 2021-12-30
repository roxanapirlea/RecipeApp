package com.roxana.recipeapp.cooking.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R
import com.roxana.recipeapp.cooking.IngredientState
import com.roxana.recipeapp.misc.toFormattedString
import com.roxana.recipeapp.ui.CheckableItem
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun IngredientView(
    ingredient: IngredientState,
    modifier: Modifier = Modifier,
    onCheckChanged: (Boolean) -> Unit = {}
) {
    val quantity = ingredient.quantityForSelectedPortion?.toFormattedString() ?: ""
    val quantityType =
        if (ingredient.quantityType is UiQuantityType.None)
            ""
        else stringResource(ingredient.quantityType.text)
    val formattedIngredient =
        if (ingredient.quantityForSelectedPortion == null)
            ingredient.name
        else
            stringResource(
                R.string.all_ingredient_placeholders,
                quantity,
                quantityType,
                ingredient.name
            )
    CheckableItem(
        isChecked = ingredient.isChecked,
        onCheckChanged = onCheckChanged,
        modifier = modifier
    ) {
        Text(text = formattedIngredient)
    }
}

package com.roxana.recipeapp.common.utilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R
import com.roxana.recipeapp.uimodel.UiQuantityType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun formatIngredient(name: String, quantity: Double?, quantityType: UiQuantityType): String {
    if (quantity == null) return name
    val formattedQuantity = pluralStringResource(
        quantityType.textForSelected,
        quantity.toInt(),
        quantity.toFormattedString()
    )
    return stringResource(R.string.all_ingredient_placeholders, formattedQuantity, name)
}

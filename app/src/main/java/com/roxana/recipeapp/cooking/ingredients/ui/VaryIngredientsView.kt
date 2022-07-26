package com.roxana.recipeapp.cooking.ingredients.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.cooking.ingredients.VaryIngredientsState
import com.roxana.recipeapp.ui.button.SecondaryButton
import com.roxana.recipeapp.ui.textfield.RecipePrimaryTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VaryIngredientsView(
    state: VaryIngredientsState,
    onIngredientSelected: (Int?) -> Unit = {},
    onQuantityChanged: (String) -> Unit = {},
    onValidate: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        IngredientMenu(
            selectedIngredient = state.updatedIngredient,
            ingredients = state.ingredients,
            onIngredientIdSelected = onIngredientSelected
        )
        state.updatedIngredient?.let { ingredient ->
            val quantityTypeText = pluralStringResource(
                ingredient.quantityType.textForSelected,
                ingredient.quantity.toInt(),
                ""
            )
            RecipePrimaryTextField(
                value = ingredient.quantityText,
                onValueChange = onQuantityChanged,
                label = stringResource(
                    R.string.vary_ingredients_label,
                    ingredient.name,
                    quantityTypeText
                ),
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                onImeAction = onValidate,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (state.updatedIngredient?.isQuantityInError == false)
            SecondaryButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp),
                onClick = onValidate
            ) { Text(stringResource(R.string.all_validate)) }
    }
}

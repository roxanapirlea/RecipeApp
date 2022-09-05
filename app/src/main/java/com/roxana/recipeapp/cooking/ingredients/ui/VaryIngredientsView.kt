package com.roxana.recipeapp.cooking.ingredients.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
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
import com.roxana.recipeapp.ui.basecomponents.Explanation
import com.roxana.recipeapp.ui.basecomponents.RecipeOutlinedTextField

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
            RecipeOutlinedTextField(
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
            FilledTonalButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp),
                onClick = onValidate
            ) { Text(stringResource(R.string.all_validate)) }

        Explanation(
            stringResource(R.string.vary_ingredients_explanation),
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(80.dp))
    }
}

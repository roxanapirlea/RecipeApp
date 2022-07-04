package com.roxana.recipeapp.cooking.ingredients

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.cooking.ingredients.ui.VaryIngredientsView

@Composable
fun VaryIngredientsDestination(
    varyIngredientsViewModel: VaryIngredientsViewModel,
    onNavQuantityMultiplierSet: (Double, Int) -> Unit = { _, _ -> }
) {
    val state by rememberFlowWithLifecycle(varyIngredientsViewModel.state)
        .collectAsState(VaryIngredientsState())

    state.validation?.let { validation ->
        LaunchedEffect(validation) {
            onNavQuantityMultiplierSet(validation.portionsMultiplier, validation.recipeId)
            varyIngredientsViewModel.onValidateDone()
        }
    }

    VaryIngredientsView(
        state,
        onIngredientSelected = varyIngredientsViewModel::onIngredientSelected,
        onQuantityChanged = varyIngredientsViewModel::onIngredientQuantityChanged,
        onValidate = varyIngredientsViewModel::onValidate
    )
}

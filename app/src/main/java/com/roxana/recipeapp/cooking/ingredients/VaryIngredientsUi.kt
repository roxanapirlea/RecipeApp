package com.roxana.recipeapp.cooking.ingredients

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.roxana.recipeapp.cooking.ingredients.ui.VaryIngredientsView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle

@Composable
fun VaryIngredientsDestination(
    varyIngredientsViewModel: VaryIngredientsViewModel,
    onNavQuantityMultiplierSet: (Double, Int) -> Unit = { _, _ -> }
) {
    val state by rememberFlowWithLifecycle(varyIngredientsViewModel.state)
        .collectAsState(VaryIngredientsState())

    LaunchedEffect(varyIngredientsViewModel.sideEffectFlow) {
        varyIngredientsViewModel.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is ValidateSuccess -> onNavQuantityMultiplierSet(
                    sideEffect.portionsMultiplier,
                    sideEffect.recipeId
                )
            }
        }
    }

    VaryIngredientsView(
        state,
        onIngredientSelected = varyIngredientsViewModel::onIngredientSelected,
        onQuantityChanged = varyIngredientsViewModel::onIngredientQuantityChanged,
        onValidate = varyIngredientsViewModel::onValidate
    )
}

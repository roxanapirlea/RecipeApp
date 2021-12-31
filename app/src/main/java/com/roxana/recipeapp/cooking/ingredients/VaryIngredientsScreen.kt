package com.roxana.recipeapp.cooking.ingredients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.FlatSecondaryButton

@Composable
fun VaryIngredientsScreen(
    varyIngredientsViewModel: VaryIngredientsViewModel,
    onQuantityMultiplierSet: (Double, Int) -> Unit = { _, _ -> }
) {
    val state by rememberFlowWithLifecycle(varyIngredientsViewModel.state)
        .collectAsState(VaryIngredientsState())

    LaunchedEffect(varyIngredientsViewModel.sideEffectFlow) {
        varyIngredientsViewModel.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is ValidateSuccess -> onQuantityMultiplierSet(
                    sideEffect.portionsMultiplier,
                    sideEffect.recipeId
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        IngredientMenu(
            selectedIngredient = state.updatedIngredient,
            ingredients = state.ingredients,
            onIngredientIdSelected = varyIngredientsViewModel::onIngredientSelected
        )
        state.updatedIngredient?.let { ingredient ->
            val quantityTypeText = stringResource(ingredient.quantityType.textForSelected)
            OutlinedTextField(
                value = ingredient.quantityText,
                onValueChange = varyIngredientsViewModel::onIngredientQuantityChanged,
                label = {
                    Text(
                        stringResource(
                            R.string.vary_ingredients_label,
                            ingredient.name,
                            quantityTypeText
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                textStyle = MaterialTheme.typography.body1,
                singleLine = true,
                keyboardActions = KeyboardActions { varyIngredientsViewModel.onValidate() },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (state.updatedIngredient?.isQuantityInError == false)
            FlatSecondaryButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp),
                onClick = varyIngredientsViewModel::onValidate
            ) { Text(stringResource(R.string.all_validate)) }
    }
}

@Composable
fun IngredientMenu(
    selectedIngredient: IngredientState?,
    ingredients: List<IngredientState>,
    onIngredientIdSelected: (Int?) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .defaultMinSize(48.dp, 48.dp)
        ) {
            if (selectedIngredient != null) {
                Text(selectedIngredient.name)
            } else {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = stringResource(R.string.vary_ingredients_type_hint))
                }
            }
            Icon(
                if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    onIngredientIdSelected(null)
                    isExpanded = false
                }
            ) { Text(stringResource(R.string.all_select)) }

            ingredients.forEach {
                DropdownMenuItem(
                    onClick = {
                        onIngredientIdSelected(it.id)
                        isExpanded = false
                    }
                ) { Text(it.name) }
            }
        }
    }
}

package com.roxana.recipeapp.edit.ingredients.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.ingredients.EditRecipeIngredientsViewState
import com.roxana.recipeapp.ui.DividerAlpha16
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun EditRecipeIngredientsView(
    state: EditRecipeIngredientsViewState,
    onIngredientNameChanged: (String) -> Unit = {},
    onIngredientQuantityChanged: (String) -> Unit = {},
    onIngredientQuantityTypeChanged: (UiQuantityType) -> Unit = {},
    onSaveIngredient: () -> Unit = {},
    onDelete: (Int) -> Unit = {},
    startFocusRequester: FocusRequester = remember { FocusRequester() },
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(3f))
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.ic_ingredients),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(40.dp)
            )
            LabelView(text = stringResource(R.string.all_ingredients))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        IngredientTextField(
            ingredient = state.editingIngredient,
            modifier = Modifier.fillMaxWidth(),
            quantityTypes = state.quantityTypes,
            canSave = state.canAddIngredient,
            onIngredientChange = onIngredientNameChanged,
            onQuantityChange = onIngredientQuantityChanged,
            onTypeChange = onIngredientQuantityTypeChanged,
            onSave = onSaveIngredient,
            startFocusRequester = startFocusRequester
        )
        if (state.ingredients.isNotEmpty())
            DividerAlpha16(Modifier.padding(bottom = 4.dp, top = 16.dp))
        LazyColumn {
            itemsIndexed(state.ingredients) { index, state ->
                IngredientText(
                    state,
                    Modifier.fillMaxWidth()
                ) { onDelete(index) }
            }
        }
        Spacer(modifier = Modifier.weight(6f))
    }
}

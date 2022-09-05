package com.roxana.recipeapp.cooking.ingredients.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.cooking.ingredients.IngredientState
import com.roxana.recipeapp.ui.basecomponents.Detail

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
                .defaultMinSize(48.dp, 48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedIngredient != null)
                Detail(selectedIngredient.name)
            else
                Detail(text = stringResource(R.string.vary_ingredients_type_hint))
            Icon(
                if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
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
                },
                text = { Text(stringResource(R.string.all_select)) }
            )

            ingredients.forEach {
                DropdownMenuItem(
                    onClick = {
                        onIngredientIdSelected(it.id)
                        isExpanded = false
                    },
                    text = { Text(it.name) }
                )
            }
        }
    }
}

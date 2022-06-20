package com.roxana.recipeapp.cooking.ingredients.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.cooking.ingredients.IngredientState

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

package com.roxana.recipeapp.edit.ingredients.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.formatIngredient
import com.roxana.recipeapp.edit.ingredients.IngredientState
import com.roxana.recipeapp.ui.basecomponents.Detail

@Composable
fun IngredientText(
    ingredient: IngredientState,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
) {
    val formattedIngredient = formatIngredient(
        ingredient.name,
        ingredient.quantity.toDoubleOrNull(),
        ingredient.quantityType
    )
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Detail(
            text = formattedIngredient,
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .weight(3f)
        )
        IconButton(
            onClick = onDelete,
            modifier = Modifier.padding(start = 6.dp)
        ) {
            Icon(
                Icons.Rounded.Delete,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(R.string.all_delete),
            )
        }
    }
}

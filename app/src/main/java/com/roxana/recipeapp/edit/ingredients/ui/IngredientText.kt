package com.roxana.recipeapp.edit.ingredients.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.ingredients.IngredientState
import com.roxana.recipeapp.misc.formatIngredient

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
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = formattedIngredient,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground,
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .weight(3f)
        )
        Icon(
            painterResource(R.drawable.ic_cross),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_delete),
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable { onDelete() }
                .padding(12.dp)
                .size(32.dp)
        )
    }
}

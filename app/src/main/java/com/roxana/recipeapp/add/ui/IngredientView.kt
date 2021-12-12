package com.roxana.recipeapp.add.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.IngredientState
import com.roxana.recipeapp.domain.QuantityType
import com.roxana.recipeapp.misc.toFormattedString
import com.roxana.recipeapp.misc.toStringRes
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun IngredientView(
    ingredient: IngredientState,
    quantityTypes: List<QuantityType>,
    onIngredientChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onTypeChange: (QuantityType) -> Unit,
    onDelete: () -> Unit,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (ingredient.isEditing) {
        IngredientTextField(
            ingredient = ingredient,
            quantityTypes = quantityTypes,
            onIngredientChange = { onIngredientChange(it) },
            onQuantityChange = { onQuantityChange(it) },
            onTypeChange = { onTypeChange(it) },
            onDelete = { onDelete() },
            modifier = modifier
        )
    } else {
        IngredientText(
            ingredient = ingredient,
            modifier = modifier.clickable { onSelect() }
        )
    }
}

@Composable
fun IngredientText(
    ingredient: IngredientState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(48.dp, 48.dp)
    ) {
        val quantity = ingredient.quantity.value?.toFormattedString() ?: ""
        val quantityType = ingredient.quantityType?.let {
            stringResource(it.toStringRes())
        } ?: ""
        val formattedIngredient =
            stringResource(
                R.string.all_ingredient_placeholders,
                quantity,
                quantityType,
                ingredient.name.text
            )
        Text(
            text = formattedIngredient,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .weight(1f)
        )
        Icon(
            painterResource(R.drawable.ic_edit),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_edit),
            modifier = Modifier
                .padding(start = 6.dp)
                .padding(vertical = 8.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun IngredientTextPreviewLight() {
    RecipeTheme {
        IngredientText(
            ingredient = IngredientState()
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun IngredientTextPreviewDark() {
    RecipeTheme {
        IngredientText(
            ingredient = IngredientState()
        )
    }
}

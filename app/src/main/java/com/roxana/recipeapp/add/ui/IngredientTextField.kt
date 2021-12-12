package com.roxana.recipeapp.add.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.IngredientState
import com.roxana.recipeapp.domain.QuantityType
import com.roxana.recipeapp.misc.toStringRes
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.ui.unlinedTextFiledColors

@Composable
fun IngredientTextField(
    ingredient: IngredientState,
    quantityTypes: List<QuantityType>,
    onIngredientChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onTypeChange: (QuantityType) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.weight(3f)) {
            TextField(
                value = ingredient.name ?: "",
                onValueChange = { onIngredientChange(it) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.add_recipe_ingredient_hint),
                        style = MaterialTheme.typography.body1
                    )
                },
                colors = unlinedTextFiledColors(),
                textStyle = MaterialTheme.typography.body1,
                singleLine = true,
                modifier = Modifier.defaultMinSize(0.dp, 0.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = ingredient.quantityText ?: "",
                    onValueChange = { onQuantityChange(it) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.add_recipe_quantity_hint),
                            style = MaterialTheme.typography.body1
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = unlinedTextFiledColors(),
                    textStyle = MaterialTheme.typography.body1,
                    singleLine = true,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .defaultMinSize(0.dp, 0.dp)
                        .weight(3f)
                )
                QuantityTypeMenu(
                    selectedQuantityType = ingredient.quantityType,
                    quantityTypes = quantityTypes,
                    onTypeChanged = onTypeChange,
                    isExpanded = isExpanded,
                    onIsExpandedChanged = { isExpanded = it }
                )
            }
        }
        Icon(
            painterResource(R.drawable.ic_cross),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_delete),
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable { onDelete() }
                .padding(12.dp)
        )
    }
}

@Composable
fun QuantityTypeMenu(
    selectedQuantityType: QuantityType?,
    quantityTypes: List<QuantityType>,
    onTypeChanged: (QuantityType) -> Unit,
    isExpanded: Boolean,
    onIsExpandedChanged: (Boolean) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .clickable { onIsExpandedChanged(!isExpanded) }
                .defaultMinSize(48.dp, 48.dp)
        ) {
            if (selectedQuantityType != null) {
                Text(text = stringResource(selectedQuantityType.toStringRes()))
            } else {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = stringResource(R.string.add_recipe_quantity_type_hint))
                }
            }
            Icon(
                if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onIsExpandedChanged(false) }
        ) {
            quantityTypes.forEach {
                DropdownMenuItem(
                    onClick = {
                        onTypeChanged(it)
                        onIsExpandedChanged(false)
                    }
                ) {
                    Text(stringResource(it.toStringRes()))
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun IngredientTextFieldEmptyPreviewLight() {
    RecipeTheme {
        IngredientTextField(
            ingredient = IngredientState(),
            quantityTypes = listOf(),
            onIngredientChange = {},
            onQuantityChange = {},
            onTypeChange = {},
            onDelete = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun IngredientTextFieldFilledPreviewLight() {
    RecipeTheme {
        IngredientTextField(
            ingredient = IngredientState(),
            quantityTypes = listOf(),
            onIngredientChange = {},
            onQuantityChange = {},
            onTypeChange = {},
            onDelete = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun IngredientTextFieldEmptyPreviewDark() {
    RecipeTheme {
        IngredientTextField(
            ingredient = IngredientState(),
            quantityTypes = listOf(),
            onIngredientChange = {},
            onQuantityChange = {},
            onTypeChange = {},
            onDelete = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun IngredientTextFieldFilledPreviewDark() {
    RecipeTheme {
        IngredientTextField(
            ingredient = IngredientState(),
            quantityTypes = listOf(),
            onIngredientChange = {},
            onQuantityChange = {},
            onTypeChange = {},
            onDelete = {}
        )
    }
}

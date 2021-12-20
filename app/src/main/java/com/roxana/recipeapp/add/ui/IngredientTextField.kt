package com.roxana.recipeapp.add.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.DoubleFieldState
import com.roxana.recipeapp.add.EmptyFieldState
import com.roxana.recipeapp.add.IngredientState
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun IngredientTextField(
    ingredient: IngredientState,
    quantityTypes: List<UiQuantityType>,
    onIngredientChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onTypeChange: (UiQuantityType) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    nameFocusRequester: FocusRequester = FocusRequester(),
    quantityFocusRequester: FocusRequester = FocusRequester()
) {

    var isExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.weight(3f)) {
            AddRecipeTextField(
                state = ingredient.name,
                onValueChange = { onIngredientChange(it) },
                placeholder = stringResource(R.string.add_recipe_ingredient_hint),
                textStyle = MaterialTheme.typography.body1,
                imeAction = ImeAction.Next,
                onImeAction = { quantityFocusRequester.requestFocus() },
                modifier = Modifier
                    .defaultMinSize(0.dp, 0.dp)
                    .focusRequester(nameFocusRequester)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                AddRecipeTextField(
                    state = ingredient.quantity,
                    onValueChange = { onQuantityChange(it) },
                    placeholder = stringResource(R.string.add_recipe_quantity_hint),
                    keyboardType = KeyboardType.Number,
                    textStyle = MaterialTheme.typography.body1,
                    imeAction = ImeAction.Next,
                    onImeAction = {
                        quantityFocusRequester.freeFocus()
                        isExpanded = true
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .defaultMinSize(0.dp, 0.dp)
                        .weight(3f)
                        .focusRequester(quantityFocusRequester)
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
    selectedQuantityType: UiQuantityType,
    quantityTypes: List<UiQuantityType>,
    onTypeChanged: (UiQuantityType) -> Unit,
    isExpanded: Boolean,
    onIsExpandedChanged: (Boolean) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .clickable { onIsExpandedChanged(!isExpanded) }
                .defaultMinSize(48.dp, 48.dp)
        ) {
            if (selectedQuantityType !is UiQuantityType.None) {
                Text(text = stringResource(selectedQuantityType.text))
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
                    Text(stringResource(it.text))
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
fun IngredientTextFieldPreviewLight() {
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
            ingredient = IngredientState(
                EmptyFieldState("Flour"),
                DoubleFieldState("2.0", 2.0),
                UiQuantityType.Cup
            ),
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
fun IngredientTextFieldPreviewDark() {
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
            ingredient = IngredientState(
                EmptyFieldState("Flour"),
                DoubleFieldState("2.0", 2.0),
                UiQuantityType.Cup
            ),
            quantityTypes = listOf(),
            onIngredientChange = {},
            onQuantityChange = {},
            onTypeChange = {},
            onDelete = {}
        )
    }
}

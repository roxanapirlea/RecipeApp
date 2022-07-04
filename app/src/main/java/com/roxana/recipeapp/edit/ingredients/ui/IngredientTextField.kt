package com.roxana.recipeapp.edit.ingredients.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.ingredients.IngredientState
import com.roxana.recipeapp.edit.ingredients.isQuantityValid
import com.roxana.recipeapp.ui.RecipeTextField
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun IngredientTextField(
    ingredient: IngredientState,
    quantityTypes: List<UiQuantityType>,
    modifier: Modifier = Modifier,
    startFocusRequester: FocusRequester = remember { FocusRequester() },
    onIngredientChange: (String) -> Unit = {},
    onQuantityChange: (String) -> Unit = {},
    onTypeChange: (UiQuantityType) -> Unit = {},
    onSave: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    val nameFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        startFocusRequester.requestFocus()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.weight(3f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RecipeTextField(
                    value = ingredient.quantity,
                    onValueChange = { onQuantityChange(it) },
                    isError = !ingredient.isQuantityValid(),
                    label = stringResource(R.string.edit_recipe_quantity_hint),
                    keyboardType = KeyboardType.Number,
                    textStyle = MaterialTheme.typography.body1,
                    imeAction = ImeAction.Next,
                    onImeAction = { isExpanded = true },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .defaultMinSize(0.dp, 0.dp)
                        .weight(3f)
                        .focusRequester(startFocusRequester)
                )
                QuantityTypeMenu(
                    selectedQuantityType = ingredient.quantityType,
                    quantityTypes = quantityTypes,
                    onTypeChanged = {
                        onTypeChange(it)
                        nameFocusRequester.requestFocus()
                    },
                    isExpanded = isExpanded,
                    onIsExpandedChanged = {
                        if (it) startFocusRequester.freeFocus()
                        isExpanded = it
                    }
                )
            }
            RecipeTextField(
                value = ingredient.name,
                onValueChange = { onIngredientChange(it) },
                label = stringResource(R.string.edit_recipe_ingredient_hint),
                textStyle = MaterialTheme.typography.body1,
                keyboardType = KeyboardType.Text,
                capitalisation = KeyboardCapitalization.None,
                imeAction = ImeAction.Done,
                onImeAction = {
                    onSave()
                    startFocusRequester.requestFocus()
                },
                modifier = Modifier
                    .defaultMinSize(0.dp, 0.dp)
                    .focusRequester(nameFocusRequester)
            )
        }
        Icon(
            painterResource(R.drawable.ic_check_outline),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_save),
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable {
                    onSave()
                    startFocusRequester.requestFocus()
                }
                .padding(12.dp)
                .size(32.dp)
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
                Text(text = stringResource(selectedQuantityType.textForSelect))
            } else {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = stringResource(R.string.edit_recipe_quantity_type_hint))
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
                    Text(stringResource(it.textForSelect))
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
            onSave = {}
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
            ingredient = IngredientState(null, "Flour", "2.0", UiQuantityType.Cup),
            quantityTypes = listOf(),
            onIngredientChange = {},
            onQuantityChange = {},
            onTypeChange = {},
            onSave = {}
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
            onSave = {}
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
            ingredient = IngredientState(null, "Flour", "2.0", UiQuantityType.Cup),
            quantityTypes = listOf(),
            onIngredientChange = {},
            onQuantityChange = {},
            onTypeChange = {},
            onSave = {}
        )
    }
}

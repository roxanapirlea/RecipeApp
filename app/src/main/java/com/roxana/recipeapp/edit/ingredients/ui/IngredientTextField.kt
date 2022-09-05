package com.roxana.recipeapp.edit.ingredients.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.ingredients.IngredientState
import com.roxana.recipeapp.ui.basecomponents.RecipeOutlinedTextField
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
                RecipeOutlinedTextField(
                    value = ingredient.quantity,
                    onValueChange = { onQuantityChange(it) },
                    isError = ingredient.isQuantityError,
                    label = stringResource(R.string.edit_recipe_quantity_hint),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    onImeAction = { isExpanded = true },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .defaultMinSize(0.dp, 0.dp)
                        .weight(2f)
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
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            RecipeOutlinedTextField(
                value = ingredient.name,
                onValueChange = { onIngredientChange(it) },
                label = stringResource(R.string.edit_recipe_ingredient_hint),
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityTypeMenu(
    selectedQuantityType: UiQuantityType,
    quantityTypes: List<UiQuantityType>,
    onTypeChanged: (UiQuantityType) -> Unit,
    isExpanded: Boolean,
    onIsExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onIsExpandedChanged,
        modifier = modifier.defaultMinSize(0.dp, 0.dp)
    ) {
        TextField(
            readOnly = true,
            value = if (selectedQuantityType !is UiQuantityType.None)
                stringResource(selectedQuantityType.textForSelect)
            else
                stringResource(R.string.edit_recipe_quantity_type_hint),
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.defaultMinSize(0.dp, 0.dp)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onIsExpandedChanged(false) },
            modifier = Modifier.defaultMinSize(0.dp, 0.dp)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(UiQuantityType.None.textForSelect)) },
                onClick = {
                    onTypeChanged(UiQuantityType.None)
                    onIsExpandedChanged(false)
                }
            )
            quantityTypes.forEach {
                DropdownMenuItem(
                    text = { Text(stringResource(it.textForSelect)) },
                    onClick = {
                        onTypeChanged(it)
                        onIsExpandedChanged(false)
                    }
                )
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
            ingredient = IngredientState(null, "Flour", "2.0", false, UiQuantityType.Cup),
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
            ingredient = IngredientState(null, "Flour", "2.0", false, UiQuantityType.Cup),
            quantityTypes = listOf(),
            onIngredientChange = {},
            onQuantityChange = {},
            onTypeChange = {},
            onSave = {}
        )
    }
}

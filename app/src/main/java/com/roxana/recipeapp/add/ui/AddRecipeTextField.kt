package com.roxana.recipeapp.add.ui

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.ui.unlinedTextFiledColors

@Composable
fun AddRecipeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = placeholder, style = textStyle) },
        leadingIcon = iconRes?.let {
            { Icon(painter = painterResource(id = iconRes), contentDescription = null) }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        singleLine = true,
        colors = unlinedTextFiledColors(),
        shape = RectangleShape,
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun AddRecipeTextFieldEmptyPreviewLight() {
    RecipeTheme {
        AddRecipeTextField(value = "", onValueChange = {}, placeholder = "Placeholder")
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun AddRecipeTextFieldFilledPreviewLight() {
    RecipeTheme {
        AddRecipeTextField(value = "Crepe", onValueChange = {}, placeholder = "Placeholder")
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun AddRecipeTextFieldEmptyPreviewDark() {
    RecipeTheme {
        AddRecipeTextField(value = "", onValueChange = {}, placeholder = "Placeholder")
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun AddRecipeTextFieldFilledPreviewDark() {
    RecipeTheme {
        AddRecipeTextField(value = "Crepe", onValueChange = {}, placeholder = "Placeholder")
    }
}

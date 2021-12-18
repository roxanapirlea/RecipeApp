package com.roxana.recipeapp.add.ui

import android.content.res.Configuration
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
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.EmptyFieldState
import com.roxana.recipeapp.add.TextFieldState
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.ui.unlinedTextFiledColors

@Composable
fun AddRecipeTextField(
    state: TextFieldState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    label: String? = null,
    leading: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: () -> Unit = {}
) {
    val isError = !state.isValid
    TextField(
        value = state.text,
        onValueChange = { onValueChange(it) },
        isError = isError,
        placeholder = placeholder?.let { { Text(text = placeholder, style = textStyle) } },
        label = label?.let { { Text(text = label) } },
        leadingIcon = leading,
        trailingIcon = if (isError) {
            { Icon(painterResource(R.drawable.ic_error), null) }
        } else null,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions { onImeAction() },
        textStyle = textStyle,
        singleLine = true,
        colors = unlinedTextFiledColors(),
        shape = RectangleShape,
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun AddRecipeTextFieldPreviewLight() {
    RecipeTheme {
        AddRecipeTextField(
            state = EmptyFieldState(),
            onValueChange = {},
            placeholder = "Placeholder"
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun AddRecipeTextFieldPreviewDark() {
    RecipeTheme {
        AddRecipeTextField(
            state = EmptyFieldState(),
            onValueChange = {},
            placeholder = "Placeholder"
        )
    }
}

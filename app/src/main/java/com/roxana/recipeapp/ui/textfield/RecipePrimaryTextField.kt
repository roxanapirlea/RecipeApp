package com.roxana.recipeapp.ui.textfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun RecipePrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    placeholder: String? = null,
    label: String? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    capitalisation: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    onImeAction: () -> Unit = {},
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        placeholder = placeholder?.let { { Text(text = placeholder, style = textStyle) } },
        label = label?.let { { Text(text = label) } },
        leadingIcon = leading,
        trailingIcon = trailing,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = capitalisation,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions { onImeAction() },
        textStyle = textStyle,
        singleLine = true,
        colors = primaryOutlineTextFiledColors(),
        modifier = modifier
    )
}

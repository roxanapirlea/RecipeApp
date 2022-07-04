package com.roxana.recipeapp.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.roxana.recipeapp.R

@Composable
fun RecipeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    placeholder: String? = null,
    label: String? = null,
    leading: @Composable (() -> Unit)? = null,
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
        trailingIcon = if (isError) {
            { Icon(painterResource(R.drawable.ic_error), null) }
        } else null,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = capitalisation,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions { onImeAction() },
        textStyle = textStyle,
        singleLine = true,
        colors = secondaryOutlineTextFiledColors(),
        modifier = modifier
    )
}

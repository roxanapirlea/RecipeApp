package com.roxana.recipeapp.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.roxana.recipeapp.R

@Composable
fun SearchTextField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(stringResource(R.string.all_search))
        },
        leadingIcon = {
            Icon(Icons.Rounded.Search, null)
        },
        trailingIcon = {
            IconButton(onClick = { onValueChange("") }) {
                Icon(painterResource(R.drawable.ic_cross), stringResource(R.string.all_clear))
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Search
        ),
        singleLine = true,
        colors = secondaryOutlineTextFiledColors(),
        modifier = modifier
    )
}

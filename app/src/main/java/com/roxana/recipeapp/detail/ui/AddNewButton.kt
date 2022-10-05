package com.roxana.recipeapp.detail.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.basecomponents.FilledTonalIconTextButton

@Composable
fun AddNewButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FilledTonalIconTextButton(
        onClick = onClick,
        text = { Text(stringResource(id = R.string.all_add_new)) },
        leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) },
        modifier = modifier
    )
}

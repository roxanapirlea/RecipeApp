package com.roxana.recipeapp.ui.basecomponents

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R

@Composable
fun AppBarBack(
    title: String = stringResource(R.string.app_name),
    actions: @Composable RowScope.() -> Unit = {},
    onIconClick: () -> Unit = {}
) {
    AppBar(
        title = title,
        icon = {
            IconButton(onClick = onIconClick) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.all_back)
                )
            }
        },
        actions = actions
    )
}

@Composable
fun AppBarClose(
    title: String = stringResource(R.string.app_name),
    actions: @Composable RowScope.() -> Unit = {},
    onIconClick: () -> Unit = {}
) {
    AppBar(
        title = title,
        icon = {
            IconButton(onClick = onIconClick) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = stringResource(R.string.all_close)
                )
            }
        },
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String = stringResource(R.string.app_name),
    icon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    onIconClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (icon != null) IconButton(onClick = onIconClick, content = icon)
        },
        title = { Text(text = title) },
        actions = actions
    )
}

package com.roxana.recipeapp.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun AppBar(
    title: String = stringResource(R.string.app_name),
    actions: @Composable RowScope.() -> Unit = {},
    onIconClick: () -> Unit = {}
) {
    SlotAppBar(
        title = title,
        icon = {
            IconButton(onClick = onIconClick) { BackIcon(Modifier.padding(horizontal = 12.dp)) }
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
    SlotAppBar(
        title = title,
        icon = {
            IconButton(onClick = onIconClick) { CloseIcon(Modifier.padding(horizontal = 12.dp)) }
        },
        actions = actions
    )
}

@Composable
fun SlotAppBar(
    title: String = stringResource(R.string.app_name),
    icon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    onIconClick: () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = icon?.let {
            { IconButton(onClick = onIconClick, content = it) }
        },
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primarySurface,
        actions = actions
    )
}

@Preview
@Composable
fun AppBarBackPreview() {
    RecipeTheme {
        AppBar("Title")
    }
}

@Preview
@Composable
fun AppBarClosePreview() {
    RecipeTheme {
        AppBarClose("Title")
    }
}

@Preview
@Composable
fun SlotAppBarPreview() {
    RecipeTheme {
        SlotAppBar("Title")
    }
}

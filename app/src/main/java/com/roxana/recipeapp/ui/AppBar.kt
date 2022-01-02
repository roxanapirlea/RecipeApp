package com.roxana.recipeapp.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R

@Composable
fun AppBar(
    title: String = stringResource(R.string.app_name),
    icon: Int? = null,
    actions: @Composable RowScope.() -> Unit = {},
    onIconClick: () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = icon?.let {
            {
                IconButton(onClick = { onIconClick() }) {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        },
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primarySurface,
        actions = actions
    )
}

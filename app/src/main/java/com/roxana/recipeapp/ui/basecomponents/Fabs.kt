package com.roxana.recipeapp.ui.basecomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExtendedTextIconFab(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(onClick = onClick, modifier = modifier) {
        FabIconTextContent(text, leadingIcon)
    }
}

@Composable
private fun FabIconTextContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit),
) {
    Box(Modifier.padding(end = 8.dp)) {
        leadingIcon()
    }
    Box(modifier = Modifier.padding(top = 4.dp, end = 4.dp)) {
        text()
    }
}

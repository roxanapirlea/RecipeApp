package com.roxana.recipeapp.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DividerAlpha40(modifier: Modifier = Modifier) {
    Divider(
        color = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.disabled),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun DividerAlpha16(modifier: Modifier = Modifier) {
    Divider(
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.16f),
        modifier = modifier.fillMaxWidth()
    )
}

package com.roxana.recipeapp.ui.basecomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DividerAlpha16(modifier: Modifier = Modifier) {
    Divider(
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.16f),
        modifier = modifier.fillMaxWidth()
    )
}

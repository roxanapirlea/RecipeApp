package com.roxana.recipeapp.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CenteredTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.h3,
        color = MaterialTheme.colors.primary,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

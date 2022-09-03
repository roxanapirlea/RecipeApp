package com.roxana.recipeapp.ui.basecomponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Title(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
fun Label(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = color,
        modifier = modifier
    )
}

@Composable
fun Detail(text: String, modifier: Modifier = Modifier,) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun Explanation(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
        modifier = modifier
    )
}

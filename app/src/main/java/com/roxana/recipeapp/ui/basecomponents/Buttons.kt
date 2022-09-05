package com.roxana.recipeapp.ui.basecomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilledIconTextButton(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(enabled = enabled, onClick = onClick, modifier = modifier) {
        ButtonIconTextContent(text, leadingIcon)
    }
}

@Composable
fun OutlinedIconTextButton(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(enabled = enabled, onClick = onClick, modifier = modifier) {
        ButtonIconTextContent(text, leadingIcon)
    }
}

@Composable
fun TextButtonWithIcon(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(enabled = enabled, onClick = onClick, modifier = modifier) {
        ButtonIconTextContent(text, leadingIcon)
    }
}

@Composable
fun ElevatedIconTextButton(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ElevatedButton(enabled = enabled, onClick = onClick, modifier = modifier) {
        ButtonIconTextContent(text, leadingIcon)
    }
}

@Composable
fun FilledTonalIconTextButton(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilledTonalButton(enabled = enabled, onClick = onClick, modifier = modifier) {
        ButtonIconTextContent(text, leadingIcon)
    }
}

@Composable
private fun ButtonIconTextContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit),
) {
    Box(
        Modifier
            .padding(end = 8.dp)
            .sizeIn(maxHeight = 18.dp)
    ) {
        leadingIcon()
    }
    Box(modifier = Modifier.padding(top = 2.dp, end = 4.dp)) {
        text()
    }
}

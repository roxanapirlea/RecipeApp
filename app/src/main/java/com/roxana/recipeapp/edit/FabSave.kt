package com.roxana.recipeapp.edit

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.CheckIcon

@Composable
fun FabSave(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        modifier = modifier.padding(16.dp),
        onClick = onClick
    ) { CheckIcon() }
}

package com.roxana.recipeapp.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R

@Composable
fun IconButtonCheckOutline(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painterResource(R.drawable.ic_check_outline),
            stringResource(R.string.all_save)
        )
    }
}

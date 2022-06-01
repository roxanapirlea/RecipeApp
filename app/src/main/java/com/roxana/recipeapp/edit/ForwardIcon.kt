package com.roxana.recipeapp.edit

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R

@Composable
fun ForwardIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Rounded.ArrowForward,
        contentDescription = stringResource(R.string.all_go_to_next_screen)
    )
}

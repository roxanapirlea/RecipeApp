package com.roxana.recipeapp.ui

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R

@Composable
fun AddIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Rounded.Add,
        contentDescription = stringResource(R.string.all_add_new),
        modifier = modifier
    )
}

@Composable
fun SettingsIcon(modifier: Modifier = Modifier) {
    Icon(
        Icons.Rounded.Settings,
        contentDescription = stringResource(R.string.all_settings),
        modifier = modifier
    )
}

@Composable
fun CloseIcon(modifier: Modifier = Modifier) {
    Icon(
        Icons.Rounded.Clear,
        contentDescription = stringResource(R.string.all_close),
        modifier = modifier
    )
}

@Composable
fun BackIcon(modifier: Modifier = Modifier) {
    Icon(
        painterResource(R.drawable.ic_arrow_back),
        contentDescription = stringResource(R.string.all_back),
        modifier = modifier
    )
}

@Composable
fun FilterIcon(modifier: Modifier = Modifier) {
    Icon(
        painterResource(R.drawable.ic_filters),
        contentDescription = stringResource(R.string.all_filters),
        modifier = modifier
    )
}

@Composable
fun ForwardIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Rounded.ArrowForward,
        contentDescription = stringResource(R.string.all_go_to_next_screen)
    )
}

package com.roxana.recipeapp.detail.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.basecomponents.CardTitleDetail

@Composable
fun ServingsView(
    servings: Short,
    modifier: Modifier = Modifier
) {
    CardTitleDetail(
        modifier = modifier,
        leadingIcon = {
            Icon(painterResource(id = R.drawable.ic_portions), contentDescription = null)
        },
        title = stringResource(R.string.detail_portions_label),
        detail = servings.toString()
    )
}

package com.roxana.recipeapp.cooking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.misc.toFormattedString

@Composable
fun AdjustablePortions(
    portions: Double,
    modifier: Modifier = Modifier,
    onPortionsIncrease: () -> Unit = {},
    onPortionsDecrease: () -> Unit = {},
    onPortionsReset: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = onPortionsDecrease) {
            Icon(
                painterResource(R.drawable.ic_substract),
                contentDescription = stringResource(R.string.cooking_minus),
                tint = MaterialTheme.colors.primary
            )
        }
        Text(
            text = portions.toFormattedString(),
            style = MaterialTheme.typography.body2
        )
        IconButton(onClick = onPortionsIncrease) {
            Icon(
                painterResource(R.drawable.ic_add),
                contentDescription = stringResource(R.string.cooking_plus),
                tint = MaterialTheme.colors.primary
            )
        }
        TextButton(onClick = onPortionsReset) {
            Text(stringResource(R.string.all_reset))
        }
    }
}

package com.roxana.recipeapp.cooking.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.toFormattedString
import com.roxana.recipeapp.ui.basecomponents.Detail

@Composable
fun AdjustablePortions(
    portions: Double,
    modifier: Modifier = Modifier,
    onPortionsIncrease: () -> Unit = {},
    onPortionsDecrease: () -> Unit = {},
    onPortionsReset: () -> Unit = {},
    onCustomPortions: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            FilledIconButton(onClick = onPortionsDecrease) {
                Icon(
                    painterResource(R.drawable.ic_substract),
                    contentDescription = stringResource(R.string.cooking_minus)
                )
            }
            Detail(text = portions.toFormattedString())
            FilledIconButton(onClick = onPortionsIncrease) {
                Icon(
                    painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.cooking_plus)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            FilledTonalButton(onClick = onCustomPortions, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.cooking_custom_portions), Modifier.padding(end = 8.dp))
            }
            FilledTonalButton(onClick = onPortionsReset, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.all_reset))
            }
        }
    }
}

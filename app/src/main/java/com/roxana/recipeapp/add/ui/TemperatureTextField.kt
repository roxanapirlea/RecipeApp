package com.roxana.recipeapp.add.ui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.IntFieldState

@Composable
fun TemperatureTextField(
    value: IntFieldState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AddRecipeTextField(
        state = value,
        onValueChange = { onValueChange(it) },
        placeholder = stringResource(R.string.add_recipe_temperature_hint),
        leading = {
            Icon(painterResource(R.drawable.ic_temperature), null)
        },
        keyboardType = KeyboardType.Number,
        textStyle = MaterialTheme.typography.body1,
        modifier = modifier.defaultMinSize(0.dp, 0.dp)
    )
}

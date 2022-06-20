package com.roxana.recipeapp.edit.temperature.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.temperature.EditRecipeTemperatureViewState
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.RecipeTextField

@Composable
fun EditRecipeTemperatureView(
    state: EditRecipeTemperatureViewState,
    focusRequester: FocusRequester,
    onTemperatureChanged: (String) -> Unit = {},
    onValidate: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(3f))
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.ic_temperature),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(40.dp)
            )
            val temperatureUnit = stringResource(state.temperatureUnit.text)
            LabelView(
                text = stringResource(
                    R.string.edit_recipe_temperature_label,
                    temperatureUnit
                )
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
        RecipeTextField(
            value = state.temperature,
            onValueChange = onTemperatureChanged,
            placeholder = stringResource(R.string.edit_recipe_temperature_hint),
            imeAction = ImeAction.Done,
            onImeAction = onValidate,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
        Spacer(modifier = Modifier.weight(6f))
    }
}

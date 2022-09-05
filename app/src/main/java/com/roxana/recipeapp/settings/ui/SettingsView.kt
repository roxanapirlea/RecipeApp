package com.roxana.recipeapp.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.settings.SettingsViewState
import com.roxana.recipeapp.ui.basecomponents.Explanation
import com.roxana.recipeapp.ui.basecomponents.Label
import com.roxana.recipeapp.ui.basecomponents.SwitchText
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

@Composable
fun SettingsView(
    state: SettingsViewState,
    modifier: Modifier = Modifier,
    onTemperatureSelected: (UiTemperature) -> Unit = {},
    onMeasuringUnitChanged: (UiQuantityType, Boolean) -> Unit = { _, _ -> }
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            Label(
                stringResource(R.string.settings_temperature_unit),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }
        item {
            ToggleTemperatures(
                items = state.temperatures,
                selected = state.selectedTemperature,
                onItemSelected = { onTemperatureSelected(it) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Label(
                stringResource(R.string.settings_measuring_units),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }
        item {
            Explanation(
                text = stringResource(R.string.settings_measuring_units_detail),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        items(state.measuringUnits) { measuringUnit ->
            SwitchText(
                checked = measuringUnit.isChecked,
                onCheckedChange = { onMeasuringUnitChanged(measuringUnit.unit, it) },
                text = stringResource(measuringUnit.unit.longDescription)
            )
        }
    }
}

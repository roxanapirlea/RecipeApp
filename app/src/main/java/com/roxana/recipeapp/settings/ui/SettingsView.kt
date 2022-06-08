package com.roxana.recipeapp.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.BuildConfig
import com.roxana.recipeapp.R
import com.roxana.recipeapp.settings.SettingsViewState
import com.roxana.recipeapp.ui.CheckableItem
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

@Composable
fun SettingsView(
    state: SettingsViewState,
    onDebugSettingsClicked: () -> Unit = {},
    onTemperatureSelected: (UiTemperature) -> Unit = {},
    onMeasuringUnitChanged: (UiQuantityType, Boolean) -> Unit = { _, _ -> }
) {
    LazyColumn(contentPadding = PaddingValues(16.dp), modifier = Modifier.fillMaxWidth()) {
        if (BuildConfig.DEBUG) {
            item {
                TextButton(
                    modifier = Modifier.padding(vertical = 16.dp),
                    onClick = onDebugSettingsClicked
                ) {
                    Text(stringResource(R.string.debug_settings))
                }
            }
        }
        item {
            LabelView(
                stringResource(R.string.settings_temperature_unit),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        item {
            ToggleTemperatures(
                items = state.temperatures,
                selected = state.selectedTemperature,
                onItemSelected = { onTemperatureSelected(it) }
            )
        }
        item {
            LabelView(
                stringResource(R.string.settings_measuring_units),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        item {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(R.string.settings_measuring_units_detail),
                    style = MaterialTheme.typography.caption
                )
            }
        }
        items(state.measuringUnits) { measuringUnit ->
            CheckableItem(
                isChecked = measuringUnit.isChecked,
                onCheckChanged = { onMeasuringUnitChanged(measuringUnit.unit, it) }
            ) {
                Text(stringResource(measuringUnit.unit.longDescription))
            }
        }
    }
}

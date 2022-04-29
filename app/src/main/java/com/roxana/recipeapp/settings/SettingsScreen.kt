package com.roxana.recipeapp.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.BuildConfig
import com.roxana.recipeapp.R
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.settings.ui.ToggleTemperatures
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.CheckableItem
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit = {},
    onDebugSettings: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(settingsViewModel.state)
        .collectAsState(SettingsViewState())

    SettingsView(state) { action ->
        when (action) {
            Back -> onBack()
            DebugSettingsSelected -> onDebugSettings()
            is TemperatureSelected -> settingsViewModel.onTemperatureSelected(action.temperature)
            is MeasuringUnitChanged ->
                settingsViewModel.onMeasuringUnitChanged(action.unit, action.isChecked)
        }
    }
}

@Composable
fun SettingsView(
    state: SettingsViewState,
    onAction: (SettingsViewAction) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                title = stringResource(R.string.home_title),
                icon = R.drawable.ic_arrow_back
            ) { onAction(Back) }
        }
    ) {
        LazyColumn(contentPadding = PaddingValues(16.dp), modifier = Modifier.fillMaxWidth()) {
            if (BuildConfig.DEBUG) {
                item {
                    TextButton(
                        modifier = Modifier.padding(vertical = 16.dp),
                        onClick = { onAction(DebugSettingsSelected) }
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
                    onItemSelected = { onAction(TemperatureSelected(it)) }
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
                    onCheckChanged = { onAction(MeasuringUnitChanged(measuringUnit.unit, it)) }
                ) {
                    Text(stringResource(measuringUnit.unit.longDescription))
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsViewPreview() {
    RecipeTheme {
        SettingsView(SettingsViewState())
    }
}

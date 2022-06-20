package com.roxana.recipeapp.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.settings.ui.SettingsView
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

@Composable
fun SettingsDestination(
    settingsViewModel: SettingsViewModel,
    onNavBack: () -> Unit = {},
    onNavDebugSettings: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(settingsViewModel.state)
        .collectAsState(SettingsViewState())

    val scaffoldState = rememberScaffoldState()

    SettingsScreen(
        state,
        scaffoldState,
        onBack = onNavBack,
        onDebugSettingsClicked = onNavDebugSettings,
        onTemperatureSelected = settingsViewModel::onTemperatureSelected,
        onMeasuringUnitChanged = settingsViewModel::onMeasuringUnitChanged
    )
}

@Composable
fun SettingsScreen(
    state: SettingsViewState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onBack: () -> Unit = {},
    onDebugSettingsClicked: () -> Unit = {},
    onTemperatureSelected: (UiTemperature) -> Unit = {},
    onMeasuringUnitChanged: (UiQuantityType, Boolean) -> Unit = { _, _ -> }
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(title = stringResource(R.string.home_title), onIconClick = onBack) }
    ) { contentPadding ->
        SettingsView(
            state,
            modifier = Modifier.padding(contentPadding),
            onDebugSettingsClicked = onDebugSettingsClicked,
            onTemperatureSelected = onTemperatureSelected,
            onMeasuringUnitChanged = onMeasuringUnitChanged
        )
    }
}

@Preview
@Composable
fun SettingsViewPreview() {
    RecipeTheme {
        SettingsScreen(SettingsViewState())
    }
}

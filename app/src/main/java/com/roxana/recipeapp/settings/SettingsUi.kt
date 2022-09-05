package com.roxana.recipeapp.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.settings.ui.SettingsView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

@Composable
fun SettingsDestination(
    settingsViewModel: SettingsViewModel,
    onNavBack: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(settingsViewModel.state)
        .collectAsState(SettingsViewState())

    SettingsScreen(
        state,
        onBack = onNavBack,
        onTemperatureSelected = settingsViewModel::onTemperatureSelected,
        onMeasuringUnitChanged = settingsViewModel::onMeasuringUnitChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsViewState,
    onBack: () -> Unit = {},
    onTemperatureSelected: (UiTemperature) -> Unit = {},
    onMeasuringUnitChanged: (UiQuantityType, Boolean) -> Unit = { _, _ -> }
) {
    Scaffold(
        topBar = { AppBarBack(title = stringResource(R.string.home_title), onIconClick = onBack) }
    ) { contentPadding ->
        SettingsView(
            state,
            modifier = Modifier.padding(contentPadding),
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

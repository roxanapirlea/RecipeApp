package com.roxana.recipeapp.settings.debug

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.settings.debug.ui.DebugSettingsView
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun DebugSettingsDestination(
    debugSettingsViewModel: DebugSettingsViewModel,
    onBack: () -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(debugSettingsViewModel.state)
        .collectAsState(DebugSettingsViewState())

    DebugSettingsScreen(
        state,
        onBack = onBack,
        onSetOnEditRecipeOnboarding = debugSettingsViewModel::onSetAddRecipeOnboarding
    )
}

@Composable
fun DebugSettingsScreen(
    state: DebugSettingsViewState,
    onBack: () -> Unit = {},
    onSetOnEditRecipeOnboarding: (Boolean) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(title = stringResource(R.string.debug_settings), onIconClick = onBack)
        }
    ) { contentPadding ->
        DebugSettingsView(
            state,
            modifier = Modifier.padding(contentPadding),
            onSetOnEditRecipeOnboarding = onSetOnEditRecipeOnboarding
        )
    }
}

@Preview
@Composable
fun DebugSettingsViewPreview() {
    RecipeTheme {
        DebugSettingsScreen(DebugSettingsViewState(true))
    }
}

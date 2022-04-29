package com.roxana.recipeapp.settings.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun DebugSettingsScreen(
    debugSettingsViewModel: DebugSettingsViewModel,
    onBack: () -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(debugSettingsViewModel.state)
        .collectAsState(DebugSettingsViewState())

    DebugSettingsView(state) { action ->
        when (action) {
            Back -> onBack()
            is SetOnAddRecipeOnboarding ->
                debugSettingsViewModel.onSetAddRecipeOnboarding(action.isDone)
        }
    }
}

@Composable
fun DebugSettingsView(
    state: DebugSettingsViewState,
    onAction: (DebugSettingsViewAction) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                title = stringResource(R.string.debug_settings),
                icon = R.drawable.ic_arrow_back
            ) { onAction(Back) }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LabelView(
                stringResource(R.string.debug_settings_onboarding),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.debug_settings_onboarding_add_recipe))
                Switch(
                    checked = state.isAddRecipeOnboardingDone,
                    onCheckedChange = { onAction(SetOnAddRecipeOnboarding(it)) }
                )
            }
        }
    }
}

@Preview
@Composable
fun DebugSettingsViewPreview() {
    RecipeTheme {
        DebugSettingsView(DebugSettingsViewState(true))
    }
}

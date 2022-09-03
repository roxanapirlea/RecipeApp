package com.roxana.recipeapp.settings.debug.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.settings.debug.DebugSettingsViewState
import com.roxana.recipeapp.ui.basecomponents.Label
import com.roxana.recipeapp.ui.basecomponents.SwitchText

@Composable
fun DebugSettingsView(
    state: DebugSettingsViewState,
    modifier: Modifier = Modifier,
    onSetOnEditRecipeOnboarding: (Boolean) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Label(
            stringResource(R.string.debug_settings_onboarding),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        SwitchText(
            checked = state.isAddRecipeOnboardingDone,
            onCheckedChange = { onSetOnEditRecipeOnboarding(it) },
            text = stringResource(R.string.debug_settings_onboarding_add_recipe)
        )
    }
}

package com.roxana.recipeapp.settings.debug.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.settings.debug.DebugSettingsViewState
import com.roxana.recipeapp.ui.LabelView

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
                onCheckedChange = { onSetOnEditRecipeOnboarding(it) }
            )
        }
    }
}

package com.roxana.recipeapp.edit.temperature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.ForwardIcon
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.RecipeTextField
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun EditRecipeTemperatureScreen(
    editRecipeTemperatureViewModel: EditRecipeTemperatureViewModel,
    onBack: () -> Unit = {},
    onForwardCreationMode: () -> Unit = {},
    onForwardEditingMode: () -> Unit = {},
    onNavigate: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(editRecipeTemperatureViewModel.state)
        .collectAsState(EditRecipeTemperatureViewState())

    AddRecipeTemperatureView(
        state,
        editRecipeTemperatureViewModel.sideEffectFlow,
        onTemperatureChanged = editRecipeTemperatureViewModel::onTemperatureChanged,
        onValidate = editRecipeTemperatureViewModel::onValidate,
        onSaveAndGoBack = editRecipeTemperatureViewModel::onSaveAndBack,
        onBackNavigation = onBack,
        onForwardNavigationForCreation = onForwardCreationMode,
        onForwardNavigationForEditing = onForwardEditingMode,
        onSelectPage = editRecipeTemperatureViewModel::onSelectPage,
        onNavigateToPage = onNavigate
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecipeTemperatureView(
    state: EditRecipeTemperatureViewState,
    sideEffectsFlow: Flow<EditRecipeTemperatureSideEffect> = flow { },
    onTemperatureChanged: (String) -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onNavigateToPage: (PageType) -> Unit = {},
    onBackNavigation: () -> Unit = {},
    onForwardNavigationForCreation: () -> Unit = {},
    onForwardNavigationForEditing: () -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(sideEffectsFlow) {
        sideEffectsFlow.collect {
            when (it) {
                ForwardForCreation -> onForwardNavigationForCreation()
                ForwardForEditing -> onForwardNavigationForEditing()
                Back -> onBackNavigation()
                is NavigateToPage -> onNavigateToPage(it.page)
            }
        }
    }

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Temperature,
        backIcon = Icons.Default.ArrowBack,
        backContentDescription = stringResource(R.string.all_back),
        onSelectPage = onSelectPage,
        onBack = onSaveAndGoBack,
    ) {
        Box(Modifier.fillMaxSize()) {
            if (state.isValid())
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onClick = onValidate
                ) { ForwardIcon() }

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
    }
}

@Preview
@Composable
fun AddRecipeTemperatureViewPreview() {
    RecipeTheme {
        AddRecipeTemperatureView(EditRecipeTemperatureViewState())
    }
}
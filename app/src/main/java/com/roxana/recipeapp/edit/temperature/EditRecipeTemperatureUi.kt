package com.roxana.recipeapp.edit.temperature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.temperature.ui.EditRecipeTemperatureView
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeTemperatureDestination(
    editRecipeTemperatureViewModel: EditRecipeTemperatureViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    val state by rememberFlowWithLifecycle(editRecipeTemperatureViewModel.state)
        .collectAsState(EditRecipeTemperatureViewState())

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Back -> onNavBack()
                is Navigation.ToPage -> onNavToPage(navigation.page, navigation.isExistingRecipe)
            }
            editRecipeTemperatureViewModel.onNavigationDone()
        }
    }

    EditRecipeTemperatureScreen(
        state,
        onTemperatureChanged = editRecipeTemperatureViewModel::onTemperatureChanged,
        onValidate = editRecipeTemperatureViewModel::onValidate,
        onBack = editRecipeTemperatureViewModel::onBack,
        onSelectPage = editRecipeTemperatureViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeTemperatureScreen(
    state: EditRecipeTemperatureViewState,
    onTemperatureChanged: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Temperature,
        onSelectPage = onSelectPage,
        onNavIcon = onBack
    ) {
        Box(Modifier.fillMaxSize()) {
            EditRecipeTemperatureView(
                state = state,
                focusRequester = focusRequester,
                onTemperatureChanged = onTemperatureChanged,
                onValidate = onValidate
            )

            if (state.isValid())
                FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)
        }
    }
}

@Preview
@Composable
fun EditRecipeTemperatureViewPreview() {
    RecipeTheme {
        EditRecipeTemperatureScreen(EditRecipeTemperatureViewState())
    }
}

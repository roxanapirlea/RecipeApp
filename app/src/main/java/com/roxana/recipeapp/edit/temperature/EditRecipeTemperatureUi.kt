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
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.SaveCreationDialog
import com.roxana.recipeapp.edit.temperature.ui.EditRecipeTemperatureView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeTemperatureDestination(
    editRecipeTemperatureViewModel: EditRecipeTemperatureViewModel,
    onNavFinish: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(editRecipeTemperatureViewModel.state)
        .collectAsState(EditRecipeTemperatureViewState())

    LaunchedEffect(editRecipeTemperatureViewModel.sideEffectFlow) {
        editRecipeTemperatureViewModel.sideEffectFlow.collect {
            when (it) {
                ForwardForCreation -> onCreationNavForward()
                ForwardForEditing -> onEditNavForward()
                Close -> onNavFinish()
                is NavigateToPage -> onNavToPage(it.page)
            }
        }
    }

    EditRecipeTemperatureScreen(
        state,
        onTemperatureChanged = editRecipeTemperatureViewModel::onTemperatureChanged,
        onValidate = editRecipeTemperatureViewModel::onValidate,
        onClose = editRecipeTemperatureViewModel::onCheckShouldClose,
        onResetAndClose = editRecipeTemperatureViewModel::onResetAndClose,
        onSaveAndClose = editRecipeTemperatureViewModel::onSaveAndClose,
        onDismissDialog = editRecipeTemperatureViewModel::onDismissDialog,
        onSelectPage = editRecipeTemperatureViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeTemperatureScreen(
    state: EditRecipeTemperatureViewState,
    onTemperatureChanged: (String) -> Unit = {},
    onClose: () -> Unit = {},
    onResetAndClose: () -> Unit = {},
    onSaveAndClose: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
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
        onClose = onClose
    ) {
        Box(Modifier.fillMaxSize()) {
            if (state.showSaveDialog)
                SaveCreationDialog(
                    onSave = onSaveAndClose,
                    onDelete = onResetAndClose,
                    onDismiss = onDismissDialog
                )

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

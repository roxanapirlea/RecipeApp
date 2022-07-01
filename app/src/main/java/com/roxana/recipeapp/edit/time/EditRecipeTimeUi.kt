package com.roxana.recipeapp.edit.time

import androidx.activity.compose.BackHandler
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
import com.roxana.recipeapp.edit.time.ui.EditRecipeTimeView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeTimeDestination(
    editRecipeTimeViewModel: EditRecipeTimeViewModel,
    onNavFinish: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(editRecipeTimeViewModel.state)
        .collectAsState(EditRecipeTimeViewState())

    LaunchedEffect(editRecipeTimeViewModel.sideEffectFlow) {
        editRecipeTimeViewModel.sideEffectFlow.collect {
            when (it) {
                ForwardForCreation -> onCreationNavForward()
                ForwardForEditing -> onEditNavForward()
                Close -> onNavFinish()
                is NavigateToPage -> onNavToPage(it.page)
            }
        }
    }

    EditRecipeTimeScreen(
        state,
        onCookingChange = editRecipeTimeViewModel::onCookingChanged,
        onPreparationChange = editRecipeTimeViewModel::onPreparationCookingChanged,
        onWaitingChange = editRecipeTimeViewModel::onWaitingChanged,
        onTotalChange = editRecipeTimeViewModel::onTotalChanged,
        onComputeTotal = editRecipeTimeViewModel::onComputeTotal,
        onValidate = editRecipeTimeViewModel::onValidate,
        onClose = editRecipeTimeViewModel::onCheckShouldClose,
        onResetAndClose = editRecipeTimeViewModel::onResetAndClose,
        onSaveAndClose = editRecipeTimeViewModel::onSaveAndClose,
        onDismissDialog = editRecipeTimeViewModel::onDismissDialog,
        onSelectPage = editRecipeTimeViewModel::onSelectPage,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeTimeScreen(
    state: EditRecipeTimeViewState,
    onCookingChange: (String) -> Unit = {},
    onPreparationChange: (String) -> Unit = {},
    onWaitingChange: (String) -> Unit = {},
    onTotalChange: (String) -> Unit = {},
    onComputeTotal: () -> Unit = {},
    onClose: () -> Unit = {},
    onResetAndClose: () -> Unit = {},
    onSaveAndClose: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val cookingFocusRequester = remember { FocusRequester() }
    val preparationFocusRequester = remember { FocusRequester() }
    val waitingFocusRequester = remember { FocusRequester() }
    val totalFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        cookingFocusRequester.requestFocus()
    }

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Time,
        onSelectPage = onSelectPage,
        onClose = onClose,
    ) {
        Box(Modifier.fillMaxSize()) {
            BackHandler(onBack = onClose)

            if (state.showSaveDialog)
                SaveCreationDialog(
                    onSave = onSaveAndClose,
                    onDelete = onResetAndClose,
                    onDismiss = onDismissDialog
                )

            EditRecipeTimeView(
                state = state,
                preparationFocusRequester = preparationFocusRequester,
                cookingFocusRequester = cookingFocusRequester,
                waitingFocusRequester = waitingFocusRequester,
                totalFocusRequester = totalFocusRequester,
                onCookingChange = onCookingChange,
                onPreparationChange = onPreparationChange,
                onWaitingChange = onWaitingChange,
                onTotalChange = onTotalChange,
                onComputeTotal = onComputeTotal,
                onValidate = onValidate
            )

            if (state.isValid())
                FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)
        }
    }
}

@Preview
@Composable
fun EditRecipeTimeViewPreview() {
    RecipeTheme {
        EditRecipeTimeScreen(EditRecipeTimeViewState())
    }
}
package com.roxana.recipeapp.edit.instructions

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
import com.roxana.recipeapp.edit.instructions.ui.EditRecipeInstructionsView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeInstructionsDestination(
    instructionsViewModel: EditRecipeInstructionsViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(instructionsViewModel.state)
        .collectAsState(EditRecipeInstructionsViewState())

    LaunchedEffect(instructionsViewModel.sideEffectFlow) {
        instructionsViewModel.sideEffectFlow.collect {
            when (it) {
                ForwardForCreation -> onCreationNavForward()
                ForwardForEditing -> onEditNavForward()
                Back -> onNavBack()
                is NavigateToPage -> onNavToPage(it.page)
            }
        }
    }

    EditRecipeInstructionsScreen(
        state,
        onInstructionChanged = instructionsViewModel::onInstructionChanged,
        onSaveInstruction = instructionsViewModel::onSaveInstruction,
        onInstructionDone = instructionsViewModel::onInstructionDone,
        onDelete = instructionsViewModel::onDeleteInstruction,
        onValidate = instructionsViewModel::onValidate,
        onSaveAndGoBack = instructionsViewModel::onSaveAndBack,
        onSelectPage = instructionsViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeInstructionsScreen(
    state: EditRecipeInstructionsViewState,
    onInstructionChanged: (String) -> Unit = {},
    onInstructionDone: () -> Unit = {},
    onSaveInstruction: () -> Unit = {},
    onDelete: (Int) -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Instructions,
        onSelectPage = onSelectPage,
        onBack = onSaveAndGoBack
    ) {
        Box(Modifier.fillMaxSize()) {
            FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)

            EditRecipeInstructionsView(
                state = state,
                focusRequester = focusRequester,
                onInstructionChanged = onInstructionChanged,
                onInstructionDone = onInstructionDone,
                onSaveInstruction = onSaveInstruction,
                onDelete = onDelete
            )
        }
    }
}

@Preview
@Composable
fun EditRecipeInstructionsViewPreview() {
    RecipeTheme {
        EditRecipeInstructionsScreen(EditRecipeInstructionsViewState())
    }
}

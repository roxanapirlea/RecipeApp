package com.roxana.recipeapp.edit.instructions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.FabSave
import com.roxana.recipeapp.edit.PageProgress
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.instructions.ui.EditRecipeInstructionsView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeInstructionsDestination(
    instructionsViewModel: EditRecipeInstructionsViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    val state by rememberFlowWithLifecycle(instructionsViewModel.state)
        .collectAsState(EditRecipeInstructionsViewState())

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Back -> onNavBack()
                is Navigation.ToPage -> onNavToPage(navigation.page, navigation.isExistingRecipe)
            }
            instructionsViewModel.onNavigationDone()
        }
    }

    EditRecipeInstructionsScreen(
        state,
        onInstructionChanged = instructionsViewModel::onInstructionChanged,
        onSaveInstruction = instructionsViewModel::onSaveInstruction,
        onInstructionDone = instructionsViewModel::onInstructionDone,
        onDelete = instructionsViewModel::onDeleteInstruction,
        onValidate = instructionsViewModel::onValidate,
        onBack = instructionsViewModel::onBack,
        onSelectPage = instructionsViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeInstructionsScreen(
    state: EditRecipeInstructionsViewState,
    onInstructionChanged: (String) -> Unit = {},
    onInstructionDone: () -> Unit = {},
    onSaveInstruction: () -> Unit = {},
    onDelete: (Int) -> Unit = {},
    onBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            AppBarBack(
                title = if (state.isExistingRecipe)
                    stringResource(R.string.edit_title_existing_recipe)
                else
                    stringResource(R.string.edit_title_new_recipe),
                onIconClick = onBack
            )
        },
        floatingActionButton = {
            if (state.editingInstruction.isBlank())
                FabForward(onClick = onValidate)
            else
                FabSave(onClick = onSaveInstruction)
        }
    ) { contentPadding ->
        Column(Modifier.fillMaxSize().padding(contentPadding)) {
            PageProgress(
                recipeAlreadyExists = state.isExistingRecipe,
                selected = PageType.Instructions,
                onSelectPage = onSelectPage
            )
            EditRecipeInstructionsView(
                state = state,
                focusRequester = focusRequester,
                onInstructionChanged = onInstructionChanged,
                onInstructionDone = onInstructionDone,
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

package com.roxana.recipeapp.edit.title

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
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
import com.roxana.recipeapp.edit.SaveCreationDialog
import com.roxana.recipeapp.edit.title.ui.EditRecipeTitleView
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeTitleDestination(
    editRecipeTitleViewModel: EditRecipeTitleViewModel,
    onNavFinish: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(editRecipeTitleViewModel.state)
        .collectAsState(EditRecipeTitleViewState())

    val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Close -> onNavFinish()
                is Navigation.ToPage -> onNavToPage(navigation.page)
            }
            editRecipeTitleViewModel.onNavigationDone()
        }
    }
    if (state.shouldRevealBackdrop) {
        LaunchedEffect(state.shouldRevealBackdrop) {
            delay(500)
            backdropState.reveal()
            delay(500)
            backdropState.conceal()
            editRecipeTitleViewModel.onBackdropRevealed()
        }
    }

    EditRecipeTitleScreen(
        state,
        backdropState,
        onTitleChanged = editRecipeTitleViewModel::onTitleChanged,
        onValidate = editRecipeTitleViewModel::onValidate,
        onClose = editRecipeTitleViewModel::onCheckShouldClose,
        onResetAndClose = editRecipeTitleViewModel::onResetAndClose,
        onSaveAndClose = editRecipeTitleViewModel::onSaveAndClose,
        onDismissDialog = editRecipeTitleViewModel::onDismissDialog,
        onSelectPage = editRecipeTitleViewModel::onSelectPage,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeTitleScreen(
    state: EditRecipeTitleViewState,
    backdropState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    onTitleChanged: (String) -> Unit = {},
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
        selectedPage = PageType.Title,
        onSelectPage = onSelectPage,
        onClose = onClose,
        scaffoldState = backdropState
    ) {
        Box(Modifier.fillMaxSize()) {
            BackHandler(onBack = onClose)

            if (state.showSaveDialog)
                SaveCreationDialog(
                    onSave = onSaveAndClose,
                    onDelete = onResetAndClose,
                    onDismiss = onDismissDialog
                )

            EditRecipeTitleView(
                state = state,
                focusRequester = focusRequester,
                onTitleChanged = onTitleChanged,
                onValidate = onValidate
            )

            if (state.isValid())
                FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun EditRecipeTitleScreenPreview() {
    RecipeTheme {
        EditRecipeTitleScreen(EditRecipeTitleViewState())
    }
}

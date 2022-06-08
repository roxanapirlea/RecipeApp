package com.roxana.recipeapp.edit.title

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
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.SaveCreationDialog
import com.roxana.recipeapp.edit.title.ui.EditRecipeTitleView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeTitleDestination(
    editRecipeTitleViewModel: EditRecipeTitleViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(editRecipeTitleViewModel.state)
        .collectAsState(EditRecipeTitleViewState())

    val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)

    LaunchedEffect(editRecipeTitleViewModel.sideEffectFlow) {
        editRecipeTitleViewModel.sideEffectFlow.collect {
            when (it) {
                ForwardForCreation -> onCreationNavForward()
                ForwardForEditing -> onEditNavForward()
                Back -> onNavBack()
                is NavigateToPage -> onNavToPage(it.page)
                RevealBackdrop -> {
                    delay(500)
                    backdropState.reveal()
                    delay(500)
                    backdropState.conceal()
                }
            }
        }
    }

    EditRecipeTitleScreen(
        state,
        backdropState,
        onTitleChanged = editRecipeTitleViewModel::onTitleChanged,
        onValidate = editRecipeTitleViewModel::onValidate,
        onCheckBack = editRecipeTitleViewModel::onCheckBack,
        onResetAndGoBack = editRecipeTitleViewModel::onReset,
        onSaveAndGoBack = editRecipeTitleViewModel::onSaveAndBack,
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
    onCheckBack: () -> Unit = {},
    onResetAndGoBack: () -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
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
        isNavCloseIcon = true,
        onSelectPage = onSelectPage,
        onBack = onCheckBack,
        scaffoldState = backdropState
    ) {
        Box(Modifier.fillMaxSize()) {
            if (state.showSaveDialog)
                SaveCreationDialog(
                    onSave = onSaveAndGoBack,
                    onDelete = onResetAndGoBack,
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

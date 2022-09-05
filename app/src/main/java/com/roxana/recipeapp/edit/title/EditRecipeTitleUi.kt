package com.roxana.recipeapp.edit.title

import androidx.compose.foundation.layout.Box
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
import com.roxana.recipeapp.edit.PageProgress
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.SaveCreationDialog
import com.roxana.recipeapp.edit.title.ui.EditRecipeTitleView
import com.roxana.recipeapp.ui.basecomponents.AppBarClose
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeTitleDestination(
    editRecipeTitleViewModel: EditRecipeTitleViewModel,
    onNavFinish: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    val state by rememberFlowWithLifecycle(editRecipeTitleViewModel.state)
        .collectAsState(EditRecipeTitleViewState())

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Close -> onNavFinish()
                is Navigation.ToPage -> onNavToPage(navigation.page, navigation.isExistingRecipe)
            }
            editRecipeTitleViewModel.onNavigationDone()
        }
    }

    EditRecipeTitleScreen(
        state,
        onTitleChanged = editRecipeTitleViewModel::onTitleChanged,
        onValidate = editRecipeTitleViewModel::onValidate,
        onClose = editRecipeTitleViewModel::onCheckShouldClose,
        onResetAndClose = editRecipeTitleViewModel::onResetAndClose,
        onSaveAndClose = editRecipeTitleViewModel::onSaveAndClose,
        onDismissDialog = editRecipeTitleViewModel::onDismissDialog,
        onSelectPage = editRecipeTitleViewModel::onSelectPage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeTitleScreen(
    state: EditRecipeTitleViewState,
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

    Scaffold(
        topBar = {
            AppBarClose(
                title = if (state.isExistingRecipe)
                    stringResource(R.string.edit_title_existing_recipe)
                else
                    stringResource(R.string.edit_title_new_recipe),
                onIconClick = onClose
            )
        },
        floatingActionButton = {
            if (state.isValid()) FabForward(onClick = onValidate)
        }
    ) { contentPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            if (state.showSaveDialog)
                SaveCreationDialog(
                    onSave = onSaveAndClose,
                    onDelete = onResetAndClose,
                    onDismiss = onDismissDialog
                )
            Column {
                PageProgress(
                    recipeAlreadyExists = state.isExistingRecipe,
                    selected = PageType.Title,
                    onSelectPage = onSelectPage
                )
                EditRecipeTitleView(
                    state = state,
                    focusRequester = focusRequester,
                    onTitleChanged = onTitleChanged,
                    onValidate = onValidate
                )
            }
        }
    }
}

@Preview
@Composable
fun EditRecipeTitleScreenPreview() {
    RecipeTheme {
        EditRecipeTitleScreen(EditRecipeTitleViewState())
    }
}

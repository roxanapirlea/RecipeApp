package com.roxana.recipeapp.edit.comments

import androidx.compose.foundation.layout.Box
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
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.comments.ui.EditRecipeCommentsView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeCommentsDestination(
    commentsViewModel: EditRecipeCommentsViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    val state by rememberFlowWithLifecycle(commentsViewModel.state)
        .collectAsState(EditRecipeCommentsViewState())

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Back -> onNavBack()
                is Navigation.ToPage -> onNavToPage(navigation.page, navigation.isExistingRecipe)
            }
            commentsViewModel.onNavigationDone()
        }
    }

    EditRecipeCommentsScreen(
        state,
        onCommentChanged = commentsViewModel::onCommentChanged,
        onSaveComment = commentsViewModel::onSaveComment,
        onCommentDone = commentsViewModel::onCommentDone,
        onDelete = commentsViewModel::onDeleteComment,
        onValidate = commentsViewModel::onValidate,
        onBack = commentsViewModel::onBack,
        onSelectPage = commentsViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeCommentsScreen(
    state: EditRecipeCommentsViewState,
    onCommentChanged: (String) -> Unit = {},
    onCommentDone: () -> Unit = {},
    onSaveComment: () -> Unit = {},
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
            if (state.editingComment.isBlank())
                FabForward(onClick = onValidate)
            else
                FabSave(onClick = onSaveComment)
        }
    ) { contentPadding ->
        Box(Modifier.fillMaxSize().padding(contentPadding)) {
            EditRecipeCommentsView(
                state = state,
                focusRequester = focusRequester,
                onCommentChanged = onCommentChanged,
                onCommentDone = onCommentDone,
                onDelete = onDelete
            )
        }
    }
}

@Preview
@Composable
fun EditRecipeInstructionsViewPreview() {
    RecipeTheme {
        EditRecipeCommentsScreen(EditRecipeCommentsViewState())
    }
}

package com.roxana.recipeapp.edit.comments

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
import com.roxana.recipeapp.edit.comments.ui.EditRecipeCommentsView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeCommentsDestination(
    commentsViewModel: EditRecipeCommentsViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(commentsViewModel.state)
        .collectAsState(EditRecipeCommentsViewState())

    LaunchedEffect(commentsViewModel.sideEffectFlow) {
        commentsViewModel.sideEffectFlow.collect {
            when (it) {
                ForwardForCreation -> onCreationNavForward()
                ForwardForEditing -> onEditNavForward()
                Back -> onNavBack()
                is NavigateToPage -> onNavToPage(it.page)
            }
        }
    }

    EditRecipeCommentsScreen(
        state,
        onCommentChanged = commentsViewModel::onCommentChanged,
        onSaveComment = commentsViewModel::onSaveComment,
        onCommentDone = commentsViewModel::onCommentDone,
        onDelete = commentsViewModel::onDeleteComment,
        onValidate = commentsViewModel::onValidate,
        onSaveAndGoBack = commentsViewModel::onSaveAndBack,
        onSelectPage = commentsViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeCommentsScreen(
    state: EditRecipeCommentsViewState,
    onCommentChanged: (String) -> Unit = {},
    onCommentDone: () -> Unit = {},
    onSaveComment: () -> Unit = {},
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
        selectedPage = PageType.Comments,
        onSelectPage = onSelectPage,
        onBack = onSaveAndGoBack
    ) {
        Box(Modifier.fillMaxSize()) {
            FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)

            EditRecipeCommentsView(
                state = state,
                focusRequester = focusRequester,
                onCommentChanged = onCommentChanged,
                onCommentDone = onCommentDone,
                onSaveComment = onSaveComment,
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

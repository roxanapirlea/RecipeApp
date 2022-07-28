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
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.FabSave
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.comments.ui.EditRecipeCommentsView
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

@OptIn(ExperimentalMaterialApi::class)
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

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Comments,
        onSelectPage = onSelectPage,
        onNavIcon = onBack
    ) {
        Box(Modifier.fillMaxSize()) {
            EditRecipeCommentsView(
                state = state,
                focusRequester = focusRequester,
                onCommentChanged = onCommentChanged,
                onCommentDone = onCommentDone,
                onSaveComment = onSaveComment,
                onDelete = onDelete
            )

            if (state.editingComment.isBlank())
                FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)
            else
                FabSave(modifier = Modifier.align(Alignment.BottomEnd), onSaveComment)
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

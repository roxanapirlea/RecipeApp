package com.roxana.recipeapp.comment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.roxana.recipeapp.comment.ui.AddCommentView
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle

@Composable
fun AddCommentDestination(
    addCommentViewModel: AddCommentViewModel,
    onNavBack: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(addCommentViewModel.state)
        .collectAsState(AddCommentState())

    if (state.isValidated) {
        LaunchedEffect(state.isValidated) {
            onNavBack()
            addCommentViewModel.onValidateDone()
        }
    }

    AddCommentView(
        state,
        onChangeComment = addCommentViewModel::onChangeComment,
        onSaveComment = addCommentViewModel::onSaveComment
    )
}

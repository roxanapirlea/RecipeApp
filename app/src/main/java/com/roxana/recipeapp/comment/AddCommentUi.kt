package com.roxana.recipeapp.comment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.roxana.recipeapp.comment.ui.AddCommentView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle

@Composable
fun AddCommentDestination(
    addCommentViewModel: AddCommentViewModel,
    onNavBack: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(addCommentViewModel.state)
        .collectAsState(AddCommentState())

    LaunchedEffect(addCommentViewModel.sideEffectFlow) {
        addCommentViewModel.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                SaveSuccess -> onNavBack()
            }
        }
    }

    AddCommentView(
        state,
        onChangeComment = addCommentViewModel::onChangeComment,
        onSaveComment = addCommentViewModel::onSaveComment
    )
}

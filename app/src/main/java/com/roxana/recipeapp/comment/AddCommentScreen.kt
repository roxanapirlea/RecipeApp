package com.roxana.recipeapp.comment

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle

@Composable
fun AddCommentScreen(
    addCommentViewModel: AddCommentViewModel,
    onBack: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(addCommentViewModel.state)
        .collectAsState("Hello")

    Text(state)
}
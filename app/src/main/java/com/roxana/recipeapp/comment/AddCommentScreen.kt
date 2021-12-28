package com.roxana.recipeapp.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.FlatSecondaryButton
import com.roxana.recipeapp.ui.unlinedTextFiledColors

@Composable
fun AddCommentScreen(
    addCommentViewModel: AddCommentViewModel,
    onBack: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(addCommentViewModel.state)
        .collectAsState(AddCommentState())

    LaunchedEffect(addCommentViewModel.sideEffectFlow) {
        addCommentViewModel.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                SaveSuccess -> onBack()
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = state.comment,
            onValueChange = addCommentViewModel::onChangeComment,
            placeholder = { Text(stringResource(id = R.string.add_comment_placeholder)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            colors = unlinedTextFiledColors(),
            shape = RectangleShape,
            keyboardActions = KeyboardActions { addCommentViewModel.onSaveComment() },
            modifier = Modifier.fillMaxWidth()
        )
        FlatSecondaryButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            onClick = addCommentViewModel::onSaveComment
        ) { Text(stringResource(R.string.all_save)) }
    }
}

package com.roxana.recipeapp.comment.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.comment.AddCommentState
import com.roxana.recipeapp.ui.FlatSecondaryButton
import com.roxana.recipeapp.ui.textfield.RecipePrimaryTextField

@Composable
fun AddCommentView(
    state: AddCommentState,
    onChangeComment: (String) -> Unit = {},
    onSaveComment: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        RecipePrimaryTextField(
            state.comment,
            placeholder = stringResource(id = R.string.add_comment_placeholder),
            modifier = Modifier.fillMaxWidth(),
            onImeAction = onSaveComment,
            onValueChange = onChangeComment,
        )
        FlatSecondaryButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            onClick = onSaveComment,
            isEnabled = state.canSave,
        ) { Text(stringResource(R.string.all_save)) }
    }
}

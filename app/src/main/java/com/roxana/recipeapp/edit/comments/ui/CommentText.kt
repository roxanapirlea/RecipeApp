package com.roxana.recipeapp.edit.comments.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.basecomponents.Detail

@Composable
fun CommentText(
    comment: String,
    index: Int,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Detail(
            text = stringResource(R.string.detail_instruction, index, comment),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .weight(1f)
        )
        IconButton(
            onClick = onDelete,
            modifier = Modifier.padding(start = 6.dp)
        ) {
            androidx.compose.material3.Icon(
                Icons.Default.Delete,
                tint = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(R.string.all_delete),
            )
        }
    }
}

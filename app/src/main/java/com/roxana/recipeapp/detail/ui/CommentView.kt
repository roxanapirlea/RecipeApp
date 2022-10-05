package com.roxana.recipeapp.detail.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.basecomponents.Detail

@Composable
fun CommentView(
    comment: String,
    modifier: Modifier = Modifier,
    isDeletable: Boolean = false,
    onDelete: () -> Unit = {}
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Detail(
            text = comment,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .weight(1f)
        )
        AnimatedVisibility(visible = isDeletable) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.all_delete),
                )
            }
        }
    }
}

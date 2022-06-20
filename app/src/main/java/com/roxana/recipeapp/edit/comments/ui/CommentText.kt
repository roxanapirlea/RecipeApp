package com.roxana.recipeapp.edit.comments.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R

@Composable
fun CommentText(
    comment: String,
    index: Int,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$index",
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Text(
            text = comment,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground,
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .weight(3f)
        )
        Icon(
            Icons.Default.Delete,
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_delete),
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable { onDelete() }
                .padding(12.dp)
                .size(32.dp)
        )
    }
}

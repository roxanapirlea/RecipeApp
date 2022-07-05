package com.roxana.recipeapp.edit.recap.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.detail.ui.EmptyItem
import com.roxana.recipeapp.ui.SecondaryButton

@Composable
fun EmptyPhoto(
    modifier: Modifier = Modifier,
    onTakePicture: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EmptyItem(text = "No associated photo")
        SecondaryButton(onClick = onTakePicture) {
            Icon(
                painterResource(R.drawable.ic_photo),
                contentDescription = stringResource(R.string.all_take_photo)
            )
        }
    }
}

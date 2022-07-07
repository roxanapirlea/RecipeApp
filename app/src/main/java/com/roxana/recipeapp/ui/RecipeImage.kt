package com.roxana.recipeapp.ui

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.roxana.recipeapp.R

@Composable
fun RecipeImage(
    path: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier
            .size(250.dp, 250.dp)
            .clip(RoundedCornerShape(16.dp)),
        model = Uri.parse(path),
        contentDescription = stringResource(R.string.all_captured_image),
        contentScale = ContentScale.Crop
    )
}

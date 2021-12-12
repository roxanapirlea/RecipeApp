package com.roxana.recipeapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun RecipePartLabel(text: String, modifier: Modifier = Modifier, image: Int? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        image?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier.padding(12.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
        } ?: Spacer(modifier = Modifier.size(52.dp, 48.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

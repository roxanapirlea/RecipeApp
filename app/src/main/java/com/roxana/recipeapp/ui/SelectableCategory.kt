package com.roxana.recipeapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun SelectableCategory(
    categoryType: UiCategoryType,
    isSelected: Boolean,
    index: Int,
    onCategoryClicked: (UiCategoryType) -> Unit = {},
) {
    Card(
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .clip(CircleShape)
            .toggleable(isSelected) { onCategoryClicked(categoryType) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(getPrimarySecondaryColor(index), CircleShape)
            ) {
                if (isSelected)
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colors.background,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp)
                    )
            }
            Text(
                text = stringResource(categoryType.text),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

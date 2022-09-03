package com.roxana.recipeapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.basecomponents.ElevatedFilterChipTertiary
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun SelectableCategory(
    categoryType: UiCategoryType,
    isSelected: Boolean,
    onCategoryClicked: (UiCategoryType) -> Unit = {},
) {
    ElevatedFilterChipTertiary(
        onClick = { onCategoryClicked(categoryType) },
        selected = isSelected,
        label = {
            Text(
                stringResource(categoryType.text),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
        },
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Preview
@Composable
fun SelectableCategoryPreview() {
    RecipeTheme {
        SelectableCategory(categoryType = UiCategoryType.Breakfast, isSelected = false)
    }
}

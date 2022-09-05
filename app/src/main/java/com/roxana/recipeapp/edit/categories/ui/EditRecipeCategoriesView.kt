package com.roxana.recipeapp.edit.categories.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.categories.EditRecipeCategoriesViewState
import com.roxana.recipeapp.ui.SelectableCategory
import com.roxana.recipeapp.ui.basecomponents.Label
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun EditRecipeCategoriesView(
    state: EditRecipeCategoriesViewState,
    onCategoryClicked: (UiCategoryType) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(3f))
        Label(
            text = stringResource(R.string.edit_recipe_categories_label),
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Spacer(modifier = Modifier.weight(0.5f))
        FlowRow(mainAxisAlignment = FlowMainAxisAlignment.Center) {
            state.categories.forEach { categoryState ->
                SelectableCategory(
                    categoryType = categoryState.categoryType,
                    isSelected = categoryState.isSelected,
                    onCategoryClicked = onCategoryClicked
                )
            }
        }
        Spacer(modifier = Modifier.weight(6f))
    }
}

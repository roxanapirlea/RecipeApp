package com.roxana.recipeapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun CategoriesView(
    categories: List<UiCategoryType>,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    categoryModifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        LazyRow(
            horizontalArrangement = horizontalArrangement,
            modifier = modifier
        ) {
            items(categories) {
                Text(
                    stringResource(it.text),
                    style = MaterialTheme.typography.overline,
                    modifier = categoryModifier
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun CategoriesViewPreviewLight() {
    RecipeTheme {
        CategoriesView(
            categories = listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert),
            horizontalArrangement = Arrangement.Center,
            categoryModifier = Modifier.padding(horizontal = 8.dp),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun CategoriesViewPreviewDark() {
    RecipeTheme {
        CategoriesView(
            categories = listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert),
            horizontalArrangement = Arrangement.Center,
            categoryModifier = Modifier.padding(horizontal = 8.dp),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        )
    }
}

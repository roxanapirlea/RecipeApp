package com.roxana.recipeapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun CategoriesView(
    categories: List<UiCategoryType>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current.applicationContext
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.clearAndSetSemantics {
            val categoriesText = categories.joinToString(",") { context.getString(it.text) }
            contentDescription = context.getString(R.string.all_in_categories, categoriesText)
        }
    ) {
        items(categories, key = { it.text }) {
            OutlinedCard(
                colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
            ) {
                Text(
                    stringResource(it.text),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
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
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        )
    }
}

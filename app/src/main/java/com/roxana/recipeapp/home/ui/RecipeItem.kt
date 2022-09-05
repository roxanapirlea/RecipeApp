package com.roxana.recipeapp.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.roxana.recipeapp.home.RecipeState
import com.roxana.recipeapp.ui.CategoriesView
import com.roxana.recipeapp.ui.basecomponents.ElevatedCardEndImage
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun RecipeItem(
    modifier: Modifier = Modifier,
    recipeState: RecipeState = RecipeState(),
    onClick: (Int) -> Unit = {}
) {
    ElevatedCardEndImage(
        modifier = modifier.clickable { onClick(recipeState.id) },
        endImage =
        recipeState.photoPath?.let {
            {
                AsyncImage(
                    model = recipeState.photoPath,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(MaterialTheme.colorScheme.primary),
                )
            }
        }
    ) {
        Column {
            Text(
                text = recipeState.name,
                style = MaterialTheme.typography.titleMedium,
            )
            if (recipeState.categories.isNotEmpty())
                CategoriesView(
                    categories = recipeState.categories,
                    modifier = Modifier.padding(top = 16.dp)
                )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun RecipeSummaryPreviewLight() {
    RecipeTheme {
        RecipeItem(
            recipeState = RecipeState(
                1,
                "Crepes",
                null,
                listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert)
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun RecipeSummaryPreviewDark() {
    RecipeTheme {
        RecipeItem(
            recipeState = RecipeState(
                1,
                "Crepes",
                null,
                listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert)
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

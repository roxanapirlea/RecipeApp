package com.roxana.recipeapp.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.home.RecipeState
import com.roxana.recipeapp.ui.RoundedStartShape
import com.roxana.recipeapp.ui.getIntermediateColors
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType

private const val IMAGE_COLOR_COUNT = 5

@Composable
fun RecipeItem(
    modifier: Modifier = Modifier,
    recipeState: RecipeState = RecipeState(),
    index: Int = 0,
    onClick: (Int) -> Unit = {}
) {
    Card(
        shape = RoundedStartShape,
        modifier = modifier
            .clip(RoundedStartShape)
            .clickable { onClick(recipeState.id) }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val colors = getIntermediateColors(
                MaterialTheme.colors.primary,
                MaterialTheme.colors.secondary,
                IMAGE_COLOR_COUNT
            ) + getIntermediateColors(
                MaterialTheme.colors.primary,
                MaterialTheme.colors.secondary,
                IMAGE_COLOR_COUNT
            ).reversed()

            Spacer(
                modifier = Modifier
                    .size(80.dp)
                    .background(colors[index % (IMAGE_COLOR_COUNT * 2)], CircleShape)
            )
            Column() {
                Text(
                    text = recipeState.name,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.primary
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        items(recipeState.categories) {
                            Text(
                                stringResource(it.text),
                                style = MaterialTheme.typography.overline
                            )
                        }
                    }
                }
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
fun RecipeSummaryPreviewLight() {
    RecipeTheme {
        RecipeItem(
            recipeState = RecipeState(
                1,
                "Crepes",
                listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert)
            ),
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
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
                listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert)
            ),
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
        )
    }
}

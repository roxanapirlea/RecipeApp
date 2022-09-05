package com.roxana.recipeapp.edit.recap.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.formatIngredient
import com.roxana.recipeapp.detail.ui.ServingsView
import com.roxana.recipeapp.detail.ui.TemperatureView
import com.roxana.recipeapp.detail.ui.TimeView
import com.roxana.recipeapp.edit.recap.IngredientState
import com.roxana.recipeapp.edit.recap.RecapViewState
import com.roxana.recipeapp.edit.recap.TimeState
import com.roxana.recipeapp.ui.CategoriesView
import com.roxana.recipeapp.ui.ConsistentHeightRow
import com.roxana.recipeapp.ui.RecipeImage
import com.roxana.recipeapp.ui.basecomponents.Detail
import com.roxana.recipeapp.ui.basecomponents.Label
import com.roxana.recipeapp.ui.basecomponents.Title
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun RecapView(
    state: RecapViewState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        state.photoPath?.let {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    RecipeImage(
                        path = it,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        item {
            CategoriesView(
                categories = state.categories,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
            )
        }
        item {
            Title(text = state.title)
        }
        if (state.photoPath == null) {
            item {
                Detail(stringResource(R.string.edit_recipe_no_photo), Modifier.padding(top = 8.dp))
            }
        }
        if (!state.time.isEmpty) {
            item {
                TimeView(
                    timeTotal = state.time.total,
                    timeCooking = state.time.cooking,
                    timePreparation = state.time.preparation,
                    timeWaiting = state.time.waiting,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
        item {
            ConsistentHeightRow(
                modifier = Modifier.padding(top = 16.dp),
            ) {
                state.temperature?.let {
                    TemperatureView(
                        it,
                        state.temperatureUnit,
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
                state.portions?.let {
                    ServingsView(
                        it,
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }
        item {
            Label(
                text = stringResource(R.string.all_ingredients),
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp),
            )
        }
        if (state.ingredients.isEmpty())
            item { Detail(stringResource(R.string.detail_ingredients_empty)) }
        else
            items(state.ingredients) {
                val ingredient = formatIngredient(it.name, it.quantity, it.quantityType)
                Detail(ingredient)
            }
        item {
            Label(
                text = stringResource(R.string.all_instructions),
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp),
            )
        }
        if (state.instructions.isEmpty())
            item { Detail(stringResource(R.string.detail_instructions_empty)) }
        else
            itemsIndexed(state.instructions) { index, item ->
                Detail(stringResource(R.string.detail_instruction, index + 1, item))
            }
        if (state.comments.isNotEmpty()) {
            item {
                Label(
                    text = stringResource(R.string.all_comments),
                    modifier = Modifier.padding(top = 32.dp, bottom = 8.dp),
                )
            }
            items(state.comments) { Detail(it) }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun DetailContentViewPreviewLight() {
    RecipeTheme {
        RecapView(
            RecapViewState(
                title = "Crepes",
                portions = 2,
                categories = listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert),
                ingredients = listOf(
                    IngredientState("Flour", 3.0, UiQuantityType.Tablespoon),
                    IngredientState("Eggs", 1.0, UiQuantityType.None)
                ),
                instructions = listOf("Mix everything together", "Cook in a pan"),
                comments = listOf("Put oil in the pan", "Excellent with chocolate"),
                time = TimeState(6, 3, 2, 1),
                temperature = 180
            )
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun DetailContentViewPreviewDark() {
    RecipeTheme {
        RecapView(
            RecapViewState(
                title = "Crepes",
                portions = 1,
                categories = listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert),
                ingredients = listOf(
                    IngredientState("Flour", 3.0, UiQuantityType.Tablespoon),
                    IngredientState("Eggs", 1.0, UiQuantityType.None)
                ),
                instructions = listOf("Mix everything together", "Cook in a pan"),
                comments = listOf("Put oil in the pan", "Excellent with chocolate"),
                time = TimeState(6, 3, 2, 1)
            )
        )
    }
}

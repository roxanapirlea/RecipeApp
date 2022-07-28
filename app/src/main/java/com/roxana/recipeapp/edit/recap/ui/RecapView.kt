package com.roxana.recipeapp.edit.recap.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.detail.ui.EmptyItem
import com.roxana.recipeapp.detail.ui.IngredientView
import com.roxana.recipeapp.detail.ui.InstructionView
import com.roxana.recipeapp.detail.ui.ItemDetailsView
import com.roxana.recipeapp.detail.ui.TimeView
import com.roxana.recipeapp.edit.recap.IngredientState
import com.roxana.recipeapp.edit.recap.RecapViewState
import com.roxana.recipeapp.edit.recap.TimeState
import com.roxana.recipeapp.ui.CategoriesView
import com.roxana.recipeapp.ui.CenteredTitle
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.RecipeImage
import com.roxana.recipeapp.ui.button.TwoButtonRow
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun RecapView(
    state: RecapViewState,
    modifier: Modifier = Modifier,
    onSave: () -> Unit = {},
    onEdit: () -> Unit = {},
) {
    Column(modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    categoryModifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            item {
                CenteredTitle(
                    text = state.title,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            state.portions?.let { portions ->
                item {
                    Text(
                        text = LocalContext.current.resources.getQuantityString(
                            R.plurals.detail_portions,
                            portions.toInt(),
                            portions
                        ),
                        color = MaterialTheme.colors.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }
            }
            if (state.photoPath == null) {
                item { EmptyItem(stringResource(R.string.edit_recipe_no_photo)) }
            }
            if (!state.time.isEmpty) {
                item {
                    TimeView(
                        timeTotal = state.time.total,
                        timeCooking = state.time.cooking,
                        timePreparation = state.time.preparation,
                        timeWaiting = state.time.waiting,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            state.temperature?.let {
                item {
                    val unit = state.temperatureUnit?.let { stringResource(it.text) } ?: ""
                    ItemDetailsView(
                        text = stringResource(R.string.detail_temperature, it, unit),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            item {
                LabelView(
                    text = stringResource(R.string.all_ingredients),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                )
            }
            if (state.ingredients.isEmpty())
                item { EmptyItem(stringResource(R.string.detail_ingredients_empty)) }
            else
                items(state.ingredients) { ingredient ->
                    IngredientView(
                        name = ingredient.name,
                        quantity = ingredient.quantity,
                        quantityType = ingredient.quantityType
                    )
                }
            item {
                LabelView(
                    text = stringResource(R.string.all_instructions),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 20.dp),
                )
            }
            if (state.instructions.isEmpty())
                item { EmptyItem(stringResource(R.string.detail_instructions_empty)) }
            else
                itemsIndexed(state.instructions) { index, item ->
                    InstructionView(index = index + 1, text = item)
                }
            if (state.comments.isNotEmpty()) {
                item {
                    LabelView(
                        text = stringResource(R.string.all_comments),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, top = 20.dp),
                    )
                }
                items(state.comments) { Text(it, color = MaterialTheme.colors.onBackground) }
            }
        }
        TwoButtonRow(
            textStartButton = stringResource(R.string.edit_recipe_recap_continue_editing),
            onClickStartButton = onEdit,
            textEndButton = stringResource(R.string.all_save),
            onClickEndButton = onSave
        )
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

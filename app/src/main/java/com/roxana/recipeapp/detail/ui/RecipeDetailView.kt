package com.roxana.recipeapp.detail.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.roxana.recipeapp.detail.DetailViewState
import com.roxana.recipeapp.detail.IngredientState
import com.roxana.recipeapp.detail.TimeState
import com.roxana.recipeapp.ui.CategoriesView
import com.roxana.recipeapp.ui.CenteredTitle
import com.roxana.recipeapp.ui.FlatSecondaryButton
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.TwoButtonRow
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun RecipeDetailView(
    state: DetailViewState.Content,
    modifier: Modifier = Modifier,
    onStartCookingClicked: () -> Unit = {},
    onAddCommentClicked: () -> Unit = {},
    onEditClicked: () -> Unit = {},
) {
    Box(modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
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
            item {
                FlatSecondaryButton(
                    onClick = onAddCommentClicked,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(stringResource(id = R.string.all_add_comment))
                }
            }
        }
        TwoButtonRow(
            modifier = Modifier.align(Alignment.BottomCenter),
            textStartButton = stringResource(R.string.detail_edit_recipe),
            textEndButton = stringResource(R.string.detail_start_cooking),
            onClickStartButton = onEditClicked,
            onClickEndButton = onStartCookingClicked
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
        RecipeDetailView(
            DetailViewState.Content(
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
        RecipeDetailView(
            DetailViewState.Content(
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
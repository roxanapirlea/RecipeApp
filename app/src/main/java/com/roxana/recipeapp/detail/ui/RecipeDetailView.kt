package com.roxana.recipeapp.detail.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.formatIngredient
import com.roxana.recipeapp.detail.CommentState
import com.roxana.recipeapp.detail.DetailViewState
import com.roxana.recipeapp.detail.IngredientState
import com.roxana.recipeapp.detail.TimeState
import com.roxana.recipeapp.edit.comments.ui.CommentText
import com.roxana.recipeapp.ui.ConsistentHeightRow
import com.roxana.recipeapp.ui.RecipeImageFull
import com.roxana.recipeapp.ui.basecomponents.Detail
import com.roxana.recipeapp.ui.basecomponents.Label
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun RecipeDetailView(
    state: DetailViewState,
    modifier: Modifier = Modifier,
    onAddComment: () -> Unit = {},
    onEditComments: () -> Unit = {},
    onDeleteComment: (Int) -> Unit = {},
    onDoneEditComments: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        state.photoPath?.let {
            item { RecipeImageFull(path = it) }
        }

        if (!state.time.isEmpty) {
            item {
                TimeView(
                    timeTotal = state.time.total,
                    timeCooking = state.time.cooking,
                    timePreparation = state.time.preparation,
                    timeWaiting = state.time.waiting,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
        }

        item {
            ConsistentHeightRow(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
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
                stringResource(R.string.all_ingredients),
                Modifier.padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }
        if (state.ingredients.isEmpty())
            item {
                Text(
                    stringResource(R.string.detail_ingredients_empty),
                    Modifier.padding(horizontal = 16.dp)
                )
            }
        else
            items(state.ingredients) {
                val formattedIngredient = formatIngredient(it.name, it.quantity, it.quantityType)
                Detail(
                    text = formattedIngredient,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

        item {
            Label(
                stringResource(R.string.all_instructions),
                Modifier
                    .padding(top = 32.dp, bottom = 8.dp)
                    .padding(horizontal = 16.dp)
            )
        }
        if (state.instructions.isEmpty())
            item {
                Text(
                    stringResource(R.string.detail_instructions_empty),
                    Modifier.padding(horizontal = 16.dp)
                )
            }
        else
            itemsIndexed(state.instructions) { index, item ->
                Detail(
                    text = stringResource(R.string.detail_instruction, index + 1, item),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
            ) {
                Label(
                    stringResource(R.string.all_comments),
                    Modifier.padding(horizontal = 16.dp)
                )
                if (state.commentState.isEditing)
                    FilledTonalIconButton(onClick = onDoneEditComments) {
                        Icon(
                            Icons.Rounded.Check,
                            stringResource(R.string.detail_recipe_finish_comments)
                        )
                    }
                else
                    FilledTonalIconButton(onClick = onEditComments) {
                        Icon(
                            painterResource(R.drawable.ic_edit),
                            contentDescription = stringResource(R.string.all_edit)
                        )
                    }
            }

        }
        if (state.commentState.isEditing) {
            itemsIndexed(state.commentState.comments) { index, comment ->
                CommentText(
                    comment = comment.text,
                    index = index + 1,
                    onDelete = { onDeleteComment(comment.id) })
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                AddNewButton(
                    onClick = onAddComment,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        } else {
            items(state.commentState.comments) { comment ->
                Detail(
                    text = comment.text,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
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
        RecipeDetailView(
            DetailViewState(
                title = "Crepes",
                portions = 2,
                categories = listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert),
                ingredients = listOf(
                    IngredientState("Flour", 3.0, UiQuantityType.Tablespoon),
                    IngredientState("Eggs", 1.0, UiQuantityType.None)
                ),
                instructions = listOf("Mix everything together", "Cook in a pan"),
                commentState = CommentState(),
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
            DetailViewState(
                title = "Crepes",
                portions = 1,
                categories = listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert),
                ingredients = listOf(
                    IngredientState("Flour", 3.0, UiQuantityType.Tablespoon),
                    IngredientState("Eggs", 1.0, UiQuantityType.None)
                ),
                instructions = listOf("Mix everything together", "Cook in a pan"),
                commentState = CommentState(),
                time = TimeState(6, 3, 2, 1)
            )
        )
    }
}

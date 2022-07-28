package com.roxana.recipeapp.cooking.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.cooking.CookingViewState
import com.roxana.recipeapp.cooking.TimeState
import com.roxana.recipeapp.detail.ui.EmptyItem
import com.roxana.recipeapp.ui.CenteredTitle
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.button.SecondaryButton
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun CookingInProgressView(
    state: CookingViewState,
    modifier: Modifier = Modifier,
    onVaryIngredient: () -> Unit = {},
    onAddComment: () -> Unit = {},
    onDecrementPortions: () -> Unit = {},
    onIncrementPortions: () -> Unit = {},
    onResetPortions: () -> Unit = {},
    onToggleIngredientCheck: (id: Int, isChecked: Boolean) -> Unit = { _, _ -> },
    onToggleInstructionCheck: (id: Short, isChecked: Boolean) -> Unit = { _, _ -> }
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {

        item {
            CenteredTitle(
                text = state.title,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        if (!state.time.isEmpty) {
            item {
                LabelView(
                    text = stringResource(R.string.cooking_time),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 16.dp),
                )
            }
        }
        state.time.total?.let {
            item { Text(stringResource(R.string.cooking_time_total, it)) }
        }
        state.time.cooking?.let {
            item { Text(stringResource(R.string.cooking_time_cooking, it)) }
        }
        state.time.preparation?.let {
            item { Text(stringResource(R.string.cooking_time_preparation, it)) }
        }
        state.time.waiting?.let {
            item { Text(stringResource(R.string.cooking_time_waiting, it)) }
        }
        state.temperature?.let {
            item {
                val unit = state.temperatureUnit?.let { stringResource(it.text) } ?: ""
                Text(
                    text = stringResource(R.string.detail_temperature, it, unit),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
        item {
            LabelView(
                text = stringResource(R.string.cooking_portions),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, top = 16.dp),
            )
        }
        item {
            AdjustablePortions(
                state.selectedPortions,
                onPortionsIncrease = onIncrementPortions,
                onPortionsDecrease = onDecrementPortions,
                onPortionsReset = onResetPortions
            )
        }
        item {
            SecondaryButton(onClick = onVaryIngredient) {
                Text(stringResource(R.string.cooking_edit_by_ingredient))
            }
        }
        item {
            LabelView(
                text = stringResource(R.string.all_ingredients),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, top = 16.dp),
            )
        }
        if (state.ingredients.isEmpty())
            item { EmptyItem(stringResource(R.string.detail_ingredients_empty)) }
        else
            items(state.ingredients) { ingredient ->
                IngredientView(ingredient = ingredient) {
                    onToggleIngredientCheck(ingredient.id, it)
                }
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
            items(state.instructions) { item ->
                InstructionView(
                    instruction = item.instruction,
                    isChecked = item.isChecked,
                    isCurrent = item.isCurrent,
                    onCheckChanged = { onToggleInstructionCheck(item.id, it) }
                )
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
            SecondaryButton(
                onClick = onAddComment,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(id = R.string.all_add_comment))
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
fun CookingInProgressPreviewLight() {
    RecipeTheme {
        CookingInProgressView(
            CookingViewState(
                title = "Title",
                time = TimeState(8, 4, 3, 1),
                temperature = 150
            ),
            Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun CookingInProgressPreviewDark() {
    RecipeTheme {
        CookingInProgressView(
            CookingViewState(
                title = "Title",
                time = TimeState(8, 4, 3, 1),
                temperature = 150
            ),
            Modifier.fillMaxWidth()
        )
    }
}

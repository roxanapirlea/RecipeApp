package com.roxana.recipeapp.cooking.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.cooking.CookingViewState
import com.roxana.recipeapp.cooking.TimeState
import com.roxana.recipeapp.detail.ui.TemperatureView
import com.roxana.recipeapp.detail.ui.TimeView
import com.roxana.recipeapp.ui.basecomponents.Detail
import com.roxana.recipeapp.ui.basecomponents.FilledTonalIconTextButton
import com.roxana.recipeapp.ui.basecomponents.Label
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
        modifier = modifier
    ) {
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

        state.temperature?.let {
            item {
                TemperatureView(
                    it,
                    state.temperatureUnit,
                    Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
        }

        item {
            Label(
                text = stringResource(R.string.cooking_portions),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
            )
        }
        item {
            AdjustablePortions(
                state.selectedPortions,
                onPortionsIncrease = onIncrementPortions,
                onPortionsDecrease = onDecrementPortions,
                onPortionsReset = onResetPortions,
                onCustomPortions = onVaryIngredient,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }
        item {
            Label(
                text = stringResource(R.string.all_ingredients),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
            )
        }
        if (state.ingredients.isEmpty())
            item {
                Text(
                    stringResource(R.string.detail_ingredients_empty),
                    Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
        else
            items(state.ingredients) { ingredient ->
                IngredientView(ingredient = ingredient) {
                    onToggleIngredientCheck(ingredient.id, it)
                }
            }
        item {
            Label(
                text = stringResource(R.string.all_instructions),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
            )
        }
        if (state.instructions.isEmpty())
            item {
                Text(
                    stringResource(R.string.detail_instructions_empty),
                    Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
        else
            items(state.instructions) { item ->
                InstructionView(
                    instruction = item.instruction,
                    isChecked = item.isChecked,
                    onCheckChanged = { onToggleInstructionCheck(item.id, it) }
                )
            }

        item {
            Label(
                stringResource(R.string.all_comments),
                Modifier.padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }
        if (state.comments.isEmpty())
            item {
                Text(
                    stringResource(R.string.detail_comments_empty),
                    Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
        else
            items(state.comments) { item ->
                Detail(
                    text = item,
                    Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
        item {
            FilledTonalIconTextButton(
                onClick = onAddComment,
                text = { Text(stringResource(id = R.string.all_add_new)) },
                leadingIcon = {
                    Icon(Icons.Rounded.Add, contentDescription = null)
                },
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
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

package com.roxana.recipeapp.edit.time.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.time.EditRecipeTimeViewState
import com.roxana.recipeapp.edit.time.isCookingValid
import com.roxana.recipeapp.edit.time.isPreparationValid
import com.roxana.recipeapp.edit.time.isTotalValid
import com.roxana.recipeapp.edit.time.isWaitingValid
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.RecipeTextField

@Composable
fun EditRecipeTimeView(
    state: EditRecipeTimeViewState,
    preparationFocusRequester: FocusRequester,
    cookingFocusRequester: FocusRequester,
    waitingFocusRequester: FocusRequester,
    totalFocusRequester: FocusRequester,
    onCookingChange: (String) -> Unit = {},
    onPreparationChange: (String) -> Unit = {},
    onWaitingChange: (String) -> Unit = {},
    onTotalChange: (String) -> Unit = {},
    onComputeTotal: () -> Unit = {},
    onValidate: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(3f))
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.ic_time),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(40.dp)
            )
            LabelView(text = stringResource(R.string.edit_recipe_time_label))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RecipeTextField(
                value = state.cooking,
                onValueChange = onCookingChange,
                isError = !state.isCookingValid(),
                label = stringResource(R.string.edit_recipe_time_cooking_hint),
                keyboardType = KeyboardType.Number,
                textStyle = MaterialTheme.typography.body1,
                imeAction = ImeAction.Next,
                onImeAction = { preparationFocusRequester.requestFocus() },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .defaultMinSize(0.dp, 0.dp)
                    .weight(1f)
                    .focusRequester(cookingFocusRequester)
            )
            RecipeTextField(
                value = state.preparation,
                onValueChange = onPreparationChange,
                isError = !state.isPreparationValid(),
                label = stringResource(R.string.edit_recipe_time_preparation_hint),
                keyboardType = KeyboardType.Number,
                textStyle = MaterialTheme.typography.body1,
                imeAction = ImeAction.Next,
                onImeAction = { waitingFocusRequester.requestFocus() },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .defaultMinSize(0.dp, 0.dp)
                    .weight(1f)
                    .focusRequester(preparationFocusRequester)
            )
            RecipeTextField(
                value = state.waiting,
                onValueChange = onWaitingChange,
                isError = !state.isWaitingValid(),
                label = stringResource(R.string.edit_recipe_time_waiting_hint),
                keyboardType = KeyboardType.Number,
                textStyle = MaterialTheme.typography.body1,
                imeAction = ImeAction.Next,
                onImeAction = { totalFocusRequester.requestFocus() },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .defaultMinSize(0.dp, 0.dp)
                    .weight(1f)
                    .focusRequester(waitingFocusRequester)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RecipeTextField(
                value = state.total,
                onValueChange = onTotalChange,
                isError = !state.isTotalValid(),
                label = stringResource(R.string.edit_recipe_time_total_hint),
                keyboardType = KeyboardType.Number,
                textStyle = MaterialTheme.typography.body1,
                imeAction = ImeAction.Done,
                onImeAction = onValidate,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .defaultMinSize(0.dp, 0.dp)
                    .weight(1f)
                    .focusRequester(totalFocusRequester)
            )
            TextButton(
                onClick = {
                    totalFocusRequester.requestFocus()
                    onComputeTotal()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.edit_recipe_compute_total))
            }
        }
        Spacer(modifier = Modifier.weight(6f))
    }
}

package com.roxana.recipeapp.add.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.TextFieldState
import com.roxana.recipeapp.add.TimeState

@Composable
fun TimeTextField(
    time: TimeState,
    onTimeCookingSet: (String) -> Unit,
    onTimePreparationSet: (String) -> Unit,
    onTimeWaitingSet: (String) -> Unit,
    onTimeTotalSet: (String) -> Unit,
    onComputeTotal: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_time),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 18.dp)
                .padding(start = 8.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            TimeTextField(
                state = time.cooking,
                onValueChange = { onTimeCookingSet(it) },
                label = stringResource(id = R.string.add_recipe_time_cooking_hint)
            )
            TimeTextField(
                state = time.preparation,
                onValueChange = { onTimePreparationSet(it) },
                label = stringResource(id = R.string.add_recipe_time_preparation_hint)
            )
            TimeTextField(
                state = time.waiting,
                onValueChange = { onTimeWaitingSet(it) },
                label = stringResource(id = R.string.add_recipe_time_waiting_hint)
            )
            Row(Modifier.fillMaxWidth()) {
                TimeTextField(
                    state = time.total,
                    onValueChange = { onTimeTotalSet(it) },
                    label = stringResource(id = R.string.add_recipe_time_total_hint),
                    modifier = Modifier
                        .widthIn(0.dp)
                        .weight(1f)
                )
                TextButton(
                    onClick = { onComputeTotal() },
                    modifier = Modifier.weight(1f)
                ) {

                    Text(text = stringResource(R.string.add_recipe_compute_total))
                }
            }
        }
    }
}

@Composable
fun TimeTextField(
    state: TextFieldState,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    AddRecipeTextField(
        state = state,
        onValueChange = { onValueChange(it) },
        label = label,
        keyboardType = KeyboardType.Number,
        textStyle = MaterialTheme.typography.body1,
        modifier = modifier
    )
}

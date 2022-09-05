package com.roxana.recipeapp.edit.instructions.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.instructions.EditRecipeInstructionsViewState
import com.roxana.recipeapp.ui.basecomponents.DividerAlpha16
import com.roxana.recipeapp.ui.basecomponents.Label
import com.roxana.recipeapp.ui.basecomponents.RecipeOutlinedTextField

@Composable
fun EditRecipeInstructionsView(
    state: EditRecipeInstructionsViewState,
    focusRequester: FocusRequester,
    onInstructionChanged: (String) -> Unit = {},
    onInstructionDone: () -> Unit = {},
    onDelete: (Int) -> Unit = {}
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
                painterResource(R.drawable.ic_instructions),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Label(text = stringResource(R.string.all_instructions))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        RecipeOutlinedTextField(
            value = state.editingInstruction,
            onValueChange = onInstructionChanged,
            label = stringResource(R.string.edit_recipe_instruction_hint),
            imeAction = ImeAction.Done,
            onImeAction = onInstructionDone,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
        )
        if (state.instructions.isNotEmpty())
            DividerAlpha16(Modifier.padding(bottom = 4.dp, top = 16.dp))
        LazyColumn {
            itemsIndexed(state.instructions) { index, instruction ->
                InstructionText(instruction, index + 1, Modifier.fillMaxWidth()) {
                    onDelete(index)
                }
            }
            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
        Spacer(modifier = Modifier.weight(6f))
    }
}

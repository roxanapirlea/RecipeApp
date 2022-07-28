package com.roxana.recipeapp.edit.instructions.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import com.roxana.recipeapp.ui.DividerAlpha16
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.button.IconButtonCheckOutline
import com.roxana.recipeapp.ui.textfield.RecipeSecondaryTextField

@Composable
fun EditRecipeInstructionsView(
    state: EditRecipeInstructionsViewState,
    focusRequester: FocusRequester,
    onInstructionChanged: (String) -> Unit = {},
    onInstructionDone: () -> Unit = {},
    onSaveInstruction: () -> Unit = {},
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
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(40.dp)
            )
            LabelView(text = stringResource(R.string.all_instructions))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        RecipeSecondaryTextField(
            value = state.editingInstruction,
            onValueChange = onInstructionChanged,
            label = stringResource(R.string.edit_recipe_instruction_hint),
            trailing = if (state.canAddInstruction) {
                { IconButtonCheckOutline(onSaveInstruction) }
            } else null,
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
        }
        Spacer(modifier = Modifier.weight(6f))
    }
}

package com.roxana.recipeapp.add.instructions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.AddRecipeBackdrop
import com.roxana.recipeapp.add.ForwardIcon
import com.roxana.recipeapp.add.PageType
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.DividerAlpha16
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.TrailingIconTextField
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun AddRecipeInstructionsScreen(
    instructionsViewModel: AddRecipeInstructionsViewModel,
    onBack: () -> Unit = {},
    onForward: () -> Unit = {},
    onNavigate: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(instructionsViewModel.state)
        .collectAsState(AddRecipeInstructionsViewState())

    AddRecipeInstructionsView(
        state,
        instructionsViewModel.sideEffectFlow,
        onInstructionChanged = instructionsViewModel::onInstructionChanged,
        onSaveInstruction = instructionsViewModel::onSaveInstruction,
        onInstructionDone = instructionsViewModel::onInstructionDone,
        onDelete = instructionsViewModel::onDeleteInstruction,
        onValidate = instructionsViewModel::onValidate,
        onSaveAndGoBack = instructionsViewModel::onSaveAndBack,
        onBackNavigation = onBack,
        onForwardNavigation = onForward,
        onSelectPage = instructionsViewModel::onSelectPage,
        onNavigateToPage = onNavigate
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecipeInstructionsView(
    state: AddRecipeInstructionsViewState,
    sideEffectsFlow: Flow<AddRecipeInstructionsSideEffect> = flow { },
    onInstructionChanged: (String) -> Unit = {},
    onInstructionDone: () -> Unit = {},
    onSaveInstruction: () -> Unit = {},
    onDelete: (Int) -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onNavigateToPage: (PageType) -> Unit = {},
    onBackNavigation: () -> Unit = {},
    onForwardNavigation: () -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(sideEffectsFlow) {
        sideEffectsFlow.collect {
            when (it) {
                Forward -> onForwardNavigation()
                Back -> onBackNavigation()
                is NavigateToPage -> onNavigateToPage(it.page)
            }
        }
    }

    AddRecipeBackdrop(
        selectedPage = PageType.Instructions,
        backIcon = Icons.Default.ArrowBack,
        backContentDescription = stringResource(R.string.all_back),
        onSelectPage = onSelectPage,
        onBack = onSaveAndGoBack,
    ) {
        Box(Modifier.fillMaxSize()) {
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                onClick = onValidate
            ) { ForwardIcon() }

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
                TrailingIconTextField(
                    value = state.editingInstruction,
                    onValueChange = onInstructionChanged,
                    label = stringResource(R.string.add_recipe_instruction_hint),
                    trailingIcon = R.drawable.ic_check_outline,
                    onTrailingIconClicked = onSaveInstruction,
                    focusRequester = focusRequester,
                    imeAction = ImeAction.Done,
                    onImeAction = onInstructionDone,
                    modifier = Modifier
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
    }
}

@Composable
fun InstructionText(
    instruction: String,
    index: Int,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$index",
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Text(
            text = instruction,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground,
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .weight(3f)
        )
        Icon(
            Icons.Default.Delete,
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_save),
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable { onDelete() }
                .padding(12.dp)
                .size(32.dp)
        )
    }
}

@Preview
@Composable
fun AddRecipeInstructionsViewPreview() {
    RecipeTheme {
        AddRecipeInstructionsView(AddRecipeInstructionsViewState())
    }
}

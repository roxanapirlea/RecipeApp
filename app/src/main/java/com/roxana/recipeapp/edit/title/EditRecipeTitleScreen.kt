package com.roxana.recipeapp.edit.title

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.ForwardIcon
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.SaveCreationDialog
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.RecipeTextField
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun EditRecipeTitleScreen(
    editRecipeTitleViewModel: EditRecipeTitleViewModel,
    onBack: () -> Unit = {},
    onForward: () -> Unit = {},
    onNavigate: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(editRecipeTitleViewModel.state)
        .collectAsState(EditRecipeTitleViewState())

    AddRecipeTitleView(
        state,
        editRecipeTitleViewModel.sideEffectFlow,
        onTitleChanged = editRecipeTitleViewModel::onTitleChanged,
        onValidate = editRecipeTitleViewModel::onValidate,
        onCheckBack = editRecipeTitleViewModel::onCheckBack,
        onResetAndGoBack = editRecipeTitleViewModel::onReset,
        onSaveAndGoBack = editRecipeTitleViewModel::onSaveAndBack,
        onDiscardDialog = editRecipeTitleViewModel::onDiscardDialog,
        onBackNavigation = onBack,
        onForwardNavigation = onForward,
        onSelectPage = editRecipeTitleViewModel::onSelectPage,
        onNavigateToPage = onNavigate
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecipeTitleView(
    state: EditRecipeTitleViewState,
    sideEffectsFlow: Flow<EditRecipeTitleSideEffect> = flow { },
    onTitleChanged: (String) -> Unit = {},
    onCheckBack: () -> Unit = {},
    onResetAndGoBack: () -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
    onDiscardDialog: () -> Unit = {},
    onBackNavigation: () -> Unit = {},
    onForwardNavigation: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onNavigateToPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(sideEffectsFlow) {
        sideEffectsFlow.collect {
            when (it) {
                Forward -> onForwardNavigation()
                Back -> onBackNavigation()
                is NavigateToPage -> onNavigateToPage(it.page)
                RevealBackdrop -> {
                    delay(500)
                    scaffoldState.reveal()
                    delay(500)
                    scaffoldState.conceal()
                }
            }
        }
    }

    EditRecipeBackdrop(
        selectedPage = PageType.Title,
        backIcon = Icons.Default.Clear,
        backContentDescription = stringResource(R.string.all_cross),
        onSelectPage = onSelectPage,
        onBack = onCheckBack,
        scaffoldState = scaffoldState
    ) {
        Box(Modifier.fillMaxSize()) {

            if (state.showSaveDialog)
                SaveCreationDialog(
                    onSave = onSaveAndGoBack,
                    onDelete = onResetAndGoBack,
                    onDiscard = onDiscardDialog
                )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.weight(3f))
                LabelView(
                    text = stringResource(R.string.edit_recipe_title_label),
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Spacer(modifier = Modifier.weight(0.5f))
                RecipeTextField(
                    value = state.title,
                    onValueChange = onTitleChanged,
                    placeholder = stringResource(R.string.edit_recipe_title_hint),
                    imeAction = ImeAction.Done,
                    onImeAction = onValidate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
                Spacer(modifier = Modifier.weight(6f))
            }
            if (state.isValid())
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onClick = onValidate
                ) { ForwardIcon() }
        }
    }
}

@Preview
@Composable
fun AddRecipeTitleScreenPreview() {
    RecipeTheme {
        AddRecipeTitleView(EditRecipeTitleViewState())
    }
}

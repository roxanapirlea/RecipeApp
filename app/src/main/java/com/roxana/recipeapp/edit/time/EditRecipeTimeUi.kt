package com.roxana.recipeapp.edit.time

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.time.ui.EditRecipeTimeView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeTimeDestination(
    editRecipeTimeViewModel: EditRecipeTimeViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    val state by rememberFlowWithLifecycle(editRecipeTimeViewModel.state)
        .collectAsState(EditRecipeTimeViewState())

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Back -> onNavBack()
                is Navigation.ToPage -> onNavToPage(navigation.page, navigation.isExistingRecipe)
            }
            editRecipeTimeViewModel.onNavigationDone()
        }
    }

    EditRecipeTimeScreen(
        state,
        onCookingChange = editRecipeTimeViewModel::onCookingChanged,
        onPreparationChange = editRecipeTimeViewModel::onPreparationChanged,
        onWaitingChange = editRecipeTimeViewModel::onWaitingChanged,
        onTotalChange = editRecipeTimeViewModel::onTotalChanged,
        onComputeTotal = editRecipeTimeViewModel::onComputeTotal,
        onValidate = editRecipeTimeViewModel::onValidate,
        onBack = editRecipeTimeViewModel::onBack,
        onSelectPage = editRecipeTimeViewModel::onSelectPage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeTimeScreen(
    state: EditRecipeTimeViewState,
    onCookingChange: (String) -> Unit = {},
    onPreparationChange: (String) -> Unit = {},
    onWaitingChange: (String) -> Unit = {},
    onTotalChange: (String) -> Unit = {},
    onComputeTotal: () -> Unit = {},
    onBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val cookingFocusRequester = remember { FocusRequester() }
    val preparationFocusRequester = remember { FocusRequester() }
    val waitingFocusRequester = remember { FocusRequester() }
    val totalFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        cookingFocusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            AppBarBack(
                title = if (state.isExistingRecipe)
                    stringResource(R.string.edit_title_existing_recipe)
                else
                    stringResource(R.string.edit_title_new_recipe),
                onIconClick = onBack
            )
        },
        floatingActionButton = { if (state.isValid()) FabForward(onClick = onValidate) }
    ) { contentPadding ->
        Box(Modifier.fillMaxSize().padding(contentPadding)) {
            EditRecipeTimeView(
                state = state,
                preparationFocusRequester = preparationFocusRequester,
                cookingFocusRequester = cookingFocusRequester,
                waitingFocusRequester = waitingFocusRequester,
                totalFocusRequester = totalFocusRequester,
                onCookingChange = onCookingChange,
                onPreparationChange = onPreparationChange,
                onWaitingChange = onWaitingChange,
                onTotalChange = onTotalChange,
                onComputeTotal = onComputeTotal,
                onValidate = onValidate
            )
        }
    }
}

@Preview
@Composable
fun EditRecipeTimeViewPreview() {
    RecipeTheme {
        EditRecipeTimeScreen(EditRecipeTimeViewState())
    }
}

package com.roxana.recipeapp.edit.portions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.portions.ui.EditRecipePortionsView
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipePortionsDestination(
    editRecipePortionsViewModel: EditRecipePortionsViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    val state by rememberFlowWithLifecycle(editRecipePortionsViewModel.state)
        .collectAsState(EditRecipePortionsViewState())

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Back -> onNavBack()
                is Navigation.ToPage -> onNavToPage(navigation.page, navigation.isExistingRecipe)
            }
            editRecipePortionsViewModel.onNavigationDone()
        }
    }

    EditRecipePortionsScreen(
        state,
        onPortionsChanged = editRecipePortionsViewModel::onPortionsChanged,
        onValidate = editRecipePortionsViewModel::onValidate,
        onBack = editRecipePortionsViewModel::onBack,
        onSelectPage = editRecipePortionsViewModel::onSelectPage,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipePortionsScreen(
    state: EditRecipePortionsViewState,
    onPortionsChanged: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Portions,
        onSelectPage = onSelectPage,
        onNavIcon = onBack
    ) {
        Box(Modifier.fillMaxSize()) {
            EditRecipePortionsView(
                state = state,
                focusRequester = focusRequester,
                onPortionsChanged = onPortionsChanged,
                onValidate = onValidate
            )

            if (state.isValid())
                FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)
        }
    }
}

@Preview
@Composable
fun EditRecipePortionsViewPreview() {
    RecipeTheme {
        EditRecipePortionsScreen(EditRecipePortionsViewState())
    }
}

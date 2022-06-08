package com.roxana.recipeapp.edit.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.categories.ui.EditRecipeCategoriesView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun EditRecipeCategoriesDestination(
    editRecipeCategoriesViewModel: EditRecipeCategoriesViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(editRecipeCategoriesViewModel.state)
        .collectAsState(EditRecipeCategoriesViewState())

    LaunchedEffect(editRecipeCategoriesViewModel.sideEffectFlow) {
        editRecipeCategoriesViewModel.sideEffectFlow.collect {
            when (it) {
                ForwardForCreation -> onCreationNavForward()
                ForwardForEditing -> onEditNavForward()
                Back -> onNavBack()
                is NavigateToPage -> onNavToPage(it.page)
            }
        }
    }

    EditRecipeCategoriesScreen(
        state,
        onCategoryClicked = editRecipeCategoriesViewModel::onCategoryClicked,
        onValidate = editRecipeCategoriesViewModel::onValidate,
        onSaveAndGoBack = editRecipeCategoriesViewModel::onSaveAndBack,
        onSelectPage = editRecipeCategoriesViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeCategoriesScreen(
    state: EditRecipeCategoriesViewState,
    onCategoryClicked: (UiCategoryType) -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {}
) {

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Categories,
        onSelectPage = onSelectPage,
        onBack = onSaveAndGoBack
    ) {
        Box(Modifier.fillMaxSize()) {
            FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)

            EditRecipeCategoriesView(state, onCategoryClicked)
        }
    }
}

@Preview
@Composable
fun EditRecipeCategoriesScreenPreview() {
    RecipeTheme {
        EditRecipeCategoriesScreen(EditRecipeCategoriesViewState())
    }
}

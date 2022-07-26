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
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.categories.ui.EditRecipeCategoriesView
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun EditRecipeCategoriesDestination(
    editRecipeCategoriesViewModel: EditRecipeCategoriesViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    val state by rememberFlowWithLifecycle(editRecipeCategoriesViewModel.state)
        .collectAsState(EditRecipeCategoriesViewState())

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Back -> onNavBack()
                is Navigation.ToPage -> onNavToPage(navigation.page, navigation.isExistingRecipe)
            }
            editRecipeCategoriesViewModel.onNavigationDone()
        }
    }

    EditRecipeCategoriesScreen(
        state,
        onCategoryClicked = editRecipeCategoriesViewModel::onCategoryClicked,
        onValidate = editRecipeCategoriesViewModel::onValidate,
        onBack = editRecipeCategoriesViewModel::onBack,
        onSelectPage = editRecipeCategoriesViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeCategoriesScreen(
    state: EditRecipeCategoriesViewState,
    onCategoryClicked: (UiCategoryType) -> Unit = {},
    onBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {}
) {

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Categories,
        onSelectPage = onSelectPage,
        onNavIcon = onBack
    ) {
        Box(Modifier.fillMaxSize()) {
            EditRecipeCategoriesView(state, onCategoryClicked)
            FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)
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

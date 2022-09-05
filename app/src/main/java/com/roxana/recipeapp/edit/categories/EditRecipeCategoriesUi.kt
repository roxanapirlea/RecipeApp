package com.roxana.recipeapp.edit.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.categories.ui.EditRecipeCategoriesView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeCategoriesScreen(
    state: EditRecipeCategoriesViewState,
    onCategoryClicked: (UiCategoryType) -> Unit = {},
    onBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {}
) {
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
        floatingActionButton = { FabForward(onClick = onValidate) }
    ) { contentPadding ->
        Box(Modifier.fillMaxSize().padding(contentPadding)) {
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

package com.roxana.recipeapp.edit.ingredients

import androidx.compose.foundation.layout.Column
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
import com.roxana.recipeapp.edit.FabSave
import com.roxana.recipeapp.edit.PageProgress
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.ingredients.ui.EditRecipeIngredientsView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun EditRecipeIngredientsDestination(
    ingredientsViewModel: EditRecipeIngredientsViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    val state by rememberFlowWithLifecycle(ingredientsViewModel.state)
        .collectAsState(EditRecipeIngredientsViewState())

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Back -> onNavBack()
                is Navigation.ToPage -> onNavToPage(navigation.page, navigation.isExistingRecipe)
            }
            ingredientsViewModel.onNavigationDone()
        }
    }

    EditRecipeIngredientsScreen(
        state,
        onIngredientNameChanged = ingredientsViewModel::onIngredientNameChanged,
        onIngredientQuantityChanged = ingredientsViewModel::onIngredientQuantityChanged,
        onIngredientQuantityTypeChanged = ingredientsViewModel::onIngredientQuantityTypeChanged,
        onSaveIngredient = ingredientsViewModel::onSaveIngredient,
        onDelete = ingredientsViewModel::onDeleteIngredient,
        onValidate = ingredientsViewModel::onValidate,
        onBack = ingredientsViewModel::onBack,
        onSelectPage = ingredientsViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeIngredientsScreen(
    state: EditRecipeIngredientsViewState,
    onIngredientNameChanged: (String) -> Unit = {},
    onIngredientQuantityChanged: (String) -> Unit = {},
    onIngredientQuantityTypeChanged: (UiQuantityType) -> Unit = {},
    onSaveIngredient: () -> Unit = {},
    onDelete: (Int) -> Unit = {},
    onBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val startFocusRequester: FocusRequester = remember { FocusRequester() }

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
        floatingActionButton = {
            if (state.canAddIngredient)
                FabSave {
                    onSaveIngredient()
                    startFocusRequester.requestFocus()
                }
            else
                FabForward(onClick = onValidate)
        }
    ) { contentPadding ->
        Column(Modifier.fillMaxSize().padding(contentPadding)) {
            PageProgress(
                recipeAlreadyExists = state.isExistingRecipe,
                selected = PageType.Ingredients,
                onSelectPage = onSelectPage
            )
            EditRecipeIngredientsView(
                state = state,
                onIngredientNameChanged = onIngredientNameChanged,
                onIngredientQuantityChanged = onIngredientQuantityChanged,
                onIngredientQuantityTypeChanged = onIngredientQuantityTypeChanged,
                onSaveIngredient = onSaveIngredient,
                onDelete = onDelete,
                startFocusRequester = startFocusRequester,
            )
        }
    }
}

@Preview
@Composable
fun EditRecipeIngredientsViewPreview() {
    RecipeTheme {
        EditRecipeIngredientsScreen(EditRecipeIngredientsViewState())
    }
}

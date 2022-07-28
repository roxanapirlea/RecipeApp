package com.roxana.recipeapp.edit.ingredients

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
import com.roxana.recipeapp.edit.FabSave
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.ingredients.ui.EditRecipeIngredientsView
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

@OptIn(ExperimentalMaterialApi::class)
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

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Ingredients,
        onSelectPage = onSelectPage,
        onNavIcon = onBack
    ) {
        Box(Modifier.fillMaxSize()) {
            EditRecipeIngredientsView(
                state = state,
                onIngredientNameChanged = onIngredientNameChanged,
                onIngredientQuantityChanged = onIngredientQuantityChanged,
                onIngredientQuantityTypeChanged = onIngredientQuantityTypeChanged,
                onSaveIngredient = onSaveIngredient,
                onDelete = onDelete,
                startFocusRequester = startFocusRequester,
            )

            if (state.canAddIngredient)
                FabSave(modifier = Modifier.align(Alignment.BottomEnd)) {
                    onSaveIngredient()
                    startFocusRequester.requestFocus()
                }
            else
                FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)
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

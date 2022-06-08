package com.roxana.recipeapp.edit.ingredients

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
import com.roxana.recipeapp.edit.ingredients.ui.EditRecipeIngredientsView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun EditRecipeIngredientsDestination(
    ingredientsViewModel: EditRecipeIngredientsViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(ingredientsViewModel.state)
        .collectAsState(EditRecipeIngredientsViewState())

    LaunchedEffect(ingredientsViewModel.sideEffectFlow) {
        ingredientsViewModel.sideEffectFlow.collect {
            when (it) {
                ForwardForCreation -> onCreationNavForward()
                ForwardForEditing -> onEditNavForward()
                Back -> onNavBack()
                is NavigateToPage -> onNavToPage(it.page)
            }
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
        onSaveAndGoBack = ingredientsViewModel::onSaveAndBack,
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
    onSaveAndGoBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Ingredients,
        onSelectPage = onSelectPage,
        onBack = onSaveAndGoBack
    ) {
        Box(Modifier.fillMaxSize()) {
            FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)

            EditRecipeIngredientsView(
                state = state,
                onIngredientNameChanged = onIngredientNameChanged,
                onIngredientQuantityChanged = onIngredientQuantityChanged,
                onIngredientQuantityTypeChanged = onIngredientQuantityTypeChanged,
                onSaveIngredient = onSaveIngredient,
                onDelete = onDelete,
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

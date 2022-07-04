package com.roxana.recipeapp.edit.ingredients

import androidx.activity.compose.BackHandler
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
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.FabForward
import com.roxana.recipeapp.edit.FabSave
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.SaveCreationDialog
import com.roxana.recipeapp.edit.ingredients.ui.EditRecipeIngredientsView
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun EditRecipeIngredientsDestination(
    ingredientsViewModel: EditRecipeIngredientsViewModel,
    onNavFinish: () -> Unit = {},
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
                Close -> onNavFinish()
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
        onClose = ingredientsViewModel::onCheckShouldClose,
        onResetAndClose = ingredientsViewModel::onResetAndClose,
        onSaveAndClose = ingredientsViewModel::onSaveAndClose,
        onDismissDialog = ingredientsViewModel::onDismissDialog,
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
    onClose: () -> Unit = {},
    onResetAndClose: () -> Unit = {},
    onSaveAndClose: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val startFocusRequester: FocusRequester = remember { FocusRequester() }

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Ingredients,
        onSelectPage = onSelectPage,
        onClose = onClose
    ) {
        Box(Modifier.fillMaxSize()) {
            BackHandler(onBack = onClose)

            if (state.showSaveDialog)
                SaveCreationDialog(
                    onSave = onSaveAndClose,
                    onDelete = onResetAndClose,
                    onDismiss = onDismissDialog
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

            if (state.editingIngredient.isEmpty())
                FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)
            else
                FabSave(modifier = Modifier.align(Alignment.BottomEnd)) {
                    onSaveIngredient()
                    startFocusRequester.requestFocus()
                }
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

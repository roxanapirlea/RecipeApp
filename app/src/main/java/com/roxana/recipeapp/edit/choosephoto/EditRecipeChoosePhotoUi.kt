package com.roxana.recipeapp.edit.choosephoto

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
import com.roxana.recipeapp.edit.choosephoto.ui.EditRecipeChoosePhotoView
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EditRecipeChoosePhotoDestination(
    editRecipeChoosePhotoViewModel: EditRecipeChoosePhotoViewModel,
    onNavBack: () -> Unit = {},
    onCreationNavForward: () -> Unit = {},
    onEditNavForward: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
    onCapturePhoto: () -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(editRecipeChoosePhotoViewModel.state)
        .collectAsState(EditRecipeChoosePhotoViewState())

    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.ForwardCreation -> onCreationNavForward()
                Navigation.ForwardEditing -> onEditNavForward()
                Navigation.Back -> onNavBack()
                is Navigation.ToPage -> onNavToPage(navigation.page, navigation.isExistingRecipe)
                Navigation.PhotoCapture -> onCapturePhoto()
            }
            editRecipeChoosePhotoViewModel.onNavigationDone()
        }
    }

    EditRecipeChoosePhotoScreen(
        state,
        onCapturePhoto = onCapturePhoto,
        onRecapturePhoto = editRecipeChoosePhotoViewModel::onRecapturePhoto,
        onClearPhoto = editRecipeChoosePhotoViewModel::onClearPhoto,
        onValidate = editRecipeChoosePhotoViewModel::onValidate,
        onBack = editRecipeChoosePhotoViewModel::onBack,
        onSelectPage = editRecipeChoosePhotoViewModel::onSelectPage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeChoosePhotoScreen(
    state: EditRecipeChoosePhotoViewState,
    onCapturePhoto: () -> Unit = {},
    onRecapturePhoto: () -> Unit = {},
    onClearPhoto: () -> Unit = {},
    onBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Photo,
        onSelectPage = onSelectPage,
        onNavIcon = onBack
    ) {
        Box(Modifier.fillMaxSize()) {
            EditRecipeChoosePhotoView(
                state = state,
                onCapturePhoto = onCapturePhoto,
                onRecapturePhoto = onRecapturePhoto,
                onClearPhoto = onClearPhoto,
            )
            FabForward(modifier = Modifier.align(Alignment.BottomEnd), onValidate)
        }
    }
}

@Preview
@Composable
fun EditRecipeTemperatureViewPreview() {
    RecipeTheme {
        EditRecipeChoosePhotoScreen(EditRecipeChoosePhotoViewState())
    }
}

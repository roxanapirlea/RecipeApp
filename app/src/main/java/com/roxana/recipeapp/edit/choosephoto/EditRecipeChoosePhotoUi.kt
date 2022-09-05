package com.roxana.recipeapp.edit.choosephoto

import androidx.compose.foundation.layout.Column
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
import com.roxana.recipeapp.edit.PageProgress
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.choosephoto.ui.EditRecipeChoosePhotoView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack
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

@OptIn(ExperimentalMaterial3Api::class)
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
        Column(Modifier.fillMaxSize().padding(contentPadding)) {
            PageProgress(
                recipeAlreadyExists = state.isExistingRecipe,
                selected = PageType.Photo,
                onSelectPage = onSelectPage
            )
            EditRecipeChoosePhotoView(
                state = state,
                onCapturePhoto = onCapturePhoto,
                onRecapturePhoto = onRecapturePhoto,
                onClearPhoto = onClearPhoto
            )
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

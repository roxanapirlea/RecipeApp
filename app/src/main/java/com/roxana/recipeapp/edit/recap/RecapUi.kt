package com.roxana.recipeapp.edit.recap

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.SaveCreationDialog
import com.roxana.recipeapp.edit.recap.ui.RecapView
import com.roxana.recipeapp.ui.theme.RecipeTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecapDestination(
    recapViewModel: RecapViewModel,
    onNavBack: () -> Unit = {},
    onNavFinish: () -> Unit = {},
    onNavToPage: (PageType) -> Unit = {},
    onNavToPhotoCapture: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(recapViewModel.state)
        .collectAsState(RecapViewState())

    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    val localContext = LocalContext.current.applicationContext

    if (state.isFetchingError) {
        LaunchedEffect(state.isFetchingError) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = localContext.getString(R.string.detail_recipe_fetch_error),
                duration = SnackbarDuration.Short
            )
            recapViewModel.onErrorDismissed()
        }
    }
    if (state.shouldClose) {
        LaunchedEffect(state.shouldClose) {
            onNavFinish()
            recapViewModel.onClosingDone()
        }
    }
    state.saveResult?.let { result ->
        LaunchedEffect(result) {
            if (result.isSuccessful) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = localContext.getString(R.string.edit_recipe_save_success),
                    duration = SnackbarDuration.Short
                )
                onNavFinish()
            } else {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = localContext.getString(R.string.edit_recipe_save_error),
                    duration = SnackbarDuration.Short
                )
            }
            recapViewModel.onSaveResultDismissed()
        }
    }

    RecapScreen(
        state,
        scaffoldState,
        onCreateRecipe = recapViewModel::createRecipe,
        onEdit = onNavBack,
        onClose = recapViewModel::onCheckShouldClose,
        onResetAndClose = recapViewModel::onResetAndClose,
        onSaveAndClose = recapViewModel::onClose,
        onDismissDialog = recapViewModel::onDismissDialog,
        onSelectPage = onNavToPage,
        onTakePicture = onNavToPhotoCapture,
        onDeletePicture = recapViewModel::onDeletePicture
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecapScreen(
    state: RecapViewState,
    scaffoldState: BackdropScaffoldState,
    onCreateRecipe: () -> Unit = {},
    onEdit: () -> Unit = {},
    onClose: () -> Unit = {},
    onResetAndClose: () -> Unit = {},
    onSaveAndClose: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onTakePicture: () -> Unit = {},
    onDeletePicture: () -> Unit = {},
) {
    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Recap,
        scaffoldState = scaffoldState,
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

            RecapView(
                state = state,
                onSave = onCreateRecipe,
                onEdit = onEdit,
                onTakePicture = onTakePicture,
                onDeletePicture = onDeletePicture
            )
        }
    }
}

@Preview
@Composable
fun RecapEmptyViewPreview() {
    RecipeTheme {
        RecapView(RecapViewState())
    }
}

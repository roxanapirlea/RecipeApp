package com.roxana.recipeapp.edit.recap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.edit.PageProgress
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.recap.ui.RecapView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack
import com.roxana.recipeapp.ui.basecomponents.ExtendedTextIconFab
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun RecapDestination(
    recapViewModel: RecapViewModel,
    onNavBack: () -> Unit = {},
    onNavFinish: () -> Unit = {},
    onNavToPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    val state by rememberFlowWithLifecycle(recapViewModel.state)
        .collectAsState(RecapViewState())

    val snackbarHostState = remember { SnackbarHostState() }
    val localContext = LocalContext.current.applicationContext

    if (state.isFetchingError) {
        LaunchedEffect(state.isFetchingError) {
            snackbarHostState.showSnackbar(
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
            onNavFinish()
            recapViewModel.onSaveResultDismissed()
        }
    }

    RecapScreen(
        state,
        onCreateRecipe = recapViewModel::createRecipe,
        onBack = onNavBack,
        onSelectPage = onNavToPage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecapScreen(
    state: RecapViewState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onCreateRecipe: () -> Unit = {},
    onBack: () -> Unit = {},
    onSelectPage: (pageType: PageType, isEdition: Boolean) -> Unit = { _, _ -> },
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            ExtendedTextIconFab(
                onClick = onCreateRecipe,
                text = { Text(stringResource(R.string.all_save)) },
                leadingIcon = { Icon(Icons.Rounded.Check, contentDescription = null) }
            )
        }
    ) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            PageProgress(
                recipeAlreadyExists = state.isExistingRecipe,
                selected = PageType.Recap,
                onSelectPage = { onSelectPage(it, state.isExistingRecipe) }
            )
            RecapView(state = state)
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

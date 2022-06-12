package com.roxana.recipeapp.edit.recap

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.recap.ui.RecapView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun RecapDestination(
    recapViewModel: RecapViewModel,
    onNavBack: () -> Unit = {},
    onNavFinish: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(recapViewModel.state)
        .collectAsState(RecapViewState())

    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext

    LaunchedEffect(recapViewModel.sideEffectFlow) {
        recapViewModel.sideEffectFlow.collect {
            when (it) {
                SaveRecipeError -> scaffoldState.snackbarHostState.showSnackbar(
                    message = localContext.getString(R.string.edit_recipe_save_error),
                    duration = SnackbarDuration.Short
                )
                SaveRecipeSuccess -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = localContext.getString(R.string.edit_recipe_save_success),
                        duration = SnackbarDuration.Short
                    )
                    onNavFinish()
                }
            }
        }
    }

    RecapScreen(
        state,
        scaffoldState,
        onSave = recapViewModel::saveRecipe,
        onEdit = onNavBack,
        onBack = onNavBack
    )
}

@Composable
fun RecapScreen(
    state: RecapViewState,
    scaffoldState: ScaffoldState,
    onSave: () -> Unit = {},
    onEdit: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(title = stringResource(R.string.edit_recipe_recap), onIconClick = onBack)
        }
    ) { contentPadding ->
        RecapView(
            state = state,
            modifier = Modifier.padding(contentPadding),
            onSave = onSave,
            onEdit = onEdit
        )
    }
}

@Preview
@Composable
fun RecapEmptyViewPreview() {
    RecipeTheme {
        RecapView(RecapViewState())
    }
}

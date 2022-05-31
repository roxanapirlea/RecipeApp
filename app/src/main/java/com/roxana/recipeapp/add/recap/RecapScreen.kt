package com.roxana.recipeapp.add.recap

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.recap.ui.RecapView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun RecapScreen(
    recapViewModel: RecapViewModel,
    onBack: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(recapViewModel.state)
        .collectAsState(RecapViewState())

    RecapView(state, recapViewModel.sideEffectFlow) { action ->
        when (action) {
            Save -> recapViewModel.saveRecipe()
            Edit -> onBack()
            Back -> onBack()
            Finish -> onFinish()
        }
    }
}

@Composable
fun RecapView(
    state: RecapViewState,
    sideEffectsFlow: Flow<RecapSideEffect> = flow { },
    onAction: (RecapViewAction) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext

    LaunchedEffect(sideEffectsFlow) {
        sideEffectsFlow.collect {
            when (it) {
                SaveRecipeError -> scaffoldState.snackbarHostState.showSnackbar(
                    message = localContext.getString(R.string.add_recipe_save_error),
                    duration = SnackbarDuration.Short
                )
                SaveRecipeSuccess -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = localContext.getString(R.string.add_recipe_save_success),
                        duration = SnackbarDuration.Short
                    )
                    onAction(Finish)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                title = stringResource(R.string.add_recipe_recap),
                icon = R.drawable.ic_arrow_back
            ) { onAction(Back) }
        }
    ) {
        RecapView(state = state, onAction)
    }
}

@Preview
@Composable
fun RecapEmptyViewPreview() {
    RecipeTheme {
        RecapView(RecapViewState())
    }
}

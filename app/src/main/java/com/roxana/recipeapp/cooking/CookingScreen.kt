package com.roxana.recipeapp.cooking

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
import com.roxana.recipeapp.cooking.ui.CookingInProgressView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.LoadingStateView
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun CookingScreen(
    cookingViewModel: CookingViewModel,
    onBack: () -> Unit = {},
    onAddComment: () -> Unit = {},
    onVaryIngredient: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(cookingViewModel.state)
        .collectAsState(CookingViewState.Loading)

    CookingView(state, cookingViewModel.sideEffectFlow) { action ->
        when (action) {
            Back -> onBack()
            DecrementPortions -> cookingViewModel.onDecrementPortions()
            IncrementPortions -> cookingViewModel.onIncrementPortions()
            ResetPortions -> cookingViewModel.onResetPortions()
            is ToggleIngredientCheck ->
                cookingViewModel.toggleIngredientCheck(action.ingredientId, action.isChecked)
            is ToggleInstructionCheck ->
                cookingViewModel.toggleInstructionCheck(action.instructionId, action.isChecked)
            ModifyQuantitiesByIngredient -> onVaryIngredient()
            AddComment -> onAddComment()
        }
    }
}

@Composable
fun CookingView(
    state: CookingViewState,
    sideEffectsFlow: Flow<CookingSideEffect> = flow { },
    onAction: (CookingViewAction) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext

    LaunchedEffect(sideEffectsFlow) {
        sideEffectsFlow.collect { sideEffect ->
            when (sideEffect) {
                FetchingError -> scaffoldState.snackbarHostState.showSnackbar(
                    message = localContext.getString(R.string.cooking_fetch_error),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                title = stringResource(R.string.home_title),
                icon = R.drawable.ic_arrow_back
            ) { onAction(Back) }
        }
    ) {
        when (state) {
            CookingViewState.Loading -> LoadingStateView()
            is CookingViewState.Content -> CookingInProgressView(state, onAction = onAction)
        }
    }
}

@Preview
@Composable
fun DetailEmptyViewPreview() {
    RecipeTheme {
        CookingView(CookingViewState.Loading)
    }
}

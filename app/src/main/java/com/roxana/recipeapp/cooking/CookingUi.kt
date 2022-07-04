package com.roxana.recipeapp.cooking

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
import com.roxana.recipeapp.R
import com.roxana.recipeapp.cooking.ui.CookingInProgressView
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.LoadingStateView

@Composable
fun CookingDestination(
    cookingViewModel: CookingViewModel,
    onNavBack: () -> Unit = {},
    onNavAddComment: () -> Unit = {},
    onNavVaryIngredient: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(cookingViewModel.state)
        .collectAsState(CookingViewState(isLoading = true))

    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext

    LaunchedEffect(cookingViewModel.sideEffectFlow) {
        cookingViewModel.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                FetchingError -> scaffoldState.snackbarHostState.showSnackbar(
                    message = localContext.getString(R.string.cooking_fetch_error),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    CookingView(
        state,
        scaffoldState,
        onBack = onNavBack,
        onVaryIngredient = onNavVaryIngredient,
        onAddComment = onNavAddComment,
        onDecrementPortions = cookingViewModel::onDecrementPortions,
        onIncrementPortions = cookingViewModel::onIncrementPortions,
        onResetPortions = cookingViewModel::onResetPortions,
        onToggleIngredientCheck = cookingViewModel::toggleIngredientCheck,
        onToggleInstructionCheck = cookingViewModel::toggleInstructionCheck,
    )
}

@Composable
fun CookingView(
    state: CookingViewState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onBack: () -> Unit = {},
    onVaryIngredient: () -> Unit = {},
    onAddComment: () -> Unit = {},
    onDecrementPortions: () -> Unit = {},
    onIncrementPortions: () -> Unit = {},
    onResetPortions: () -> Unit = {},
    onToggleIngredientCheck: (id: Int, isChecked: Boolean) -> Unit = { _, _ -> },
    onToggleInstructionCheck: (id: Short, isChecked: Boolean) -> Unit = { _, _ -> }
) {

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(title = stringResource(R.string.home_title), onIconClick = onBack)
        }
    ) { contentPadding ->
        when (state.isLoading) {
            true -> LoadingStateView(Modifier.padding(contentPadding))
            false -> CookingInProgressView(
                state,
                modifier = Modifier.padding(contentPadding),
                onVaryIngredient = onVaryIngredient,
                onAddComment = onAddComment,
                onDecrementPortions = onDecrementPortions,
                onIncrementPortions = onIncrementPortions,
                onResetPortions = onResetPortions,
                onToggleIngredientCheck = onToggleIngredientCheck,
                onToggleInstructionCheck = onToggleInstructionCheck,
            )
        }
    }
}

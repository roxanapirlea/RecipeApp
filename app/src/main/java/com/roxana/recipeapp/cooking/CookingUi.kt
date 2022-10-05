package com.roxana.recipeapp.cooking

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.cooking.ui.CookingInProgressView
import com.roxana.recipeapp.ui.LoadingStateView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack

@Composable
fun CookingDestination(
    cookingViewModel: CookingViewModel,
    onNavBack: () -> Unit = {},
    onNavAddComment: () -> Unit = {},
    onNavVaryIngredient: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(cookingViewModel.state)
        .collectAsState(CookingViewState(isLoading = true))

    val snackbarHostState = remember { SnackbarHostState() }
    val localContext = LocalContext.current.applicationContext

    if (state.isFetchingError) {
        LaunchedEffect(state.isFetchingError) {
            snackbarHostState.showSnackbar(
                message = localContext.getString(R.string.cooking_fetch_error),
                duration = SnackbarDuration.Short
            )
            cookingViewModel.onDismissError()
        }
    }

    CookingView(
        state,
        snackbarHostState,
        onBack = onNavBack,
        onVaryIngredient = onNavVaryIngredient,
        onAddComment = onNavAddComment,
        onDecrementPortions = cookingViewModel::onDecrementPortions,
        onIncrementPortions = cookingViewModel::onIncrementPortions,
        onResetPortions = cookingViewModel::onResetPortions,
        onToggleIngredientCheck = cookingViewModel::toggleIngredientCheck,
        onToggleInstructionCheck = cookingViewModel::toggleInstructionCheck,
        onEditComments = cookingViewModel::onEditComments,
        onDoneEditComments = cookingViewModel::onDoneEditComments,
        onDeleteComment = cookingViewModel::onDeleteComment
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingView(
    state: CookingViewState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBack: () -> Unit = {},
    onVaryIngredient: () -> Unit = {},
    onAddComment: () -> Unit = {},
    onDecrementPortions: () -> Unit = {},
    onIncrementPortions: () -> Unit = {},
    onResetPortions: () -> Unit = {},
    onToggleIngredientCheck: (id: Int, isChecked: Boolean) -> Unit = { _, _ -> },
    onToggleInstructionCheck: (id: Short, isChecked: Boolean) -> Unit = { _, _ -> },
    onEditComments: () -> Unit = {},
    onDeleteComment: (Int) -> Unit = {},
    onDoneEditComments: () -> Unit = {},
) {

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppBarBack(title = state.title, onIconClick = onBack)
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
                onEditComments = onEditComments,
                onDoneEditComments = onDoneEditComments,
                onDeleteComment = onDeleteComment
            )
        }
    }
}

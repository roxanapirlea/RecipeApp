package com.roxana.recipeapp.detail

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
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.detail.ui.RecipeDetailView
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.LoadingStateView

@Composable
fun DetailDestination(
    detailViewModel: DetailViewModel,
    onNavStartCooking: () -> Unit = {},
    onNavBack: () -> Unit = {},
    onNavAddComment: () -> Unit = {},
    onNavEdit: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(detailViewModel.state)
        .collectAsState(DetailViewState(isLoading = true))

    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext

    if (state.isFetchingError) {
        LaunchedEffect(state.isFetchingError) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = localContext.getString(R.string.cooking_fetch_error),
                duration = SnackbarDuration.Short
            )
            detailViewModel.onErrorDismissed()
        }
    }
    if (state.shouldStartEditing) {
        LaunchedEffect(state.shouldStartEditing) {
            onNavEdit()
            detailViewModel.onEditingStarted()
        }
    }

    DetailScreen(
        state,
        scaffoldState,
        onStartCookingClicked = onNavStartCooking,
        onBackClicked = onNavBack,
        onAddCommentClicked = onNavAddComment,
        onEditClicked = detailViewModel::onEdit,
    )
}

@Composable
fun DetailScreen(
    state: DetailViewState,
    scaffoldState: ScaffoldState,
    onBackClicked: () -> Unit = {},
    onStartCookingClicked: () -> Unit = {},
    onAddCommentClicked: () -> Unit = {},
    onEditClicked: () -> Unit = {}
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(title = stringResource(R.string.home_title), onIconClick = onBackClicked)
        }
    ) { contentPadding ->
        when {
            state.isLoading -> LoadingStateView(Modifier.padding(contentPadding))
            else -> RecipeDetailView(
                state = state,
                modifier = Modifier.padding(contentPadding),
                onStartCookingClicked = onStartCookingClicked,
                onAddCommentClicked = onAddCommentClicked,
                onEditClicked = onEditClicked
            )
        }
    }
}

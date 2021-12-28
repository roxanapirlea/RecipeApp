package com.roxana.recipeapp.detail

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.detail.ui.ContentView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.LoadingStateView
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
    onStartCooking: () -> Unit,
    onBack: () -> Unit = {},
    onAddComment: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(detailViewModel.state)
        .collectAsState(DetailViewState.Loading)

    DetailView(state, detailViewModel.sideEffectFlow) { action ->
        when (action) {
            StartCooking -> onStartCooking()
            Back -> onBack()
            AddComment -> onAddComment()
        }
    }
}

@Composable
fun DetailView(
    state: DetailViewState,
    sideEffectsFlow: Flow<DetailSideEffect> = flow { },
    onAction: (DetailViewAction) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext

    LaunchedEffect(sideEffectsFlow) {
        sideEffectsFlow.collect {
            // no-op
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
            DetailViewState.Loading -> LoadingStateView()
            is DetailViewState.Content -> ContentView(state = state, onAction)
        }
    }
}

@Preview
@Composable
fun DetailEmptyViewPreview() {
    RecipeTheme {
        DetailView(DetailViewState.Loading)
    }
}

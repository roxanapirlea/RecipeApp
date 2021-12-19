package com.roxana.recipeapp.home

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.home.ui.ContentView
import com.roxana.recipeapp.home.ui.EmptyView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.LoadingStateView
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavAddRecipe: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(homeViewModel.state)
        .collectAsState(HomeViewState.Loading)

    HomeView(state, homeViewModel.sideEffectFlow) { action ->
        when (action) {
            AddRecipe -> onNavAddRecipe()
            is RecipeDetail -> {
                // no-op
            }
        }
    }
}

@Composable
fun HomeView(
    state: HomeViewState,
    sideEffectsFlow: Flow<HomeSideEffect> = flow { },
    onAction: (HomeViewAction) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext

    LaunchedEffect(sideEffectsFlow) {
        sideEffectsFlow.collect { sideEffect ->
            when (sideEffect) {
                ItemsFetchingError -> scaffoldState.snackbarHostState.showSnackbar(
                    message = localContext.getString(R.string.home_recipe_fetch_error),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(title = stringResource(R.string.home_title))
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(AddRecipe) }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }
    ) {
        when (state) {
            HomeViewState.Loading -> LoadingStateView()
            HomeViewState.Empty -> EmptyView()
            is HomeViewState.Content -> ContentView(state, onAction)
        }
    }
}

@Preview
@Composable
fun HomeEmptyViewPreview() {
    RecipeTheme {
        HomeView(HomeViewState.Empty)
    }
}

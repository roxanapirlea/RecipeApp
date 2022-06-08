package com.roxana.recipeapp.home

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R
import com.roxana.recipeapp.home.ui.EmptyView
import com.roxana.recipeapp.home.ui.FiltersView
import com.roxana.recipeapp.home.ui.RecipeListView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.AddIcon
import com.roxana.recipeapp.ui.LoadingStateView
import com.roxana.recipeapp.ui.SettingsIcon
import com.roxana.recipeapp.ui.SlotAppBar
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun HomeDestination(
    homeViewModel: HomeViewModel,
    onNavDetail: (Int) -> Unit,
    onNavAddRecipe: () -> Unit = {},
    onNavSettings: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(homeViewModel.state)
        .collectAsState(HomeViewState.Loading)

    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext

    LaunchedEffect(homeViewModel.sideEffectFlow) {
        homeViewModel.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                ItemsFetchingError -> scaffoldState.snackbarHostState.showSnackbar(
                    message = localContext.getString(R.string.home_recipe_fetch_error),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    HomeScreen(
        state,
        scaffoldState,
        onSettingsClicked = onNavSettings,
        onAddRecipeClicked = onNavAddRecipe,
        onRecipeSelected = onNavDetail,
        onFiltersClicked = homeViewModel::onFiltersClicked,
        onCategoryClicked = homeViewModel::onCategoryClicked,
        onTotalTimeSelected = homeViewModel::onTotalTimeSelected,
        onPreparationTimeSelected = homeViewModel::onPreparationTimeSelected,
        onCookingTimeSelected = homeViewModel::onCookingTimeSelected,
        onResetFiltersClicked = homeViewModel::onResetFiltersClicked,
        onCloseFiltersClicked = homeViewModel::onCloseFiltersClicked,
        onSearchQueryModified = homeViewModel::onQueryModified
    )
}

@Composable
fun HomeScreen(
    state: HomeViewState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onSettingsClicked: () -> Unit = {},
    onAddRecipeClicked: () -> Unit = {},
    onFiltersClicked: () -> Unit = {},
    onRecipeSelected: (Int) -> Unit = {},
    onCategoryClicked: (UiCategoryType) -> Unit = {},
    onTotalTimeSelected: (Int) -> Unit = {},
    onPreparationTimeSelected: (Int) -> Unit = {},
    onCookingTimeSelected: (Int) -> Unit = {},
    onResetFiltersClicked: () -> Unit = {},
    onCloseFiltersClicked: () -> Unit = {},
    onSearchQueryModified: (String) -> Unit = {}
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SlotAppBar(
                title = stringResource(R.string.home_title),
                actions = { IconButton(onClick = onSettingsClicked) { SettingsIcon() } }
            )
        },
        floatingActionButton = {
            if ((state as? HomeViewState.Content)?.showFilters != true)
                FloatingActionButton(onClick = onAddRecipeClicked) { AddIcon() }
        }
    ) {
        when (state) {
            HomeViewState.Loading -> LoadingStateView()
            HomeViewState.Empty -> EmptyView()
            is HomeViewState.Content -> {
                if (state.showFilters)
                    FiltersView(
                        state = state.filtersState,
                        onCategoryClicked = onCategoryClicked,
                        onTotalTimeSelected = onTotalTimeSelected,
                        onPreparationTimeSelected = onPreparationTimeSelected,
                        onCookingTimeSelected = onCookingTimeSelected,
                        onResetFiltersClicked = onResetFiltersClicked,
                        onCloseFiltersClicked = onCloseFiltersClicked,
                    )
                else
                    RecipeListView(
                        state = state,
                        onFiltersClicked = onFiltersClicked,
                        onSearchQueryModified = onSearchQueryModified,
                        onRecipeSelected = onRecipeSelected
                    )
            }
        }
    }
}

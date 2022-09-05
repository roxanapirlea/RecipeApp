package com.roxana.recipeapp.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.home.ui.EmptyView
import com.roxana.recipeapp.home.ui.FiltersView
import com.roxana.recipeapp.home.ui.RecipeListView
import com.roxana.recipeapp.ui.AddIcon
import com.roxana.recipeapp.ui.BackIcon
import com.roxana.recipeapp.ui.LoadingStateView
import com.roxana.recipeapp.ui.SettingsIcon
import com.roxana.recipeapp.ui.basecomponents.AppBar
import com.roxana.recipeapp.uimodel.UiCategoryType

@Composable
fun HomeDestination(
    homeViewModel: HomeViewModel,
    onNavDetail: (Int) -> Unit,
    onNavAddRecipe: () -> Unit = {},
    onNavSettings: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(homeViewModel.state)
        .collectAsState(HomeViewState(isLoading = true))

    val snackbarHostState = remember { SnackbarHostState() }
    val localContext = LocalContext.current.applicationContext

    if (state.isFetchingError) {
        LaunchedEffect(state.isFetchingError) {
            snackbarHostState.showSnackbar(
                message = localContext.getString(R.string.home_recipe_fetch_error),
                duration = SnackbarDuration.Short
            )
            homeViewModel.onErrorDismissed()
        }
    }
    state.randomRecipeId?.let {
        LaunchedEffect(state.randomRecipeId) {
            onNavDetail(it)
            homeViewModel.onNavToRandomRecipe()
        }
    }

    HomeScreen(
        state,
        snackbarHostState,
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
        onSearchQueryModified = homeViewModel::onQueryModified,
        onRandomRecipe = homeViewModel::onRandomRecipe,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeViewState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
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
    onSearchQueryModified: (String) -> Unit = {},
    onRandomRecipe: () -> Unit = {},
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppBar(
                title = stringResource(R.string.home_title),
                actions = {
                    if (!state.showFilters)
                        IconButton(onClick = onSettingsClicked) { SettingsIcon() }
                },
                icon = if (state.showFilters) { { BackIcon() } } else null,
                onIconClick = onCloseFiltersClicked
            )
        },
        floatingActionButton = {
            if (!state.showFilters)
                FloatingActionButton(onClick = onAddRecipeClicked) { AddIcon() }
        }
    ) { contentPadding ->
        when {
            state.isLoading -> LoadingStateView(Modifier.padding(contentPadding))
            state.isEmpty -> EmptyView(Modifier.padding(contentPadding))
            else -> {
                if (state.showFilters)
                    FiltersView(
                        state = state.filtersState,
                        modifier = Modifier.padding(contentPadding),
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
                        modifier = Modifier.padding(contentPadding),
                        onFiltersClicked = onFiltersClicked,
                        onSearchQueryModified = onSearchQueryModified,
                        onRecipeSelected = onRecipeSelected,
                        onRandomRecipe = onRandomRecipe
                    )
            }
        }
    }
}

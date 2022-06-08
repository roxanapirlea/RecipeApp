package com.roxana.recipeapp.detail

import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R
import com.roxana.recipeapp.detail.ui.RecipeDetailView
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
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
        .collectAsState(DetailViewState.Loading)

    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(detailViewModel.sideEffectFlow) {
        detailViewModel.sideEffectFlow.collect {
            when (it) {
                StartEditing -> onNavEdit()
            }
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
    ) {
        when (state) {
            DetailViewState.Loading -> LoadingStateView()
            is DetailViewState.Content -> RecipeDetailView(
                state = state,
                onStartCookingClicked = onStartCookingClicked,
                onAddCommentClicked = onAddCommentClicked,
                onEditClicked = onEditClicked
            )
        }
    }
}

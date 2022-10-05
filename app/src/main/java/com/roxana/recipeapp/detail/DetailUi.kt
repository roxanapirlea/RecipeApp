package com.roxana.recipeapp.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.detail.ui.RecipeDetailView
import com.roxana.recipeapp.ui.LoadingStateView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack
import com.roxana.recipeapp.ui.basecomponents.ExtendedTextIconFab

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

    val snackbarHostState = remember { SnackbarHostState() }
    val localContext = LocalContext.current.applicationContext

    if (state.isFetchingError) {
        LaunchedEffect(state.isFetchingError) {
            snackbarHostState.showSnackbar(
                message = localContext.getString(R.string.cooking_fetch_error),
                duration = SnackbarDuration.Short
            )
            detailViewModel.onErrorDismissed()
        }
    }
    state.navigation?.let { navigation ->
        LaunchedEffect(navigation) {
            when (navigation) {
                Navigation.EDIT -> onNavEdit()
                Navigation.BACK -> onNavBack()
            }
            detailViewModel.onNavigationDone()
        }
    }
    if (state.shouldShowDeleteMessage) {
        LaunchedEffect(state.shouldShowDeleteMessage) {
            val result = snackbarHostState.showSnackbar(
                message = localContext.getString(R.string.detail_recipe_deleted),
                duration = SnackbarDuration.Short,
                actionLabel = localContext.getString(R.string.all_undo)
            )
            when (result) {
                SnackbarResult.Dismissed -> detailViewModel.onDeleteMessageDismissed()
                SnackbarResult.ActionPerformed -> detailViewModel.onUndoDelete()
            }
        }
    }

    DetailScreen(
        state,
        snackbarHostState,
        onStartCookingClicked = onNavStartCooking,
        onBackClicked = onNavBack,
        onAddCommentClicked = onNavAddComment,
        onEditClicked = detailViewModel::onEdit,
        onDeleteClicked = detailViewModel::onDelete,
        onEditComments = detailViewModel::onEditComments,
        onDoneEditComments = detailViewModel::onDoneEditComments,
        onDeleteComment = detailViewModel::onDeleteComment,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: DetailViewState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClicked: () -> Unit = {},
    onStartCookingClicked: () -> Unit = {},
    onAddCommentClicked: () -> Unit = {},
    onEditClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    onEditComments: () -> Unit = {},
    onDeleteComment: (Int) -> Unit = {},
    onDoneEditComments: () -> Unit = {},
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppBarBack(
                title = state.title,
                onIconClick = onBackClicked,
                actions = {
                    AppBarActions(
                        showMenu = showMenu,
                        onDeleteClicked = onDeleteClicked,
                        onEditClicked = onEditClicked,
                        shouldShowMenu = { showMenu = it }
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedTextIconFab(
                onClick = onStartCookingClicked,
                text = { Text(stringResource(R.string.detail_start_cooking)) },
                leadingIcon = { Icon(Icons.Rounded.ArrowForward, contentDescription = null) }
            )
        }
    ) { contentPadding ->
        when {
            state.isLoading -> LoadingStateView(Modifier.padding(contentPadding))
            else -> RecipeDetailView(
                state = state,
                modifier = Modifier.padding(contentPadding),
                onAddComment = onAddCommentClicked,
                onEditComments = onEditComments,
                onDoneEditComments = onDoneEditComments,
                onDeleteComment = onDeleteComment,
            )
        }
    }
}

@Composable
fun AppBarActions(
    showMenu: Boolean,
    onDeleteClicked: () -> Unit = {},
    onEditClicked: () -> Unit = {},
    shouldShowMenu: (Boolean) -> Unit = {}
) {
    IconButton(onClick = { shouldShowMenu(!showMenu) }) {
        Icon(
            Icons.Rounded.MoreVert,
            contentDescription = stringResource(R.string.all_more_actions)
        )
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { shouldShowMenu(false) }
    ) {
        DropdownMenuItem(
            onClick = onEditClicked,
            text = {
                Text(stringResource(R.string.all_edit), Modifier.padding(top = 2.dp, end = 16.dp))
            },
            leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null) }
        )
        DropdownMenuItem(
            onClick = onDeleteClicked,
            text = {
                Text(stringResource(R.string.all_delete), Modifier.padding(top = 2.dp, end = 16.dp))
            },
            leadingIcon = { Icon(Icons.Rounded.Delete, contentDescription = null) }
        )
    }
}

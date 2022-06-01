package com.roxana.recipeapp.edit.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.ForwardIcon
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.DividerAlpha16
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.TrailingIconTextField
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun EditRecipeCommentsScreen(
    commentsViewModel: EditRecipeCommentsViewModel,
    onBack: () -> Unit = {},
    onForwardCreationMode: () -> Unit = {},
    onForwardEditingMode: () -> Unit = {},
    onNavigate: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(commentsViewModel.state)
        .collectAsState(EditRecipeCommentsViewState())

    AddRecipeCommentsView(
        state,
        commentsViewModel.sideEffectFlow,
        onCommentChanged = commentsViewModel::onCommentChanged,
        onSaveComment = commentsViewModel::onSaveComment,
        onCommentDone = commentsViewModel::onCommentDone,
        onDelete = commentsViewModel::onDeleteComment,
        onValidate = commentsViewModel::onValidate,
        onSaveAndGoBack = commentsViewModel::onSaveAndBack,
        onBackNavigation = onBack,
        onForwardNavigationForCreation = onForwardCreationMode,
        onForwardNavigationForEditing = onForwardEditingMode,
        onSelectPage = commentsViewModel::onSelectPage,
        onNavigateToPage = onNavigate
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecipeCommentsView(
    state: EditRecipeCommentsViewState,
    sideEffectsFlow: Flow<EditRecipeCommentsSideEffect> = flow { },
    onCommentChanged: (String) -> Unit = {},
    onCommentDone: () -> Unit = {},
    onSaveComment: () -> Unit = {},
    onDelete: (Int) -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onNavigateToPage: (PageType) -> Unit = {},
    onBackNavigation: () -> Unit = {},
    onForwardNavigationForCreation: () -> Unit = {},
    onForwardNavigationForEditing: () -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(sideEffectsFlow) {
        sideEffectsFlow.collect {
            when (it) {
                ForwardForCreation -> onForwardNavigationForCreation()
                ForwardForEditing -> onForwardNavigationForEditing()
                Back -> onBackNavigation()
                is NavigateToPage -> onNavigateToPage(it.page)
            }
        }
    }

    EditRecipeBackdrop(
        recipeAlreadyExists = state.isExistingRecipe,
        selectedPage = PageType.Comments,
        backIcon = Icons.Default.ArrowBack,
        backContentDescription = stringResource(R.string.all_back),
        onSelectPage = onSelectPage,
        onBack = onSaveAndGoBack,
    ) {
        Box(Modifier.fillMaxSize()) {
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                onClick = onValidate
            ) { ForwardIcon() }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.weight(3f))
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.ic_instructions),
                        contentDescription = null,
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier.size(40.dp)
                    )
                    LabelView(text = stringResource(R.string.all_comments))
                }
                Spacer(modifier = Modifier.weight(0.5f))
                TrailingIconTextField(
                    value = state.editingComment,
                    onValueChange = onCommentChanged,
                    label = stringResource(R.string.edit_recipe_comment_hint),
                    trailingIcon = R.drawable.ic_check_outline,
                    onTrailingIconClicked = onSaveComment,
                    focusRequester = focusRequester,
                    imeAction = ImeAction.Done,
                    onImeAction = onCommentDone,
                    modifier = Modifier
                )
                if (state.comments.isNotEmpty())
                    DividerAlpha16(Modifier.padding(bottom = 4.dp, top = 16.dp))
                LazyColumn {
                    itemsIndexed(state.comments) { index, instruction ->
                        CommentText(instruction, index + 1, Modifier.fillMaxWidth()) {
                            onDelete(index)
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(6f))
            }
        }
    }
}

@Composable
fun CommentText(
    comment: String,
    index: Int,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$index",
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Text(
            text = comment,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground,
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .weight(3f)
        )
        Icon(
            Icons.Default.Delete,
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_save),
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable { onDelete() }
                .padding(12.dp)
                .size(32.dp)
        )
    }
}

@Preview
@Composable
fun AddRecipeInstructionsViewPreview() {
    RecipeTheme {
        AddRecipeCommentsView(EditRecipeCommentsViewState())
    }
}

package com.roxana.recipeapp.add.portions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.AddRecipeBackdrop
import com.roxana.recipeapp.add.ForwardIcon
import com.roxana.recipeapp.add.PageType
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.RecipeTextField
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun AddRecipePortionsScreen(
    addRecipePortionsViewModel: AddRecipePortionsViewModel,
    onBack: () -> Unit = {},
    onForward: () -> Unit = {},
    onNavigate: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(addRecipePortionsViewModel.state)
        .collectAsState(AddRecipePortionsViewState())

    AddRecipePortionsView(
        state,
        addRecipePortionsViewModel.sideEffectFlow,
        onPortionsChanged = addRecipePortionsViewModel::onPortionsChanged,
        onValidate = addRecipePortionsViewModel::onValidate,
        onSaveAndGoBack = addRecipePortionsViewModel::onSaveAndBack,
        onBackNavigation = onBack,
        onForwardNavigation = onForward,
        onSelectPage = addRecipePortionsViewModel::onSelectPage,
        onNavigateToPage = onNavigate
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecipePortionsView(
    state: AddRecipePortionsViewState,
    sideEffectsFlow: Flow<AddRecipePortionsSideEffect> = flow { },
    onPortionsChanged: (String) -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onNavigateToPage: (PageType) -> Unit = {},
    onBackNavigation: () -> Unit = {},
    onForwardNavigation: () -> Unit = {},
    onValidate: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(sideEffectsFlow) {
        sideEffectsFlow.collect {
            when (it) {
                Forward -> onForwardNavigation()
                Back -> onBackNavigation()
                is NavigateToPage -> onNavigateToPage(it.page)
            }
        }
    }

    AddRecipeBackdrop(
        selectedPage = PageType.Portions,
        backIcon = Icons.Default.ArrowBack,
        backContentDescription = stringResource(R.string.all_back),
        onSelectPage = onSelectPage,
        onBack = onSaveAndGoBack,
    ) {
        Box(Modifier.fillMaxSize()) {
            if (state.isValid())
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onClick = onValidate
                ) { ForwardIcon() }

            AddRecipePortionsContent(
                state = state,
                focusRequester = focusRequester,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                onPortionsChanged = onPortionsChanged,
                onValidate = onValidate
            )
        }
    }
}

@Composable
fun AddRecipePortionsContent(
    state: AddRecipePortionsViewState,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    onPortionsChanged: (String) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(3f))
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.ic_portions),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(40.dp)
            )
            LabelView(text = stringResource(R.string.add_recipe_portions_label))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        RecipeTextField(
            value = state.portions,
            onValueChange = onPortionsChanged,
            placeholder = stringResource(R.string.add_recipe_portions_hint),
            imeAction = ImeAction.Done,
            onImeAction = onValidate,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
        Spacer(modifier = Modifier.weight(6f))
    }
}

@Preview
@Composable
fun AddRecipePortionsViewPreview() {
    RecipeTheme {
        AddRecipePortionsView(AddRecipePortionsViewState())
    }
}

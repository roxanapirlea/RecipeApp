package com.roxana.recipeapp.edit.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.EditRecipeBackdrop
import com.roxana.recipeapp.edit.ForwardIcon
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.SelectableCategory
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiCategoryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun EditRecipeCategoriesScreen(
    editRecipeCategoriesViewModel: EditRecipeCategoriesViewModel,
    onBack: () -> Unit = {},
    onForwardCreationMode: () -> Unit = {},
    onForwardEditingMode: () -> Unit = {},
    onNavigate: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(editRecipeCategoriesViewModel.state)
        .collectAsState(EditRecipeCategoriesViewState())

    AddRecipeCategoriesView(
        state,
        editRecipeCategoriesViewModel.sideEffectFlow,
        onCategoryClicked = editRecipeCategoriesViewModel::onCategoryClicked,
        onValidate = editRecipeCategoriesViewModel::onValidate,
        onSaveAndGoBack = editRecipeCategoriesViewModel::onSaveAndBack,
        onBackNavigation = onBack,
        onForwardNavigationForCreation = onForwardCreationMode,
        onForwardNavigationForEditing = onForwardEditingMode,
        onSelectPage = editRecipeCategoriesViewModel::onSelectPage,
        onNavigateToPage = onNavigate
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecipeCategoriesView(
    state: EditRecipeCategoriesViewState,
    sideEffectsFlow: Flow<EditRecipeCategoriesSideEffect> = flow { },
    onCategoryClicked: (UiCategoryType) -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onNavigateToPage: (PageType) -> Unit = {},
    onBackNavigation: () -> Unit = {},
    onForwardNavigationForCreation: () -> Unit = {},
    onForwardNavigationForEditing: () -> Unit = {},
    onValidate: () -> Unit = {},
) {
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
        selectedPage = PageType.Categories,
        backIcon = Icons.Default.ArrowBack,
        backContentDescription = stringResource(R.string.all_back),
        onSelectPage = onSelectPage,
        onBack = onSaveAndGoBack,
    ) {
        Box(Modifier.fillMaxSize()) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = onValidate
            ) { ForwardIcon() }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Spacer(
                    modifier = Modifier.weight(
                        3f
                    )
                )
                LabelView(
                    text = stringResource(R.string.edit_recipe_categories_label),
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Spacer(
                    modifier = Modifier.weight(
                        0.5f
                    )
                )
                FlowRow(mainAxisAlignment = FlowMainAxisAlignment.Center) {
                    state.categories.forEachIndexed { index, categoryState ->
                        SelectableCategory(
                            categoryType = categoryState.categoryType,
                            isSelected = categoryState.isSelected,
                            index = index,
                            onCategoryClicked = onCategoryClicked
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.weight(
                        6f
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun AddRecipeCategoriesScreenPreview() {
    RecipeTheme {
        AddRecipeCategoriesView(EditRecipeCategoriesViewState())
    }
}
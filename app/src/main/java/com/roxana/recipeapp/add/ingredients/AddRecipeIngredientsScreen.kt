package com.roxana.recipeapp.add.ingredients

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.AddRecipeBackdrop
import com.roxana.recipeapp.add.ForwardIcon
import com.roxana.recipeapp.add.PageType
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.misc.toFormattedString
import com.roxana.recipeapp.ui.DividerAlpha16
import com.roxana.recipeapp.ui.LabelView
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun AddRecipeIngredientsScreen(
    ingredientsViewModel: AddRecipeIngredientsViewModel,
    onBack: () -> Unit = {},
    onForward: () -> Unit = {},
    onNavigate: (PageType) -> Unit = {},
) {
    val state by rememberFlowWithLifecycle(ingredientsViewModel.state)
        .collectAsState(AddRecipeIngredientsViewState())

    AddRecipeIngredientsView(
        state,
        ingredientsViewModel.sideEffectFlow,
        onIngredientNameChanged = ingredientsViewModel::onIngredientNameChanged,
        onIngredientQuantityChanged = ingredientsViewModel::onIngredientQuantityChanged,
        onIngredientQuantityTypeChanged = ingredientsViewModel::onIngredientQuantityTypeChanged,
        onSaveIngredient = ingredientsViewModel::onSaveIngredient,
        onDelete = ingredientsViewModel::onDeleteIngredient,
        onValidate = ingredientsViewModel::onValidate,
        onSaveAndGoBack = ingredientsViewModel::onSaveAndBack,
        onBackNavigation = onBack,
        onForwardNavigation = onForward,
        onSelectPage = ingredientsViewModel::onSelectPage,
        onNavigateToPage = onNavigate
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecipeIngredientsView(
    state: AddRecipeIngredientsViewState,
    sideEffectsFlow: Flow<AddRecipeIngredientsSideEffect> = flow { },
    onIngredientNameChanged: (String) -> Unit = {},
    onIngredientQuantityChanged: (String) -> Unit = {},
    onIngredientQuantityTypeChanged: (UiQuantityType) -> Unit = {},
    onSaveIngredient: () -> Unit = {},
    onDelete: (Int) -> Unit = {},
    onSaveAndGoBack: () -> Unit = {},
    onSelectPage: (PageType) -> Unit = {},
    onNavigateToPage: (PageType) -> Unit = {},
    onBackNavigation: () -> Unit = {},
    onForwardNavigation: () -> Unit = {},
    onValidate: () -> Unit = {},
) {
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
        selectedPage = PageType.Ingredients,
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

            AddRecipeIngredientsContent(
                state = state,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                onIngredientNameChanged = onIngredientNameChanged,
                onIngredientQuantityChanged = onIngredientQuantityChanged,
                onIngredientQuantityTypeChanged = onIngredientQuantityTypeChanged,
                onSaveIngredient = onSaveIngredient,
                onDelete = onDelete,
            )
        }
    }
}

@Composable
fun AddRecipeIngredientsContent(
    state: AddRecipeIngredientsViewState,
    modifier: Modifier = Modifier,
    onIngredientNameChanged: (String) -> Unit = {},
    onIngredientQuantityChanged: (String) -> Unit = {},
    onIngredientQuantityTypeChanged: (UiQuantityType) -> Unit = {},
    onSaveIngredient: () -> Unit = {},
    onDelete: (Int) -> Unit = {},
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
                painterResource(R.drawable.ic_ingredients),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(40.dp)
            )
            LabelView(text = stringResource(R.string.all_ingredients))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        IngredientTextField(
            ingredient = state.editingIngredient,
            modifier = Modifier.fillMaxWidth(),
            quantityTypes = state.quantityTypes,
            onIngredientChange = onIngredientNameChanged,
            onQuantityChange = onIngredientQuantityChanged,
            onTypeChange = onIngredientQuantityTypeChanged,
            onSave = onSaveIngredient
        )
        if (state.ingredients.isNotEmpty())
            DividerAlpha16(Modifier.padding(bottom = 4.dp, top = 16.dp))
        LazyColumn {
            itemsIndexed(state.ingredients) { index, state ->
                IngredientText(
                    state,
                    Modifier.fillMaxWidth()
                ) { onDelete(index) }
            }
        }
        Spacer(modifier = Modifier.weight(6f))
    }
}

@Composable
fun IngredientText(
    ingredient: IngredientState,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
) {
    val quantity = ingredient.quantity.toDoubleOrNull()?.toFormattedString() ?: ""
    val quantityType = stringResource(ingredient.quantityType.textForSelected)
    val formattedIngredient =
        stringResource(
            R.string.all_ingredient_placeholders,
            quantity,
            quantityType,
            ingredient.name
        )
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = formattedIngredient,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground,
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .weight(3f)
        )
        Icon(
            painterResource(R.drawable.ic_cross),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_delete),
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
fun AddRecipeIngredientsViewPreview() {
    RecipeTheme {
        AddRecipeIngredientsView(AddRecipeIngredientsViewState())
    }
}

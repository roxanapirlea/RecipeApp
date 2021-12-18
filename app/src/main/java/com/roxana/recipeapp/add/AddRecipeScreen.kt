package com.roxana.recipeapp.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.ui.AddButton
import com.roxana.recipeapp.add.ui.AddRecipeTextField
import com.roxana.recipeapp.add.ui.IndexedView
import com.roxana.recipeapp.add.ui.IngredientView
import com.roxana.recipeapp.add.ui.TimeTextField
import com.roxana.recipeapp.misc.rememberFlowWithLifecycle
import com.roxana.recipeapp.misc.toStringRes
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.CategoryChip
import com.roxana.recipeapp.ui.DividerAlpha16
import com.roxana.recipeapp.ui.DividerAlpha40
import com.roxana.recipeapp.ui.RecipePartLabel
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

@Composable
fun AddRecipeScreen(
    addRecipeViewModel: AddRecipeViewModel,
    onBack: () -> Unit = {}
) {
    val state by rememberFlowWithLifecycle(addRecipeViewModel.state)
        .collectAsState(AddRecipeViewState())

    AddRecipeView(state, addRecipeViewModel.eventsFlow) {
        when (it) {
            Back -> onBack()
            is TitleChanged -> addRecipeViewModel.onTitleChanged(it.name)
            is CategoryClicked -> addRecipeViewModel.onCategoryClicked(it.type)
            is PortionsChanged -> addRecipeViewModel.onPortionsChanged(it.portions)
            AddIngredientClicked -> addRecipeViewModel.onAddIngredient()
            is IngredientClicked -> addRecipeViewModel.onIngredientClicked(it.id)
            is DeleteIngredientClicked -> addRecipeViewModel.onDeleteIngredient(it.id)
            is IngredientNameChanged -> addRecipeViewModel.onIngredientNameChanged(it.id, it.name)
            is IngredientQuantityChanged ->
                addRecipeViewModel.onIngredientQuantityChanged(it.id, it.quantity)
            is IngredientQuantityTypeChanged ->
                addRecipeViewModel.onIngredientQuantityTypeChanged(it.id, it.quantityType)
            AddInstructionClicked -> addRecipeViewModel.onAddInstruction()
            is InstructionClicked -> addRecipeViewModel.onInstructionClicked(it.id)
            is DeleteInstructionClicked -> addRecipeViewModel.onDeleteInstruction(it.id)
            is InstructionChanged -> addRecipeViewModel.onInstructionChanged(it.id, it.name)
            AddCommentClicked -> addRecipeViewModel.onAddComment()
            is CommentClicked -> addRecipeViewModel.onCommentClicked(it.id)
            is DeleteCommentClicked -> addRecipeViewModel.onDeleteComment(it.id)
            is CommentChanged -> addRecipeViewModel.onCommentChanged(it.id, it.name)
            is TimeCookingChanged -> addRecipeViewModel.onTimeCookingChanged(it.time)
            is TimePreparationChanged -> addRecipeViewModel.onTimePreparationChanged(it.time)
            is TimeTotalChanged -> addRecipeViewModel.onTimeTotalChanged(it.time)
            is TimeWaitingChanged -> addRecipeViewModel.onTimeWaitingChanged(it.time)
            ComputeTotal -> addRecipeViewModel.computeTotal()
            is TemperatureChanged -> addRecipeViewModel.onTemperatureChanged(it.temperature)
            Validate -> addRecipeViewModel.onValidate()
        }
    }
}

@Composable
fun AddRecipeView(
    state: AddRecipeViewState,
    eventsFlow: Flow<AddRecipeEvent> = flow { },
    onAction: (AddRecipeViewAction) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext
    val portionsFocusRequester = remember { FocusRequester() }
    val ingredientNameFocusRequester = remember { FocusRequester() }
    val ingredientQuantityFocusRequester = remember { FocusRequester() }
    val timeCookingFocusRequester = remember { FocusRequester() }
    val timePreparationFocusRequester = remember { FocusRequester() }
    val timeWaitingFocusRequester = remember { FocusRequester() }
    val timeTotalFocusRequester = remember { FocusRequester() }
    val temperatureFocusRequester = remember { FocusRequester() }
    val commentFocusRequester = remember { FocusRequester() }

    LaunchedEffect(eventsFlow) {
        eventsFlow.collect {
            when (it) {
                ShowCategoryError ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = localContext.getString(R.string.add_recipe_category_error),
                        duration = SnackbarDuration.Short
                    )
                ShowQuantityError ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = localContext.getString(R.string.add_recipe_quantity_error),
                        duration = SnackbarDuration.Short
                    )
                SaveRecipeError -> scaffoldState.snackbarHostState.showSnackbar(
                    message = localContext.getString(R.string.add_recipe_save_error),
                    duration = SnackbarDuration.Short
                )
                SaveRecipeSuccess -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = localContext.getString(R.string.add_recipe_save_success),
                        duration = SnackbarDuration.Short
                    )
                    onAction(Back)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                title = stringResource(id = R.string.add_title),
                icon = R.drawable.ic_arrow_back
            ) { onAction(Back) }
        },
        floatingActionButton = {
            if (state.isValid)
                FloatingActionButton(onClick = { onAction(Validate) }) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = stringResource(R.string.add_recipe_done_description)
                    )
                }
        }
    ) { innerPadding ->
        val padding = PaddingValues(
            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
        )

        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 8.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                AddRecipeTextField(
                    state = state.title,
                    onValueChange = { onAction(TitleChanged(it)) },
                    placeholder = stringResource(id = R.string.add_recipe_title_hint),
                    textStyle = MaterialTheme.typography.h5,
                    imeAction = ImeAction.Next,
                    onImeAction = { portionsFocusRequester.requestFocus() },
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxWidth()
                )
            }
            item { DividerAlpha40() }

            item {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = padding
                ) {
                    items(state.categories) { category ->
                        CategoryChip(
                            text = stringResource(category.type.toStringRes()),
                            isActivated = category.isSelected,
                            onClick = { onAction(CategoryClicked(category.type)) }
                        )
                    }
                }
            }

            if (state.categories.isNotEmpty())
                item { DividerAlpha40(modifier = Modifier.padding(top = 8.dp)) }

            item {
                AddRecipeTextField(
                    state = state.portions,
                    onValueChange = { onAction(PortionsChanged(it)) },
                    placeholder = stringResource(id = R.string.add_recipe_portions_hint),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    onImeAction = { ingredientNameFocusRequester.requestFocus() },
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxWidth()
                        .focusRequester(portionsFocusRequester)
                )
            }
            item { DividerAlpha40() }

            item {
                RecipePartLabel(
                    text = stringResource(id = R.string.add_recipe_ingredients),
                    image = R.drawable.ic_ingredients,
                    modifier = Modifier.padding(padding)
                )
            }
            itemsIndexed(state.ingredients) { index, ingredient ->
                Column(Modifier.padding(padding)) {
                    DividerAlpha16()
                    IngredientView(
                        ingredient = ingredient,
                        quantityTypes = state.quantities,
                        onIngredientChange = { onAction(IngredientNameChanged(index, it)) },
                        onQuantityChange = { onAction(IngredientQuantityChanged(index, it)) },
                        onTypeChange = { onAction(IngredientQuantityTypeChanged(index, it)) },
                        onDelete = { onAction(DeleteIngredientClicked(index)) },
                        onSelect = { onAction(IngredientClicked(index)) },
                        nameFocusRequester = ingredientNameFocusRequester,
                        quantityFocusRequester = ingredientQuantityFocusRequester
                    )
                }
            }

            if (state.ingredients.isNotEmpty())
                item { DividerAlpha16(modifier = Modifier.padding(padding)) }

            item {
                AddButton(
                    onClick = { onAction(AddIngredientClicked) },
                    modifier = Modifier.padding(padding)
                )
            }
            item { DividerAlpha40(modifier = Modifier.padding(top = 16.dp)) }

            item {
                RecipePartLabel(
                    text = stringResource(id = R.string.add_recipe_instructions),
                    image = R.drawable.ic_instructions,
                    modifier = Modifier.padding(padding)
                )
            }
            itemsIndexed(state.instructions) { index, instruction ->
                Column(Modifier.padding(padding)) {
                    DividerAlpha16()
                    IndexedView(
                        editableState = instruction,
                        index = index + 1,
                        placeholder = stringResource(R.string.add_recipe_instruction_hint),
                        onNameChange = { onAction(InstructionChanged(index, it)) },
                        onDelete = { onAction(DeleteInstructionClicked(index)) },
                        onSelect = { onAction(InstructionClicked(index)) },
                        imeAction = ImeAction.Next,
                        onImeAction = { onAction(AddInstructionClicked) }
                    )
                }
            }
            if (state.instructions.isNotEmpty())
                item { DividerAlpha16(modifier = Modifier.padding(padding)) }

            item {
                AddButton(
                    onClick = { onAction(AddInstructionClicked) },
                    modifier = Modifier.padding(padding)
                )
            }
            item { DividerAlpha40(modifier = Modifier.padding(top = 8.dp)) }

            item {
                TimeTextField(
                    time = state.time,
                    onTimeCookingSet = { onAction(TimeCookingChanged(it)) },
                    onTimePreparationSet = { onAction(TimePreparationChanged(it)) },
                    onTimeWaitingSet = { onAction(TimeWaitingChanged(it)) },
                    onTimeTotalSet = { onAction(TimeTotalChanged(it)) },
                    onComputeTotal = { onAction(ComputeTotal) },
                    totalImeAction = ImeAction.Next,
                    onTotalTimeImeAction = { temperatureFocusRequester.requestFocus() },
                    cookingFocusRequester = timeCookingFocusRequester,
                    preparationFocusRequester = timePreparationFocusRequester,
                    waitingFocusRequester = timeWaitingFocusRequester,
                    totalFocusRequester = timeTotalFocusRequester,
                    modifier = Modifier.padding(padding)
                )
            }
            item { DividerAlpha40() }

            item {
                AddRecipeTextField(
                    state = state.temperature,
                    onValueChange = { onAction(TemperatureChanged(it)) },
                    placeholder = stringResource(R.string.add_recipe_temperature_hint),
                    leading = {
                        Icon(painterResource(R.drawable.ic_temperature), null)
                    },
                    keyboardType = KeyboardType.Number,
                    textStyle = MaterialTheme.typography.body1,
                    imeAction = ImeAction.Next,
                    onImeAction = { commentFocusRequester.requestFocus() },
                    modifier = Modifier
                        .padding(padding)
                        .defaultMinSize(0.dp, 0.dp)
                        .focusRequester(temperatureFocusRequester)
                )
            }
            item { DividerAlpha40() }

            item {
                RecipePartLabel(
                    text = stringResource(id = R.string.add_recipe_comments),
                    image = R.drawable.ic_comment,
                    modifier = Modifier.padding(padding)
                )
            }
            itemsIndexed(state.comments) { index, comment ->
                Column(Modifier.padding(padding)) {
                    DividerAlpha16()
                    IndexedView(
                        editableState = comment,
                        index = index + 1,
                        placeholder = stringResource(R.string.add_recipe_comment_hint),
                        onNameChange = { onAction(CommentChanged(index, it)) },
                        onDelete = { onAction(DeleteCommentClicked(index)) },
                        onSelect = { onAction(CommentClicked(index)) },
                        imeAction = ImeAction.Next,
                        onImeAction = { onAction(AddCommentClicked) },
                        focusRequester = commentFocusRequester
                    )
                }
            }
            if (state.comments.isNotEmpty())
                item { DividerAlpha16(modifier = Modifier.padding(padding)) }

            item {
                AddButton(
                    onClick = { onAction(AddCommentClicked) },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Preview
@Composable
fun AddRecipeScreenPreview() {
    RecipeTheme {
        AddRecipeView(AddRecipeViewState())
    }
}

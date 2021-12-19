package com.roxana.recipeapp.add

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.addrecipe.AddRecipeUseCase
import com.roxana.recipeapp.domain.addrecipe.GetAvailableCategoriesUseCase
import com.roxana.recipeapp.domain.addrecipe.GetAvailableQuantityTypesUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.misc.toNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetAvailableCategoriesUseCase,
    private val getQuantityTypesUseCase: GetAvailableQuantityTypesUseCase,
    private val addRecipeUseCase: AddRecipeUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(
        AddRecipeViewState(
            ingredients = listOf(IngredientState(isEditing = true)),
            instructions = listOf(EditingState(isEditing = true)),
            comments = listOf(EditingState(isEditing = true))
        )
    )
    val state: StateFlow<AddRecipeViewState> = _state.asStateFlow()

    private val eventChannel = Channel<AddRecipeEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val availableCategories = getCategoriesUseCase(null)
                .getOrElse {
                    eventChannel.send(ShowCategoryError)
                    emptyList()
                }
            val quantities = getQuantityTypesUseCase(null)
                .getOrElse {
                    eventChannel.send(ShowQuantityError)
                    emptyList()
                }
            val categories = availableCategories.map { CategoryState(it, false) }
            _state.emit(
                state.value.copy(
                    categories = categories,
                    quantities = listOf(null) + quantities
                )
            )
        }
    }

    fun onTitleChanged(name: String) {
        _state.value = state.value.copy(title = NonEmptyFieldState(name))
    }

    fun onCategoryClicked(type: CategoryType) {
        val categories = state.value.categories.map {
            if (it.type == type) it.copy(isSelected = !it.isSelected)
            else it
        }
        _state.value = state.value.copy(categories = categories)
    }

    fun onPortionsChanged(portions: String) {
        _state.value = state.value.copy(
            portions = ShortFieldState(
                text = portions,
                value = portions.toShortOrNull()
            )
        )
    }

    fun onAddIngredient() {
        val ingredients = state.value.ingredients
            .filterNot { it.isEditing && it.isEmpty }
            .map { it.copy(isEditing = false) }
        _state.value = state.value.copy(
            ingredients = ingredients + IngredientState(isEditing = true)
        )
    }

    fun onDeleteIngredient(id: Int) {
        val ingredients = state.value.ingredients.filterIndexed { index, _ -> index != id }
        _state.value = state.value.copy(ingredients = ingredients)
    }

    fun onIngredientClicked(id: Int) {
        val ingredients = state.value.ingredients
            .mapIndexedNotNull { index, ingredient ->
                when {
                    index == id -> ingredient.copy(isEditing = true)
                    ingredient.isEmpty && ingredient.isEditing -> null
                    else -> ingredient.copy(isEditing = false)
                }
            }
        _state.value = state.value.copy(ingredients = ingredients)
    }

    fun onIngredientNameChanged(id: Int, name: String) {
        val ingredients = state.value.ingredients
            .mapIndexed { index, ingredient ->
                if (index == id) ingredient.copy(name = EmptyFieldState(name)) else ingredient
            }
        _state.value = state.value.copy(ingredients = ingredients)
    }

    fun onIngredientQuantityChanged(id: Int, quantity: String) {
        val ingredients = state.value.ingredients
            .mapIndexed { index, ingredient ->
                if (index == id) ingredient.copy(
                    quantity = DoubleFieldState(quantity, quantity.toDoubleOrNull())
                ) else ingredient
            }
        _state.value = state.value.copy(ingredients = ingredients)
    }

    fun onIngredientQuantityTypeChanged(id: Int, quantityType: QuantityType?) {
        val shouldAddNewIngredient = with(state.value.ingredients[id]) {
            name.text.isNotEmpty() && quantity.text.isNotEmpty() && isValid
        } && state.value.ingredients.size - 1 == id
        val ingredients = state.value.ingredients
            .mapIndexed { index, ingredient ->
                if (index == id) {
                    ingredient.copy(
                        quantityType = quantityType,
                        isEditing = !shouldAddNewIngredient
                    )
                } else ingredient
            }
        _state.value = if (shouldAddNewIngredient)
            state.value.copy(
                ingredients = ingredients + IngredientState(isEditing = true)
            )
        else
            state.value.copy(ingredients = ingredients)
    }

    fun onAddInstruction() {
        val instructions = state.value.instructions
            .filterNot { it.isEditing && it.fieldState.text.isEmpty() }
            .map { it.copy(isEditing = false) }
        _state.value = state.value.copy(
            instructions = instructions + EditingState(isEditing = true)
        )
    }

    fun onDeleteInstruction(id: Int) {
        val instructions = state.value.instructions.filterIndexed { index, _ -> index != id }
        _state.value = state.value.copy(instructions = instructions)
    }

    fun onInstructionClicked(id: Int) {
        val instructions = state.value.instructions
            .mapIndexedNotNull { index, instruction ->
                when {
                    index == id -> instruction.copy(isEditing = true)
                    instruction.fieldState.text.isEmpty() && instruction.isEditing -> null
                    else -> instruction.copy(isEditing = false)
                }
            }
        _state.value = state.value.copy(instructions = instructions)
    }

    fun onInstructionChanged(id: Int, name: String) {
        val instructions = state.value.instructions
            .mapIndexed { index, instruction ->
                if (index == id)
                    instruction.copy(fieldState = EmptyFieldState(name))
                else
                    instruction
            }
        _state.value = state.value.copy(instructions = instructions)
    }

    fun onAddComment() {
        val comments = state.value.comments
            .filterNot { it.isEditing && it.fieldState.text.isEmpty() }
            .map { it.copy(isEditing = false) }
        _state.value = state.value.copy(
            comments = comments + EditingState(isEditing = true)
        )
    }

    fun onDeleteComment(id: Int) {
        val comments = state.value.comments.filterIndexed { index, _ -> index != id }
        _state.value = state.value.copy(comments = comments)
    }

    fun onCommentClicked(id: Int) {
        val comments = state.value.comments
            .mapIndexedNotNull { index, comment ->
                when {
                    index == id -> comment.copy(isEditing = true)
                    comment.fieldState.text.isEmpty() && comment.isEditing -> null
                    else -> comment.copy(isEditing = false)
                }
            }
        _state.value = state.value.copy(comments = comments)
    }

    fun onCommentChanged(id: Int, name: String) {
        val comments = state.value.comments
            .mapIndexed { index, comment ->
                if (index == id) comment.copy(fieldState = EmptyFieldState(name)) else comment
            }
        _state.value = state.value.copy(comments = comments)
    }

    fun onTimeCookingChanged(time: String) {
        _state.value = state.value.copy(
            time = state.value.time.copy(
                cooking = ShortFieldState(time, time.toShortOrNull())
            )
        )
    }

    fun onTimePreparationChanged(time: String) {
        _state.value = state.value.copy(
            time = state.value.time.copy(
                preparation = ShortFieldState(time, time.toShortOrNull())
            )
        )
    }

    fun onTimeWaitingChanged(time: String) {
        _state.value = state.value.copy(
            time = state.value.time.copy(
                waiting = ShortFieldState(time, time.toShortOrNull())
            )
        )
    }

    fun onTimeTotalChanged(time: String) {
        _state.value = state.value.copy(
            time = state.value.time.copy(
                total = ShortFieldState(time, time.toShortOrNull())
            )
        )
    }

    fun computeTotal() {
        val timeState = state.value.time
        val total = timeState.cooking.value.toNotNull(0) +
            timeState.preparation.value.toNotNull(0) +
            timeState.waiting.value.toNotNull(0)
        _state.value = state.value.copy(
            time = timeState.copy(
                total = ShortFieldState(total.toString(), total.toShort())
            )
        )
    }

    fun onTemperatureChanged(temperature: String) {
        _state.value = state.value.copy(
            temperature = ShortFieldState(
                text = temperature,
                value = temperature.toShortOrNull()
            )
        )
    }

    fun onValidate() {
        viewModelScope.launch {
            addRecipeUseCase(state.value.toRecipe()).fold(
                { eventChannel.send(SaveRecipeSuccess) },
                { eventChannel.send(SaveRecipeError) }
            )
        }
    }
}

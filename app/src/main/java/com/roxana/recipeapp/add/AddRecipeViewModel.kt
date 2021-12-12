package com.roxana.recipeapp.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.CategoryType
import com.roxana.recipeapp.domain.QuantityType
import com.roxana.recipeapp.domain.addrecipe.GetAvailableCategoriesUseCase
import com.roxana.recipeapp.domain.addrecipe.GetAvailableQuantityTypesUseCase
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
    private val getQuantityTypesUseCase: GetAvailableQuantityTypesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        AddRecipeViewState(
            ingredients = listOf(IngredientState(isEditing = true)),
            instructions = listOf(EditableState(isEditing = true)),
            comments = listOf(EditableState(isEditing = true))
        )
    )
    val state: StateFlow<AddRecipeViewState> = _state.asStateFlow()

    private val eventChannel = Channel<AddRecipeEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val availableCategories = getCategoriesUseCase.invoke(null)
                .getOrElse {
                    eventChannel.send(ShowCategoryError)
                    emptyList()
                }
            val quantities = getQuantityTypesUseCase.invoke(null)
                .getOrElse {
                    eventChannel.send(ShowQuantityError)
                    emptyList()
                }
            val categories = availableCategories.map { Category(it, false) }
            _state.emit(
                state.value.copy(
                    categories = categories,
                    quantities = quantities
                )
            )
        }
    }

    fun onTitleChanged(name: String) {
        _state.value = state.value.copy(title = Title(name, name.isNotEmpty()))
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
            portions = PortionsState(
                portions.toShortOrNull(),
                portions,
                portions.isShort()
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
                if (index == id) ingredient.copy(name = name) else ingredient
            }
        _state.value = state.value.copy(ingredients = ingredients)
    }

    fun onIngredientQuantityChanged(id: Int, quantity: String) {
        val ingredients = state.value.ingredients
            .mapIndexed { index, ingredient ->
                if (index == id) ingredient.copy(
                    quantityText = quantity,
                    quantityValue = quantity.toDoubleOrNull(),
                    isQuantityValid = quantity.isDouble()
                ) else ingredient
            }
        _state.value = state.value.copy(ingredients = ingredients)
    }

    fun onIngredientQuantityTypeChanged(id: Int, quantityType: QuantityType?) {
        val ingredients = state.value.ingredients
            .mapIndexed { index, ingredient ->
                if (index == id) ingredient.copy(quantityType = quantityType) else ingredient
            }
        _state.value = state.value.copy(ingredients = ingredients)
    }

    fun onAddInstruction() {
        val instructions = state.value.instructions
            .filterNot { it.isEditing && it.name.isEmpty() }
            .map { it.copy(isEditing = false) }
        _state.value = state.value.copy(
            instructions = instructions + EditableState(isEditing = true)
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
                    instruction.name.isEmpty() && instruction.isEditing -> null
                    else -> instruction.copy(isEditing = false)
                }
            }
        _state.value = state.value.copy(instructions = instructions)
    }

    fun onInstructionChanged(id: Int, name: String) {
        val instructions = state.value.instructions
            .mapIndexed { index, instruction ->
                if (index == id) instruction.copy(name = name) else instruction
            }
        _state.value = state.value.copy(instructions = instructions)
    }

    fun onAddComment() {
        val comments = state.value.comments
            .filterNot { it.isEditing && it.name.isEmpty() }
            .map { it.copy(isEditing = false) }
        _state.value = state.value.copy(
            comments = comments + EditableState(isEditing = true)
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
                    comment.name.isEmpty() && comment.isEditing -> null
                    else -> comment.copy(isEditing = false)
                }
            }
        _state.value = state.value.copy(comments = comments)
    }

    fun onCommentChanged(id: Int, name: String) {
        val comments = state.value.comments
            .mapIndexed { index, comment ->
                if (index == id) comment.copy(name = name) else comment
            }
        _state.value = state.value.copy(comments = comments)
    }

    fun onTimeCookingChanged(time: String) {
        _state.value = state.value.copy(
            time = state.value.time.copy(
                cookingText = time,
                cooking = time.toShortOrNull(),
                isCookingValid = time.isShort()
            )
        )
    }

    fun onTimePreparationChanged(time: String) {
        _state.value = state.value.copy(
            time = state.value.time.copy(
                preparationText = time,
                preparation = time.toShortOrNull(),
                isPreparationValid = time.isShort()
            )
        )
    }

    fun onTimeWaitingChanged(time: String) {
        _state.value = state.value.copy(
            time = state.value.time.copy(
                waitingText = time,
                waiting = time.toShortOrNull(),
                isWaitingValid = time.isShort()
            )
        )
    }

    fun onTimeTotalChanged(time: String) {
        _state.value = state.value.copy(
            time = state.value.time.copy(
                totalText = time,
                total = time.toShortOrNull(),
                isTotalValid = time.isShort()
            )
        )
    }

    fun computeTotal() {
        val timeState = state.value.time
        val total = timeState.cooking.toNotNull(0) +
            timeState.preparation.toNotNull(0) +
            timeState.waiting.toNotNull(0)
        _state.value = state.value.copy(time = timeState.copy(total = total.toShort()))
    }

    private fun String.isShort(): Boolean {
        if (isEmpty()) return true
        toShortOrNull()?.let { return true } ?: return false
    }

    private fun String.isDouble(): Boolean {
        if (isEmpty()) return true
        toDoubleOrNull()?.let { return true } ?: return false
    }

    private fun Short?.toNotNull(default: Short): Short = this ?: default
}

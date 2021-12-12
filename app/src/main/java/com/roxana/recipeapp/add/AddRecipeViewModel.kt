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
            ingredients = listOf(IngredientState(isEditing = true))
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

    private fun String.isShort(): Boolean {
        if (isEmpty()) return true
        toShortOrNull()?.let { return true } ?: return false
    }

    private fun String.isDouble(): Boolean {
        if (isEmpty()) return true
        toDoubleOrNull()?.let { return true } ?: return false
    }
}

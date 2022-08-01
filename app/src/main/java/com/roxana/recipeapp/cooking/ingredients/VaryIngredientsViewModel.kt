package com.roxana.recipeapp.cooking.ingredients

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.VaryIngredientQuantitiesNode
import com.roxana.recipeapp.domain.detail.GetRecipeByIdUseCase
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaryIngredientsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VaryIngredientsState())
    val state: StateFlow<VaryIngredientsState> = _state.asStateFlow()

    init {
        val recipeId = savedStateHandle.get<Int>(VaryIngredientQuantitiesNode.KEY_RECIPE_ID)!!
        viewModelScope.launch {
            getRecipeByIdUseCase(recipeId).getOrNull()?.let { recipe ->
                val ingredients = recipe.ingredients
                    .filter { it.quantity != null }
                    .map {
                        IngredientState(
                            id = it.id,
                            name = it.name,
                            quantity = it.quantity!!,
                            quantityText = it.quantity.toString(),
                            quantityType = it.quantityType.toUiModel()
                        )
                    }
                _state.value = VaryIngredientsState(ingredients)
            }
        }
    }

    fun onIngredientSelected(id: Int?) {
        val selectedIngredient = state.value.ingredients.firstOrNull { it.id == id }
        _state.value = state.value.copy(updatedIngredient = selectedIngredient)
    }

    fun onIngredientQuantityChanged(quantity: String) {
        val ingredient = state.value.updatedIngredient?.copy(
            quantityText = quantity,
            isQuantityInError = quantity.toDoubleOrNull() == null
        ) ?: return

        _state.value = state.value.copy(updatedIngredient = ingredient)
    }

    fun onValidate() {
        val ingredient = state.value.updatedIngredient ?: return
        if (ingredient.isQuantityInError) return
        val recipeId = savedStateHandle.get<Int>(VaryIngredientQuantitiesNode.KEY_RECIPE_ID)!!
        val multiplier = ingredient.quantityText.toDouble() / ingredient.quantity

        _state.update {
            it.copy(validation = Validation(recipeId, multiplier))
        }
    }

    fun onValidateDone() {
        _state.update { it.copy(validation = null) }
    }
}

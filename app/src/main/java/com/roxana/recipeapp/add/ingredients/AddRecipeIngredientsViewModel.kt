package com.roxana.recipeapp.add.ingredients

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.add.PageType
import com.roxana.recipeapp.domain.addrecipe.GetIngredientsUseCase
import com.roxana.recipeapp.domain.addrecipe.SetIngredientsUseCase
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.quantities.GetAllQuantityTypesUseCase
import com.roxana.recipeapp.domain.quantities.GetPreferredQuantitiesUseCase
import com.roxana.recipeapp.misc.toFormattedString
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.toDomainModel
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeIngredientsViewModel @Inject constructor(
    private val getPreferredQuantityTypesUseCase: GetPreferredQuantitiesUseCase,
    private val getAllQuantityTypesUseCase: GetAllQuantityTypesUseCase,
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val setIngredientsUseCase: SetIngredientsUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(AddRecipeIngredientsViewState())
    val state: StateFlow<AddRecipeIngredientsViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<AddRecipeIngredientsSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val quantities = getPreferredQuantityTypesUseCase(null).first()
                .getOrElse { emptyList() }
                .ifEmpty { getAllQuantityTypesUseCase(null).getOrNull() ?: emptyList() }
                .map { it.toUiModel() }
            val ingredients = getIngredientsUseCase(null).first().getOrElse { emptyList() }
                .map {
                    IngredientState(
                        it.id,
                        it.name,
                        it.quantity?.toFormattedString() ?: "",
                        it.quantityType.toUiModel()
                    )
                }
            _state.update { it.copy(ingredients = ingredients, quantityTypes = quantities) }
        }
    }

    fun onIngredientNameChanged(text: String) {
        _state.update {
            val editing = it.editingIngredient.copy(name = text)
            it.copy(editingIngredient = editing)
        }
    }

    fun onIngredientQuantityChanged(text: String) {
        _state.update {
            val editing = it.editingIngredient.copy(quantity = text)
            it.copy(editingIngredient = editing)
        }
    }

    fun onIngredientQuantityTypeChanged(quantityType: UiQuantityType) {
        _state.update {
            val editing = it.editingIngredient.copy(quantityType = quantityType)
            it.copy(editingIngredient = editing)
        }
    }

    fun onSaveIngredient() {
        _state.update { state ->
            val ingredient = state.editingIngredient
            val quantityType =
                if (ingredient.quantity.toDoubleOrNull() != null) ingredient.quantityType
                else UiQuantityType.None

            state.copy(
                ingredients = state.ingredients + ingredient.copy(quantityType = quantityType),
                editingIngredient = IngredientState()
            )
        }
    }

    fun onDeleteIngredient(indexToDelete: Int) {
        _state.update {
            val ingredients = it.ingredients.filterIndexed { index, _ -> index != indexToDelete }
            it.copy(ingredients = ingredients)
        }
    }

    fun onValidate() {
        viewModelScope.launch {
            setIngredientsUseCase(getAllIngredients()).fold(
                { sideEffectChannel.send(Forward) },
                { sideEffectChannel.send(Forward) }
            )
        }
    }

    fun onSaveAndBack() {
        viewModelScope.launch {
            setIngredientsUseCase(getAllIngredients()).fold(
                { sideEffectChannel.send(Back) },
                { sideEffectChannel.send(Back) }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setIngredientsUseCase(getAllIngredients()).fold(
                { sideEffectChannel.send(NavigateToPage(page)) },
                { sideEffectChannel.send(NavigateToPage(page)) }
            )
        }
    }

    private fun getAllIngredients() = state.value.ingredients
        .apply {
            if (!state.value.editingIngredient.isEmpty())
                plus(state.value.editingIngredient)
        }
        .map {
            CreationIngredient(
                it.id,
                it.name,
                it.quantity.toDoubleOrNull(),
                it.quantityType.toDomainModel()
            )
        }
}

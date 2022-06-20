package com.roxana.recipeapp.edit.ingredients

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetIngredientsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SetIngredientsUseCase
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.quantities.GetAllQuantityTypesUseCase
import com.roxana.recipeapp.domain.quantities.GetPreferredQuantitiesUseCase
import com.roxana.recipeapp.edit.PageType
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
class EditRecipeIngredientsViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getPreferredQuantityTypesUseCase: GetPreferredQuantitiesUseCase,
    private val getAllQuantityTypesUseCase: GetAllQuantityTypesUseCase,
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val setIngredientsUseCase: SetIngredientsUseCase,
    private val resetRecipeUseCase: ResetRecipeUseCase,
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipeIngredientsViewState())
    val state: StateFlow<EditRecipeIngredientsViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<EditRecipeIngredientsSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
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
            _state.update {
                it.copy(
                    isExistingRecipe = isExistingRecipe,
                    ingredients = ingredients,
                    quantityTypes = quantities
                )
            }
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
                { sendForwardEvent() },
                { sendForwardEvent() }
            )
        }
    }

    private suspend fun sendForwardEvent() {
        val isExistingRecipe = state.value.isExistingRecipe
        if (isExistingRecipe)
            sideEffectChannel.send(ForwardForEditing)
        else
            sideEffectChannel.send(ForwardForCreation)
    }

    fun onResetAndClose() {
        _state.update { it.copy(showSaveDialog = false) }
        viewModelScope.launch {
            resetRecipeUseCase(null).fold(
                { sideEffectChannel.send(Close) },
                { sideEffectChannel.send(Close) }
            )
        }
    }

    fun onSaveAndClose() {
        _state.update { it.copy(showSaveDialog = false) }
        viewModelScope.launch {
            setIngredientsUseCase(getAllIngredients()).fold(
                { sideEffectChannel.send(Close) },
                { sideEffectChannel.send(Close) }
            )
        }
    }

    fun onDismissDialog() {
        _state.update { it.copy(showSaveDialog = false) }
    }

    fun onCheckShouldClose() {
        _state.update { it.copy(showSaveDialog = true) }
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

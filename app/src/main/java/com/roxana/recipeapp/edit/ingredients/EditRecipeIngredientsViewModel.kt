package com.roxana.recipeapp.edit.ingredients

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.common.utilities.toFormattedString
import com.roxana.recipeapp.domain.editrecipe.GetIngredientsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SetIngredientsUseCase
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.quantities.GetAllQuantityTypesUseCase
import com.roxana.recipeapp.domain.quantities.GetPreferredQuantitiesUseCase
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.toDomainModel
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

    init {
        viewModelScope.launch {
            val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
            val quantities = getPreferredQuantityTypesUseCase(null).first()
                .getOrElse { emptyList() }
                .ifEmpty { getAllQuantityTypesUseCase(null).getOrNull() ?: emptyList() }
                .map { it.toUiModel() }
            val ingredients = getIngredientsUseCase(null).first().getOrElse { emptyList() }
                .map {
                    val quantity = it.quantity?.toFormattedString() ?: ""
                    val quantityError = !isQuantityValid(quantity)
                    IngredientState(
                        it.id,
                        it.name,
                        quantity,
                        quantityError,
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

    private fun isQuantityValid(quantity: String) =
        quantity.isEmpty() || quantity.toDoubleOrNull() != null

    fun onIngredientNameChanged(text: String) {
        _state.update {
            val editing = it.editingIngredient.copy(name = text)
            it.copy(
                editingIngredient = editing,
                canAddIngredient = text.isNotBlank() && !editing.isQuantityError
            )
        }
    }

    fun onIngredientQuantityChanged(text: String) {
        _state.update {
            val quantityError = !isQuantityValid(text)
            val editing =
                it.editingIngredient.copy(quantity = text, isQuantityError = quantityError)
            it.copy(
                editingIngredient = editing,
                canAddIngredient = editing.name.isNotBlank() && !quantityError
            )
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
            if (state.canAddIngredient) {
                val ingredient = state.editingIngredient
                val quantityType =
                    if (ingredient.quantity.toDoubleOrNull() != null) ingredient.quantityType
                    else UiQuantityType.None

                state.copy(
                    ingredients = state.ingredients + ingredient.copy(quantityType = quantityType),
                    editingIngredient = IngredientState(),
                    canAddIngredient = false
                )
            } else state
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

    private fun sendForwardEvent() {
        val isExistingRecipe = state.value.isExistingRecipe
        if (isExistingRecipe)
            _state.update { it.copy(navigation = Navigation.ForwardEditing) }
        else
            _state.update { it.copy(navigation = Navigation.ForwardCreation) }
    }

    fun onBack() {
        viewModelScope.launch {
            setIngredientsUseCase(getAllIngredients()).fold(
                { _state.update { it.copy(navigation = Navigation.Back) } },
                { _state.update { it.copy(navigation = Navigation.Back) } }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setIngredientsUseCase(getAllIngredients()).fold(
                {
                    _state.update {
                        it.copy(navigation = Navigation.ToPage(page, state.value.isExistingRecipe))
                    }
                },
                {
                    _state.update {
                        it.copy(navigation = Navigation.ToPage(page, state.value.isExistingRecipe))
                    }
                }
            )
        }
    }

    fun onNavigationDone() {
        _state.update { it.copy(navigation = null) }
    }

    private fun getAllIngredients() = state.value.ingredients
        .apply {
            if (state.value.canAddIngredient)
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

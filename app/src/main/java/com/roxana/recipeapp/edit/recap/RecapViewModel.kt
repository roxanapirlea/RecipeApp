package com.roxana.recipeapp.edit.recap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SaveRecipeUseCase
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
class RecapViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(RecapViewState())
    val state: StateFlow<RecapViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getRecipeUseCase(null).first()
                .fold(
                    { recipe ->
                        val content = RecapViewState(
                            title = recipe.name,
                            photoPath = recipe.photoPath,
                            categories = recipe.categories.map { it.toUiModel() },
                            portions = recipe.portions,
                            ingredients = recipe.ingredients.map {
                                IngredientState(
                                    it.name,
                                    it.quantity,
                                    it.quantityType.toUiModel()
                                )
                            },
                            instructions = recipe.instructions.sortedBy { it.ordinal }
                                .map { it.name },
                            comments = recipe.comments.sortedBy { it.ordinal }
                                .map { it.detail },
                            time = TimeState(
                                total = recipe.timeTotal,
                                cooking = recipe.timeCooking,
                                waiting = recipe.timeWaiting,
                                preparation = recipe.timePreparation
                            ),
                            temperature = recipe.temperature,
                            temperatureUnit = recipe.temperatureUnit?.toUiModel(),
                            isExistingRecipe = recipe.id != null
                        )
                        _state.value = content
                    },
                    {
                        _state.update { it.copy(isFetchingError = true) }
                    }
                )
        }
    }

    fun onErrorDismissed() {
        _state.update { it.copy(isFetchingError = false) }
    }

    fun createRecipe() {
        viewModelScope.launch {
            saveRecipeUseCase(null).fold(
                { _state.update { it.copy(saveResult = SaveResult(true)) } },
                { _state.update { it.copy(saveResult = SaveResult(false)) } }
            )
        }
    }

    fun onSaveResultDismissed() {
        _state.update { it.copy(saveResult = null) }
    }

    fun onClosingDone() {
        _state.update { it.copy(shouldClose = false) }
    }
}

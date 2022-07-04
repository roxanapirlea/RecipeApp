package com.roxana.recipeapp.detail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.RecipeDetailNode
import com.roxana.recipeapp.domain.detail.GetRecipeByIdAsFlowUseCase
import com.roxana.recipeapp.domain.detail.StartRecipeEditingUseCase
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecipeByIdUseCase: GetRecipeByIdAsFlowUseCase,
    private val startRecipeEditingUseCase: StartRecipeEditingUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(DetailViewState(isLoading = true))
    val state: StateFlow<DetailViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val recipeId: Int = savedStateHandle.get(RecipeDetailNode.KEY_ID)!!
            getRecipeByIdUseCase(recipeId).collect { result ->
                result.fold(
                    { recipe ->
                        val content = DetailViewState(
                            title = recipe.name,
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
                            comments = recipe.comments.sortedBy { it.ordinal }.map { it.name },
                            time = TimeState(
                                total = recipe.timeTotal,
                                cooking = recipe.timeCooking,
                                waiting = recipe.timeWaiting,
                                preparation = recipe.timePreparation
                            ),
                            temperature = recipe.temperature,
                            temperatureUnit = recipe.temperatureUnit?.toUiModel(),
                            isLoading = false
                        )
                        _state.value = content
                    },
                    {
                        _state.update { it.copy(isFetchingError = true) }
                    }
                )
            }
        }
    }

    fun onEdit() {
        viewModelScope.launch {
            val recipeId: Int = savedStateHandle.get(RecipeDetailNode.KEY_ID)!!
            startRecipeEditingUseCase(recipeId).onSuccess {
                _state.update { it.copy(shouldStartEditing = true) }
            }
        }
    }

    fun onEditingStarted() {
        _state.update { it.copy(shouldStartEditing = false) }
    }

    fun onErrorDismissed() {
        _state.update { it.copy(isFetchingError = false) }
    }
}

package com.roxana.recipeapp.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.RecipeDetailNode
import com.roxana.recipeapp.domain.detail.DeleteRecipeUseCase
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
    private val startRecipeEditingUseCase: StartRecipeEditingUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DetailViewState(isLoading = true))
    val state: StateFlow<DetailViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val recipeId: Int = savedStateHandle.get(RecipeDetailNode.KEY_ID)!!
            getRecipeByIdUseCase(recipeId).collect { result ->
                result.fold(
                    { recipe ->
                        val content = DetailViewState(
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
            val recipeId: Int = savedStateHandle[RecipeDetailNode.KEY_ID]!!
            startRecipeEditingUseCase(recipeId).onSuccess {
                _state.update { it.copy(navigation = Navigation.EDIT) }
            }
        }
    }

    fun onNavigationDone() {
        _state.update { it.copy(navigation = null) }
    }

    fun onDelete() {
        _state.update { it.copy(shouldShowDeleteMessage = true) }
    }

    fun onUndoDelete() {
        _state.update { it.copy(shouldShowDeleteMessage = false) }
    }

    fun onDeleteMessageDismissed() {
        viewModelScope.launch {
            _state.update { it.copy(shouldShowDeleteMessage = false) }
            val recipeId: Int = savedStateHandle[RecipeDetailNode.KEY_ID]!!
            deleteRecipeUseCase(recipeId).onSuccess {
                _state.update { it.copy(navigation = Navigation.BACK) }
            }
        }
    }

    fun onErrorDismissed() {
        _state.update { it.copy(isFetchingError = false) }
    }
}

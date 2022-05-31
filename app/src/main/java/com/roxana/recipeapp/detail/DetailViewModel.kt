package com.roxana.recipeapp.detail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.RecipeDetail
import com.roxana.recipeapp.domain.detail.GetRecipeByIdAsFlowUseCase
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecipeByIdUseCase: GetRecipeByIdAsFlowUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow<DetailViewState>(DetailViewState.Loading)
    val state: StateFlow<DetailViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<DetailSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val recipeId: Int = savedStateHandle.get(RecipeDetail.KEY_ID)!!
            getRecipeByIdUseCase(recipeId).collect { result ->
                result.fold(
                    { recipe ->
                        val content = DetailViewState.Content(
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
                            temperatureUnit = recipe.temperatureUnit?.toUiModel()
                        )
                        _state.value = content
                    },
                    {
                        sideEffectChannel.send(FetchingError)
                    }
                )
            }
        }
    }
}

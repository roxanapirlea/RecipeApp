package com.roxana.recipeapp.add.recap

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.addrecipe.AddRecipeUseCase
import com.roxana.recipeapp.domain.addrecipe.GetRecipeUseCase
import com.roxana.recipeapp.domain.addrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecapViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val addRecipeUseCase: AddRecipeUseCase
) : ViewModel() {

    @VisibleForTesting
    val _state = MutableStateFlow(RecapViewState())
    val state: StateFlow<RecapViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<RecapSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getRecipeUseCase(null).first()
                .fold(
                    { recipe ->
                        val content = RecapViewState(
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
                            comments = recipe.comments.sortedBy { it.ordinal }.map { it.detail },
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

    fun saveRecipe() {
        viewModelScope.launch {
            addRecipeUseCase(null).fold(
                { sideEffectChannel.send(SaveRecipeSuccess) },
                { sideEffectChannel.send(SaveRecipeError) }
            )
        }
    }
}

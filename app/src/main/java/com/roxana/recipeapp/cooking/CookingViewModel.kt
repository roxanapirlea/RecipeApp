package com.roxana.recipeapp.cooking

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.Cooking
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
import kotlin.math.floor

@HiltViewModel
class CookingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecipeByIdUseCase: GetRecipeByIdAsFlowUseCase
) : ViewModel() {

    @VisibleForTesting
    val _state = MutableStateFlow<CookingViewState>(CookingViewState.Loading)
    val state: StateFlow<CookingViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<CookingSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val recipeId: Int = savedStateHandle.get(Cooking.KEY_ID)!!
            val quantityMultiplier: Double = savedStateHandle.get<String>(
                Cooking.KEY_PORTIONS_MULTIPLIER
            )?.toDoubleOrNull() ?: 1.0
            getRecipeByIdUseCase(recipeId).collect {
                it.fold(
                    { recipe ->
                        val nonNullPortions = recipe.portions?.toDouble() ?: 1.0
                        val content = CookingViewState.Content(
                            title = recipe.name,
                            portions = recipe.portions,
                            selectedPortions = nonNullPortions * quantityMultiplier,
                            ingredients = recipe.ingredients.map {
                                val quantityForSelectedPortions =
                                    it.quantity?.let { quantity -> quantity * quantityMultiplier }
                                IngredientState(
                                    it.id,
                                    it.name,
                                    it.quantity,
                                    quantityForSelectedPortions,
                                    it.quantityType.toUiModel()
                                )
                            },
                            instructions = recipe.instructions.sortedBy { it.ordinal }
                                .map { InstructionState(it.ordinal, it.name) }
                                .updateCurrent(),
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

    fun onDecrementPortions() {
        if (state.value !is CookingViewState.Content) return
        val content = state.value as CookingViewState.Content

        val selectedPortions = floor(content.selectedPortions) - 1
        if (selectedPortions == 0.0) return

        val multiplyCoefficient = selectedPortions / (content.portions ?: 0)

        _state.value = content.copy(
            selectedPortions = selectedPortions,
            ingredients = content.ingredients.updateQuantities(multiplyCoefficient)
        )
    }

    fun onIncrementPortions() {
        if (state.value !is CookingViewState.Content) return
        val content = state.value as CookingViewState.Content

        val selectedPortions = floor(content.selectedPortions) + 1
        if (selectedPortions == 0.0) return

        val multiplyCoefficient = selectedPortions / (content.portions ?: 0)

        _state.value = content.copy(
            selectedPortions = selectedPortions,
            ingredients = content.ingredients.updateQuantities(multiplyCoefficient)
        )
    }

    fun onResetPortions() {
        if (state.value !is CookingViewState.Content) return
        val content = state.value as CookingViewState.Content

        val selectedPortions = content.portions?.toDouble() ?: 1.0

        _state.value = content.copy(
            selectedPortions = selectedPortions,
            ingredients = content.ingredients.updateQuantities(1.0)
        )
    }

    fun toggleIngredientCheck(id: Int, isChecked: Boolean) {
        if (state.value !is CookingViewState.Content) return
        val content = state.value as CookingViewState.Content

        val updatedIngredient = content.ingredients.getById(id).copy(isChecked = isChecked)

        _state.value = content.copy(
            ingredients = content.ingredients.updateItem(id, updatedIngredient)
        )
    }

    fun toggleInstructionCheck(id: Short, isChecked: Boolean) {
        if (state.value !is CookingViewState.Content) return

        val content = state.value as CookingViewState.Content

        val updatedInstruction = content.instructions.getById(id).copy(isChecked = isChecked)

        _state.value = content.copy(
            instructions = content.instructions.updateItem(id, updatedInstruction).updateCurrent()
        )
    }

    private fun List<IngredientState>.updateQuantities(
        multiplyCoefficient: Double
    ): List<IngredientState> =
        map { ingredient ->
            val quantity = ingredient.quantity?.let { it * multiplyCoefficient }
            ingredient.copy(quantityForSelectedPortion = quantity)
        }

    private fun List<IngredientState>.updateItem(
        id: Int,
        newState: IngredientState
    ): List<IngredientState> =
        map { if (it.id == id) newState else it }

    private fun List<IngredientState>.getById(id: Int): IngredientState = first { it.id == id }

    private fun List<InstructionState>.updateCurrent(): List<InstructionState> {
        val currentInstructionId = firstOrNull { !it.isChecked }?.id
        return map { it.copy(isCurrent = it.id == currentInstructionId) }
    }

    private fun List<InstructionState>.updateItem(
        id: Short,
        newState: InstructionState
    ): List<InstructionState> =
        map { if (it.id == id) newState else it }

    private fun List<InstructionState>.getById(id: Short): InstructionState = first { it.id == id }
}

package com.roxana.recipeapp.cooking

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.CookingNode
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

    companion object {
        private const val KEY_SAVE_PORTIONS_COEF = "key_save_portions_coef"
        private const val KEY_SAVE_CHECKED_INSTRUCTIONS = "key_save_checked_instructions"
        private const val KEY_SAVE_CHECKED_INGREDIENTS = "key_save_checked_ingredients"
    }

    @VisibleForTesting
    val _state = MutableStateFlow(CookingViewState(isLoading = true))
    val state: StateFlow<CookingViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<CookingSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val recipeId: Int = savedStateHandle[CookingNode.KEY_ID]!!
            val quantityMultiplier: Double = savedStateHandle.get<String>(
                CookingNode.KEY_PORTIONS_MULTIPLIER
            )?.toDoubleOrNull() ?: 1.0
            getRecipeByIdUseCase(recipeId).collect { result ->
                result.fold(
                    { recipe ->
                        val nonNullPortions = recipe.portions?.toDouble() ?: 1.0
                        val multiplier =
                            savedStateHandle[KEY_SAVE_PORTIONS_COEF] ?: quantityMultiplier
                        val checkedIngredients = savedStateHandle.get<List<Int>>(
                            KEY_SAVE_CHECKED_INGREDIENTS
                        ) ?: emptyList()
                        val checkedInstructions = savedStateHandle.get<List<Short>>(
                            KEY_SAVE_CHECKED_INSTRUCTIONS
                        ) ?: emptyList()

                        val state = CookingViewState(
                            title = recipe.name,
                            portions = recipe.portions,
                            selectedPortions = nonNullPortions * multiplier,
                            ingredients = recipe.ingredients.map {
                                val quantityForSelectedPortions =
                                    it.quantity?.let { quantity -> quantity * multiplier }
                                IngredientState(
                                    it.id,
                                    it.name,
                                    it.quantity,
                                    quantityForSelectedPortions,
                                    it.quantityType.toUiModel(),
                                    checkedIngredients.contains(it.id)
                                )
                            },
                            instructions = recipe.instructions.sortedBy { it.ordinal }
                                .map {
                                    InstructionState(
                                        it.ordinal,
                                        it.name,
                                        checkedInstructions.contains(it.ordinal)
                                    )
                                }
                                .updateCurrent(),
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
                        _state.value = state
                    },
                    {
                        sideEffectChannel.send(FetchingError)
                    }
                )
            }
        }
    }

    fun onDecrementPortions() {
        val content = state.value

        val selectedPortions = floor(content.selectedPortions) - 1
        if (selectedPortions == 0.0) return

        val multiplyCoefficient = selectedPortions / (content.portions ?: 0)
        savedStateHandle[KEY_SAVE_PORTIONS_COEF] = multiplyCoefficient

        _state.value = content.copy(
            selectedPortions = selectedPortions,
            ingredients = content.ingredients.updateQuantities(multiplyCoefficient)
        )
    }

    fun onIncrementPortions() {
        val content = state.value

        val selectedPortions = floor(content.selectedPortions) + 1
        if (selectedPortions == 0.0) return

        val multiplyCoefficient = selectedPortions / (content.portions ?: 0)
        savedStateHandle[KEY_SAVE_PORTIONS_COEF] = multiplyCoefficient

        _state.value = content.copy(
            selectedPortions = selectedPortions,
            ingredients = content.ingredients.updateQuantities(multiplyCoefficient)
        )
    }

    fun onResetPortions() {
        val content = state.value

        val selectedPortions = content.portions?.toDouble() ?: 1.0
        savedStateHandle.remove<Double>(KEY_SAVE_PORTIONS_COEF)

        _state.value = content.copy(
            selectedPortions = selectedPortions,
            ingredients = content.ingredients.updateQuantities(1.0)
        )
    }

    fun toggleIngredientCheck(id: Int, isChecked: Boolean) {
        val content = state.value

        val updatedIngredient = content.ingredients.getById(id).copy(isChecked = isChecked)
        val newIngredients = content.ingredients.updateItem(id, updatedIngredient)

        savedStateHandle[KEY_SAVE_CHECKED_INGREDIENTS] =
            newIngredients.filter { it.isChecked }.map { it.id }

        _state.value = content.copy(ingredients = newIngredients)
    }

    fun toggleInstructionCheck(id: Short, isChecked: Boolean) {
        val content = state.value

        val updatedInstruction = content.instructions.getById(id).copy(isChecked = isChecked)
        val newInstructions = content.instructions.updateItem(id, updatedInstruction)

        savedStateHandle[KEY_SAVE_CHECKED_INSTRUCTIONS] =
            newInstructions.filter { it.isChecked }.map { it.id }

        _state.value = content.copy(
            instructions = newInstructions.updateCurrent()
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

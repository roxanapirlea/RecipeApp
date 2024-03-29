package com.roxana.recipeapp.edit.instructions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetInstructionsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetInstructionsUseCase
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.edit.PageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeInstructionsViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getInstructionsUseCase: GetInstructionsUseCase,
    private val setInstructionsUseCase: SetInstructionsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(EditRecipeInstructionsViewState())
    val state: StateFlow<EditRecipeInstructionsViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
            val instructions = getInstructionsUseCase(null).first().getOrElse { emptyList() }
                .map { it.name }
            _state.update {
                it.copy(
                    instructions = instructions,
                    isExistingRecipe = isExistingRecipe
                )
            }
        }
    }

    fun onInstructionChanged(text: String) {
        _state.update {
            it.copy(editingInstruction = text, canAddInstruction = text.isNotBlank())
        }
    }

    fun onSaveInstruction() {
        _state.update { state ->
            if (state.canAddInstruction)
                state.copy(
                    instructions = state.instructions + state.editingInstruction,
                    editingInstruction = "",
                    canAddInstruction = false
                )
            else state
        }
    }

    fun onInstructionDone() {
        if (state.value.canAddInstruction) onSaveInstruction() else onValidate()
    }

    fun onDeleteInstruction(indexToDelete: Int) {
        _state.update {
            val instructions = it.instructions.filterIndexed { index, _ -> index != indexToDelete }
            it.copy(instructions = instructions)
        }
    }

    fun onValidate() {
        viewModelScope.launch {
            setInstructionsUseCase(getAllInstructions()).fold(
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
            setInstructionsUseCase(getAllInstructions()).fold(
                { _state.update { it.copy(navigation = Navigation.Back) } },
                { _state.update { it.copy(navigation = Navigation.Back) } }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setInstructionsUseCase(getAllInstructions()).fold(
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

    private fun getAllInstructions() = state.value.instructions
        .apply {
            if (state.value.canAddInstruction)
                plus(state.value.editingInstruction)
        }
        .mapIndexed { index, instruction ->
            CreationInstruction(instruction, index.toShort())
        }
}

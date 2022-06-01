package com.roxana.recipeapp.edit.instructions

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.domain.editrecipe.GetInstructionsUseCase
import com.roxana.recipeapp.domain.editrecipe.SetInstructionsUseCase
import com.roxana.recipeapp.domain.model.CreationInstruction
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
class EditRecipeInstructionsViewModel @Inject constructor(
    private val getInstructionsUseCase: GetInstructionsUseCase,
    private val setInstructionsUseCase: SetInstructionsUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipeInstructionsViewState())
    val state: StateFlow<EditRecipeInstructionsViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<EditRecipeInstructionsSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val instructions = getInstructionsUseCase(null).first().getOrElse { emptyList() }
                .map { it.name }
            _state.update { it.copy(instructions = instructions) }
        }
    }

    fun onInstructionChanged(text: String) {
        _state.update {
            it.copy(editingInstruction = text)
        }
    }

    fun onSaveInstruction() {
        _state.update { state ->
            state.copy(
                instructions = state.instructions + state.editingInstruction,
                editingInstruction = ""
            )
        }
    }

    fun onInstructionDone() {
        if (state.value.editingInstruction.isEmpty()) onValidate() else onSaveInstruction()
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
                { sideEffectChannel.send(Forward) },
                { sideEffectChannel.send(Forward) }
            )
        }
    }

    fun onSaveAndBack() {
        viewModelScope.launch {
            setInstructionsUseCase(getAllInstructions()).fold(
                { sideEffectChannel.send(Back) },
                { sideEffectChannel.send(Back) }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setInstructionsUseCase(getAllInstructions()).fold(
                { sideEffectChannel.send(NavigateToPage(page)) },
                { sideEffectChannel.send(NavigateToPage(page)) }
            )
        }
    }

    private fun getAllInstructions() = state.value.instructions
        .apply {
            if (state.value.editingInstruction.isNotEmpty())
                plus(state.value.editingInstruction)
        }
        .mapIndexed { index, instruction ->
            CreationInstruction(instruction, index.toShort())
        }
}

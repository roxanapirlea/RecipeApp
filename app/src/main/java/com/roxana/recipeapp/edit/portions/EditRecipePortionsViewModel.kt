package com.roxana.recipeapp.edit.portions

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.domain.editrecipe.GetPortionsUseCase
import com.roxana.recipeapp.domain.editrecipe.SetPortionsUseCase
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
class EditRecipePortionsViewModel @Inject constructor(
    private val getPortionsUseCase: GetPortionsUseCase,
    private val setPortionsUseCase: SetPortionsUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipePortionsViewState())
    val state: StateFlow<EditRecipePortionsViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<EditRecipePortionsSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val portions = getPortionsUseCase(null).first().getOrNull()?.toString() ?: ""
            _state.value = EditRecipePortionsViewState(portions)
        }
    }

    fun onPortionsChanged(name: String) {
        _state.update { it.copy(portions = name) }
    }

    fun onValidate() {
        viewModelScope.launch {
            setPortionsUseCase(state.value.portions.toShortOrNull()).fold(
                { sideEffectChannel.send(Forward) },
                { sideEffectChannel.send(Forward) }
            )
        }
    }

    fun onSaveAndBack() {
        viewModelScope.launch {
            setPortionsUseCase(state.value.portions.toShortOrNull()).fold(
                { sideEffectChannel.send(Back) },
                { sideEffectChannel.send(Back) }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setPortionsUseCase(state.value.portions.toShortOrNull()).fold(
                { sideEffectChannel.send(NavigateToPage(page)) },
                { sideEffectChannel.send(NavigateToPage(page)) }
            )
        }
    }
}

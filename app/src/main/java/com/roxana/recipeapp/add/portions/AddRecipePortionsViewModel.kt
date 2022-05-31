package com.roxana.recipeapp.add.portions

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.add.PageType
import com.roxana.recipeapp.domain.addrecipe.GetPortionsUseCase
import com.roxana.recipeapp.domain.addrecipe.SetPortionsUseCase
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
class AddRecipePortionsViewModel @Inject constructor(
    private val getPortionsUseCase: GetPortionsUseCase,
    private val setPortionsUseCase: SetPortionsUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(AddRecipePortionsViewState())
    val state: StateFlow<AddRecipePortionsViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<AddRecipePortionsSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val portions = getPortionsUseCase(null).first().getOrNull()?.toString() ?: ""
            _state.value = AddRecipePortionsViewState(portions)
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

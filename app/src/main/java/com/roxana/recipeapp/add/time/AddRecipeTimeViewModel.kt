package com.roxana.recipeapp.add.time

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.add.PageType
import com.roxana.recipeapp.domain.addrecipe.GetCookingTimeUseCase
import com.roxana.recipeapp.domain.addrecipe.GetPreparationTimeUseCase
import com.roxana.recipeapp.domain.addrecipe.GetTotalTimeUseCase
import com.roxana.recipeapp.domain.addrecipe.GetWaitingTimeUseCase
import com.roxana.recipeapp.domain.addrecipe.SetTimeUseCase
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
class AddRecipeTimeViewModel @Inject constructor(
    private val getCookingTimeUseCase: GetCookingTimeUseCase,
    private val getPreparationTimeUseCase: GetPreparationTimeUseCase,
    private val getWaitingTimeUseCase: GetWaitingTimeUseCase,
    private val getTotalTimeUseCase: GetTotalTimeUseCase,
    private val setTimeUseCase: SetTimeUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(AddRecipeTimeViewState())
    val state: StateFlow<AddRecipeTimeViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<AddRecipeTimeSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val cooking = getCookingTimeUseCase(null).first().getOrNull()?.toString() ?: ""
            val preparation =
                getPreparationTimeUseCase(null).first().getOrNull()?.toString() ?: ""
            val waiting = getWaitingTimeUseCase(null).first().getOrNull()?.toString() ?: ""
            val total = getTotalTimeUseCase(null).first().getOrNull()?.toString() ?: ""
            _state.value = AddRecipeTimeViewState(
                cooking = cooking,
                preparation = preparation,
                waiting = waiting,
                total = total
            )
        }
    }

    fun onCookingChanged(time: String) {
        _state.update { it.copy(cooking = time) }
    }

    fun onPreparationCookingChanged(time: String) {
        _state.update { it.copy(preparation = time) }
    }

    fun onWaitingChanged(time: String) {
        _state.update { it.copy(waiting = time) }
    }

    fun onTotalChanged(time: String) {
        _state.update { it.copy(total = time) }
    }

    fun onComputeTotal() {
        _state.update {
            val cooking = it.cooking.toShortOrNull() ?: 0
            val preparation = it.preparation.toShortOrNull() ?: 0
            val waiting = it.waiting.toShortOrNull() ?: 0
            val total = cooking + preparation + waiting
            it.copy(total = total.toString())
        }
    }

    fun onValidate() {
        viewModelScope.launch {
            setTimeUseCase(getInput()).fold(
                { sideEffectChannel.send(Forward) },
                { sideEffectChannel.send(Forward) }
            )
        }
    }

    fun onSaveAndBack() {
        viewModelScope.launch {
            setTimeUseCase(getInput()).fold(
                { sideEffectChannel.send(Back) },
                { sideEffectChannel.send(Back) }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setTimeUseCase(getInput()).fold(
                { sideEffectChannel.send(NavigateToPage(page)) },
                { sideEffectChannel.send(NavigateToPage(page)) }
            )
        }
    }

    private fun getInput(): SetTimeUseCase.Input =
        state.value.let {
            SetTimeUseCase.Input(
                cooking = it.cooking.toShortOrNull(),
                preparation = it.preparation.toShortOrNull(),
                waiting = it.waiting.toShortOrNull(),
                total = it.total.toShortOrNull()
            )
        }
}

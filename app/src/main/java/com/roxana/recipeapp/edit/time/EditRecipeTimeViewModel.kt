package com.roxana.recipeapp.edit.time

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetCookingTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.GetPreparationTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.GetTotalTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.GetWaitingTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetTimeUseCase
import com.roxana.recipeapp.edit.PageType
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
class EditRecipeTimeViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getCookingTimeUseCase: GetCookingTimeUseCase,
    private val getPreparationTimeUseCase: GetPreparationTimeUseCase,
    private val getWaitingTimeUseCase: GetWaitingTimeUseCase,
    private val getTotalTimeUseCase: GetTotalTimeUseCase,
    private val setTimeUseCase: SetTimeUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipeTimeViewState())
    val state: StateFlow<EditRecipeTimeViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<EditRecipeTimeSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
            val cooking = getCookingTimeUseCase(null).first().getOrNull()?.toString() ?: ""
            val preparation =
                getPreparationTimeUseCase(null).first().getOrNull()?.toString() ?: ""
            val waiting = getWaitingTimeUseCase(null).first().getOrNull()?.toString() ?: ""
            val total = getTotalTimeUseCase(null).first().getOrNull()?.toString() ?: ""
            _state.value = EditRecipeTimeViewState(
                isExistingRecipe = isExistingRecipe,
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
                { sendForwardEvent() },
                { sendForwardEvent() }
            )
        }
    }

    private suspend fun sendForwardEvent() {
        val isExistingRecipe = state.value.isExistingRecipe
        if (isExistingRecipe)
            sideEffectChannel.send(ForwardForEditing)
        else
            sideEffectChannel.send(ForwardForCreation)
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

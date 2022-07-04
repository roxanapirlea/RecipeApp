package com.roxana.recipeapp.edit.time

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetCookingTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.GetPreparationTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.GetTotalTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.GetWaitingTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SetTimeUseCase
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
class EditRecipeTimeViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getCookingTimeUseCase: GetCookingTimeUseCase,
    private val getPreparationTimeUseCase: GetPreparationTimeUseCase,
    private val getWaitingTimeUseCase: GetWaitingTimeUseCase,
    private val getTotalTimeUseCase: GetTotalTimeUseCase,
    private val setTimeUseCase: SetTimeUseCase,
    private val resetRecipeUseCase: ResetRecipeUseCase,
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipeTimeViewState())
    val state: StateFlow<EditRecipeTimeViewState> = _state.asStateFlow()

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

    private fun sendForwardEvent() {
        val isExistingRecipe = state.value.isExistingRecipe
        if (isExistingRecipe)
            _state.update { it.copy(navigation = Navigation.ForwardEditing) }
        else
            _state.update { it.copy(navigation = Navigation.ForwardCreation) }
    }

    fun onResetAndClose() {
        _state.update { it.copy(showSaveDialog = false) }
        viewModelScope.launch {
            resetRecipeUseCase(null).fold(
                { _state.update { it.copy(navigation = Navigation.Close) } },
                { _state.update { it.copy(navigation = Navigation.Close) } }
            )
        }
    }

    fun onSaveAndClose() {
        viewModelScope.launch {
            setTimeUseCase(getInput()).fold(
                { _state.update { it.copy(navigation = Navigation.Close) } },
                { _state.update { it.copy(navigation = Navigation.Close) } }
            )
        }
    }

    fun onDismissDialog() {
        _state.update { it.copy(showSaveDialog = false) }
    }

    fun onCheckShouldClose() {
        _state.update { it.copy(showSaveDialog = true) }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setTimeUseCase(getInput()).fold(
                { _state.update { it.copy(navigation = Navigation.ToPage(page)) } },
                { _state.update { it.copy(navigation = Navigation.ToPage(page)) } }
            )
        }
    }

    fun onNavigationDone() {
        _state.update { it.copy(navigation = null) }
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

package com.roxana.recipeapp.edit.title

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.domain.editrecipe.GetTitleUseCase
import com.roxana.recipeapp.domain.editrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SetTitleUseCase
import com.roxana.recipeapp.domain.onboarding.GetEditOnboardingUseCase
import com.roxana.recipeapp.domain.onboarding.SetEditOnboardingDoneUseCase
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
class EditRecipeTitleViewModel @Inject constructor(
    private val getTitleUseCase: GetTitleUseCase,
    private val setTitleUseCase: SetTitleUseCase,
    private val resetRecipeUseCase: ResetRecipeUseCase,
    private val onboardingUseCase: GetEditOnboardingUseCase,
    private val setOnboardingDoneUseCase: SetEditOnboardingDoneUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipeTitleViewState())
    val state: StateFlow<EditRecipeTitleViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<EditRecipeTitleSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val title = getTitleUseCase(null).first().getOrNull() ?: ""
            val isOnboardingDone = onboardingUseCase(null).first()
                .getOrElse { GetEditOnboardingUseCase.Output(false) }.isDone
            if (!isOnboardingDone) {
                sideEffectChannel.send(RevealBackdrop)
                setOnboardingDoneUseCase.execute(null)
            }
            _state.value = EditRecipeTitleViewState(title)
        }
    }

    fun onTitleChanged(name: String) {
        _state.update { it.copy(title = name) }
    }

    fun onValidate() {
        viewModelScope.launch {
            setTitleUseCase(state.value.title).fold(
                { sideEffectChannel.send(Forward) },
                { sideEffectChannel.send(Forward) }
            )
        }
    }

    fun onReset() {
        _state.update { it.copy(showSaveDialog = false) }
        viewModelScope.launch {
            resetRecipeUseCase(null).fold(
                { sideEffectChannel.send(Back) },
                { sideEffectChannel.send(Back) }
            )
        }
    }

    fun onSaveAndBack() {
        _state.update { it.copy(showSaveDialog = false) }
        viewModelScope.launch {
            setTitleUseCase(state.value.title).fold(
                { sideEffectChannel.send(Back) },
                { sideEffectChannel.send(Back) }
            )
        }
    }

    fun onDiscardDialog() {
        _state.update { it.copy(showSaveDialog = false) }
    }

    fun onCheckBack() {
        _state.update { it.copy(showSaveDialog = true) }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setTitleUseCase(state.value.title).fold(
                { sideEffectChannel.send(NavigateToPage(page)) },
                { sideEffectChannel.send(NavigateToPage(page)) }
            )
        }
    }
}

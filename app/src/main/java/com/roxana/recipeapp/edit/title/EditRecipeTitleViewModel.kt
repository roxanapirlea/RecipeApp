package com.roxana.recipeapp.edit.title

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetTitleUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SetTitleUseCase
import com.roxana.recipeapp.domain.onboarding.GetEditOnboardingUseCase
import com.roxana.recipeapp.domain.onboarding.SetEditOnboardingDoneUseCase
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
class EditRecipeTitleViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getTitleUseCase: GetTitleUseCase,
    private val setTitleUseCase: SetTitleUseCase,
    private val resetRecipeUseCase: ResetRecipeUseCase,
    private val onboardingUseCase: GetEditOnboardingUseCase,
    private val setOnboardingDoneUseCase: SetEditOnboardingDoneUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipeTitleViewState())
    val state: StateFlow<EditRecipeTitleViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
            val title = getTitleUseCase(null).first().getOrNull() ?: ""
            val isOnboardingDone = onboardingUseCase(null).first()
                .getOrElse { GetEditOnboardingUseCase.Output(false) }.isDone
            if (!isOnboardingDone) {
                _state.update { it.copy(shouldRevealBackdrop = true) }
            }
            _state.value = EditRecipeTitleViewState(title, isExistingRecipe)
        }
    }

    fun onBackdropRevealed() {
        viewModelScope.launch {
            setOnboardingDoneUseCase.execute(null)
            _state.update { it.copy(shouldRevealBackdrop = false) }
        }
    }

    fun onTitleChanged(name: String) {
        _state.update { it.copy(title = name) }
    }

    fun onValidate() {
        viewModelScope.launch {
            setTitleUseCase(state.value.title).fold(
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
        _state.update { it.copy(showSaveDialog = false) }
        viewModelScope.launch {
            setTitleUseCase(state.value.title).fold(
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
            setTitleUseCase(state.value.title).fold(
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
}

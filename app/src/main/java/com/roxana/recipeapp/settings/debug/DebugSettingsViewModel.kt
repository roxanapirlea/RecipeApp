package com.roxana.recipeapp.settings.debug

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.debug.SetEditOnboardingValueUseCase
import com.roxana.recipeapp.domain.onboarding.GetEditOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebugSettingsViewModel @Inject constructor(
    private val getEditOnboardingUseCase: GetEditOnboardingUseCase,
    private val setEditOnboardingValueUseCase: SetEditOnboardingValueUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(DebugSettingsViewState())
    val state: StateFlow<DebugSettingsViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getEditOnboardingUseCase(null)
                .collect { result ->
                    val isDone = result.getOrDefault(GetEditOnboardingUseCase.Output(false)).isDone
                    _state.update { it.copy(isAddRecipeOnboardingDone = isDone) }
                }
        }
    }

    fun onSetAddRecipeOnboarding(isDone: Boolean) {
        viewModelScope.launch {
            setEditOnboardingValueUseCase(SetEditOnboardingValueUseCase.Input(isDone))
        }
    }
}

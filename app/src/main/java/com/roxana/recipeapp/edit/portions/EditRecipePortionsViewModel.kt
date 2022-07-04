package com.roxana.recipeapp.edit.portions

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetPortionsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SetPortionsUseCase
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
class EditRecipePortionsViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getPortionsUseCase: GetPortionsUseCase,
    private val setPortionsUseCase: SetPortionsUseCase,
    private val resetRecipeUseCase: ResetRecipeUseCase,
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipePortionsViewState())
    val state: StateFlow<EditRecipePortionsViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
            val portions = getPortionsUseCase(null).first().getOrNull()?.toString() ?: ""
            _state.value = EditRecipePortionsViewState(portions, isExistingRecipe)
        }
    }

    fun onPortionsChanged(name: String) {
        _state.update { it.copy(portions = name) }
    }

    fun onValidate() {
        viewModelScope.launch {
            setPortionsUseCase(state.value.portions.toShortOrNull()).fold(
                { sendForwardEvent() },
                { sendForwardEvent() }
            )
        }
    }

    private suspend fun sendForwardEvent() {
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
            setPortionsUseCase(state.value.portions.toShortOrNull()).fold(
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
            setPortionsUseCase(state.value.portions.toShortOrNull()).fold(
                { _state.update { it.copy(navigation = Navigation.ToPage(page)) } },
                { _state.update { it.copy(navigation = Navigation.ToPage(page)) } }
            )
        }
    }

    fun onNavigationDone() {
        _state.update { it.copy(navigation = null) }
    }
}

package com.roxana.recipeapp.edit.portions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetPortionsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
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
) : ViewModel() {
    private val _state = MutableStateFlow(EditRecipePortionsViewState())
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

    private fun sendForwardEvent() {
        val isExistingRecipe = state.value.isExistingRecipe
        if (isExistingRecipe)
            _state.update { it.copy(navigation = Navigation.ForwardEditing) }
        else
            _state.update { it.copy(navigation = Navigation.ForwardCreation) }
    }

    fun onBack() {
        viewModelScope.launch {
            setPortionsUseCase(state.value.portions.toShortOrNull()).fold(
                { _state.update { it.copy(navigation = Navigation.Back) } },
                { _state.update { it.copy(navigation = Navigation.Back) } }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setPortionsUseCase(state.value.portions.toShortOrNull()).fold(
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

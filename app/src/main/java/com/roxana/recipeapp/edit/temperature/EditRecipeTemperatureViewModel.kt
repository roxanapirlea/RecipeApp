package com.roxana.recipeapp.edit.temperature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetTemperatureUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetTemperatureUseCase
import com.roxana.recipeapp.domain.temperature.GetPreferredTemperatureUseCase
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.uimodel.UiTemperature
import com.roxana.recipeapp.uimodel.toDomainModel
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeTemperatureViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getTemperatureUseCase: GetTemperatureUseCase,
    private val setTemperatureUseCase: SetTemperatureUseCase,
    private val getPreferredTemperatureUseCase: GetPreferredTemperatureUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(EditRecipeTemperatureViewState())
    val state: StateFlow<EditRecipeTemperatureViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
            val temperature =
                getTemperatureUseCase(null).first().getOrNull()?.toString() ?: ""
            val temperatureUnit =
                getPreferredTemperatureUseCase(null).first().getOrNull()?.toUiModel()
            _state.value = EditRecipeTemperatureViewState(
                isExistingRecipe = isExistingRecipe,
                temperature = temperature,
                temperatureUnit = temperatureUnit ?: UiTemperature.Celsius
            )
        }
    }

    fun onTemperatureChanged(temperature: String) {
        _state.update { it.copy(temperature = temperature) }
    }

    fun onValidate() {
        viewModelScope.launch {
            setTemperatureUseCase(getInput()).fold(
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
            setTemperatureUseCase(getInput()).fold(
                { _state.update { it.copy(navigation = Navigation.Back) } },
                { _state.update { it.copy(navigation = Navigation.Back) } }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setTemperatureUseCase(getInput()).fold(
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

    private fun getInput(): SetTemperatureUseCase.Input =
        state.value.let {
            SetTemperatureUseCase.Input(
                it.temperature.toShortOrNull(),
                it.temperatureUnit.toDomainModel()
            )
        }
}

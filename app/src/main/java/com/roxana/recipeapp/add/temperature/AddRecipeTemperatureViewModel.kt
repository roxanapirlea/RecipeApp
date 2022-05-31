package com.roxana.recipeapp.add.temperature

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.add.PageType
import com.roxana.recipeapp.domain.addrecipe.GetTemperatureUseCase
import com.roxana.recipeapp.domain.addrecipe.SetTemperatureUseCase
import com.roxana.recipeapp.domain.temperature.GetPreferredTemperatureUseCase
import com.roxana.recipeapp.uimodel.UiTemperature
import com.roxana.recipeapp.uimodel.toDomainModel
import com.roxana.recipeapp.uimodel.toUiModel
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
class AddRecipeTemperatureViewModel @Inject constructor(
    private val getTemperatureUseCase: GetTemperatureUseCase,
    private val setTemperatureUseCase: SetTemperatureUseCase,
    private val getPreferredTemperatureUseCase: GetPreferredTemperatureUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(AddRecipeTemperatureViewState())
    val state: StateFlow<AddRecipeTemperatureViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<AddRecipeTemperatureSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val temperature =
                getTemperatureUseCase(null).first().getOrNull()?.toString() ?: ""
            val temperatureUnit =
                getPreferredTemperatureUseCase(null).first().getOrNull()?.toUiModel()
            _state.value = AddRecipeTemperatureViewState(
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
                { sideEffectChannel.send(Forward) },
                { sideEffectChannel.send(Forward) }
            )
        }
    }

    fun onSaveAndBack() {
        viewModelScope.launch {
            setTemperatureUseCase(getInput()).fold(
                { sideEffectChannel.send(Back) },
                { sideEffectChannel.send(Back) }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setTemperatureUseCase(getInput()).fold(
                { sideEffectChannel.send(NavigateToPage(page)) },
                { sideEffectChannel.send(NavigateToPage(page)) }
            )
        }
    }

    private fun getInput(): SetTemperatureUseCase.Input =
        state.value.let {
            SetTemperatureUseCase.Input(
                it.temperature.toShortOrNull(),
                it.temperatureUnit.toDomainModel()
            )
        }
}

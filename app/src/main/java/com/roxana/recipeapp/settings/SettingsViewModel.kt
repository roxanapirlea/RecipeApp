package com.roxana.recipeapp.settings

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.quantities.GetAllQuantityTypesUseCase
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val quantityTypesUseCase: GetAllQuantityTypesUseCase,
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(SettingsViewState())
    val state: StateFlow<SettingsViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val measuringUnits = quantityTypesUseCase(null).getOrNull()?.let { quantityTypes ->
                quantityTypes.map { MeasuringUnit(it.toUiModel()) }
            } ?: emptyList()
            _state.value = state.value.copy(
                measuringUnits = measuringUnits
            )
        }
    }

    fun onTemperatureSelected(type: UiTemperature) {
        _state.value = state.value.copy(selectedTemperature = type)
    }

    fun onMeasuringUnitChanged(unit: UiQuantityType, isChecked: Boolean) {
        _state.value = state.value.copy(
            measuringUnits = state.value.measuringUnits.map {
                if (it.unit == unit) it.copy(isChecked = isChecked) else it
            }
        )
    }
}

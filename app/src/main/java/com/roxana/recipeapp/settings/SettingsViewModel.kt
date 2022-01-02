package com.roxana.recipeapp.settings

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.quantities.GetAllQuantityTypesUseCase
import com.roxana.recipeapp.domain.quantities.GetPreferredQuantitiesUseCase
import com.roxana.recipeapp.domain.quantities.SavePreferredQuantitiesUseCase
import com.roxana.recipeapp.domain.temperature.GetPreferredTemperatureUseCase
import com.roxana.recipeapp.domain.temperature.SavePreferredTemperatureUseCase
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature
import com.roxana.recipeapp.uimodel.toDomainModel
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val quantityTypesUseCase: GetAllQuantityTypesUseCase,
    private val preferredQuantitiesUseCase: GetPreferredQuantitiesUseCase,
    private val preferredTemperatureUseCase: GetPreferredTemperatureUseCase,
    private val savePreferredQuantitiesUseCase: SavePreferredQuantitiesUseCase,
    private val savePreferredTemperatureUseCase: SavePreferredTemperatureUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(SettingsViewState())
    val state: StateFlow<SettingsViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferredTemperatureUseCase(null),
                preferredQuantitiesUseCase(null)
            ) { temperature, units ->
                val preferredUnits = units.getOrNull() ?: emptyList()
                val allUnits = quantityTypesUseCase(null).getOrNull()?.let { quantityTypes ->
                    quantityTypes.map { MeasuringUnit(it.toUiModel(), preferredUnits.contains(it)) }
                } ?: emptyList()
                val preferredTemperature =
                    temperature.getOrNull()?.toUiModel() ?: UiTemperature.Celsius
                SettingsViewState(
                    measuringUnits = allUnits,
                    temperatures = listOf(UiTemperature.Celsius, UiTemperature.Fahrenheit),
                    selectedTemperature = preferredTemperature
                )
            }.collect { state ->
                _state.value = state
            }
        }
    }

    fun onTemperatureSelected(type: UiTemperature) {
        viewModelScope.launch {
            savePreferredTemperatureUseCase(type.toDomainModel())
        }
    }

    fun onMeasuringUnitChanged(unit: UiQuantityType, isChecked: Boolean) {
        val measuringUnits = state.value.measuringUnits
            .map { if (it.unit == unit) it.copy(isChecked = isChecked) else it }
            .filter { it.isChecked }
            .mapNotNull { it.unit.toDomainModel() }
        viewModelScope.launch {
            savePreferredQuantitiesUseCase(measuringUnits)
        }
    }
}

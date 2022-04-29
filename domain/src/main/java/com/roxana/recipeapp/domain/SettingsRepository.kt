package com.roxana.recipeapp.domain

import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getPreferredTemperatureUnit(): Flow<Temperature>
    fun getSelectedMeasuringUnits(): Flow<List<QuantityType>>
    fun isAddOnboardingDone(): Flow<Boolean>
    suspend fun setPreferredTemperatureUnit(temperature: Temperature)
    suspend fun setPreferredMeasuringUnits(units: List<QuantityType>)
    suspend fun setAddOnboarding(isDone: Boolean)
}

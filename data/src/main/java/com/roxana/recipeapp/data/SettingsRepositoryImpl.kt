package com.roxana.recipeapp.data

import androidx.datastore.core.DataStore
import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: DataStore<Settings>
) : SettingsRepository {

    override fun getPreferredTemperatureUnit(): Flow<Temperature> =
        settingsDataStore.data.map { it.temperature.toDomainModel() }

    override fun getSelectedMeasuringUnits(): Flow<List<QuantityType>> =
        settingsDataStore.data.map { settings ->
            settings.unitsList.mapNotNull { it.toDomainModel() }
        }

    override suspend fun setPreferredTemperatureUnit(temperature: Temperature) {
        settingsDataStore.updateData { current ->
            current.toBuilder()
                .setTemperature(temperature.toProto())
                .build()
        }
    }

    override suspend fun setPreferredMeasuringUnits(units: List<QuantityType>) {
        settingsDataStore.updateData { current ->
            current.toBuilder()
                .clearUnits()
                .addAllUnits(units.map { it.toProto() })
                .build()
        }
    }
}

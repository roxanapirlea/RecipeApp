package com.roxana.recipeapp.data.settings

import androidx.datastore.core.DataStore
import com.roxana.recipeapp.data.Settings
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

    override fun isEditOnboardingDone(): Flow<Boolean> =
        settingsDataStore.data.map { settings ->
            settings.editOnboardingDone
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

    override suspend fun setEditOnboarding(isDone: Boolean) {
        settingsDataStore.updateData { current ->
            current.toBuilder()
                .setEditOnboardingDone(isDone)
                .build()
        }
    }
}

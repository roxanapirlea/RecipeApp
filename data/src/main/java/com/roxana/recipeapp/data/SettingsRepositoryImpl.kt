package com.roxana.recipeapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val DATA_STORE_FILE_NAME = "app_settings.pb"

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    private val Context.settingsDataStore: DataStore<Settings> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = SettingsSerializer
    )

    override fun getPreferredTemperatureUnit(): Flow<Temperature> =
        context.settingsDataStore.data.map { it.temperature.toDomainModel() }

    override fun getSelectedMeasuringUnits(): Flow<List<QuantityType>> =
        context.settingsDataStore.data.map { settings ->
            settings.unitsList.mapNotNull { it.toDomainModel() }
        }

    override suspend fun setPreferredTemperatureUnit(temperature: Temperature) {
        context.settingsDataStore.updateData { current ->
            current.toBuilder()
                .setTemperature(temperature.toProto())
                .build()
        }
    }

    override suspend fun setPreferredMeasuringUnits(units: List<QuantityType>) {
        context.settingsDataStore.updateData { current ->
            current.toBuilder()
                .clearUnits()
                .addAllUnits(units.map { it.toProto() })
                .build()
        }
    }
}

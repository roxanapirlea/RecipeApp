package com.roxana.recipeapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.roxana.recipeapp.domain.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val DATA_STORE_FILE_NAME = "app_settings.pb"

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    private val Context.settingsDataStore: DataStore<Settings> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = SettingsSerializer
    )
}

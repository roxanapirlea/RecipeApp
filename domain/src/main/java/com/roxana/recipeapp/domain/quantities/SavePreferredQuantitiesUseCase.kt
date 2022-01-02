package com.roxana.recipeapp.domain.quantities

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.QuantityType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavePreferredQuantitiesUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseSuspendableUseCase<List<QuantityType>, Unit>() {
    override suspend fun execute(input: List<QuantityType>) {
        withContext(Dispatchers.IO) {
            settingsRepository.setPreferredMeasuringUnits(input)
        }
    }
}

package com.roxana.recipeapp.domain.onboarding

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetAddOnboardingDoneUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseSuspendableUseCase<Any?, Unit>() {
    override suspend fun execute(input: Any?) {
        withContext(Dispatchers.IO) {
            settingsRepository.setAddOnboarding(true)
        }
    }
}
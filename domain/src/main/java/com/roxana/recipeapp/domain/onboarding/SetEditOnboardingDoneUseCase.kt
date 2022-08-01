package com.roxana.recipeapp.domain.onboarding

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.base.CommonDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetEditOnboardingDoneUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dispatchers: CommonDispatchers
) : BaseSuspendableUseCase<Any?, Unit>() {
    override suspend fun execute(input: Any?) {
        withContext(dispatchers.io) {
            settingsRepository.setEditOnboarding(true)
        }
    }
}

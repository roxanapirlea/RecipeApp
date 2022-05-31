package com.roxana.recipeapp.domain.debug

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetAddOnboardingValueUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseSuspendableUseCase<SetAddOnboardingValueUseCase.Input, Unit>() {
    override suspend fun execute(input: Input) {
        withContext(Dispatchers.IO) {
            settingsRepository.setAddOnboarding(input.isDone)
        }
    }

    data class Input(val isDone: Boolean)
}

package com.roxana.recipeapp.domain.debug

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.base.CommonDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetEditOnboardingValueUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dispatchers: CommonDispatchers
) : BaseSuspendableUseCase<SetEditOnboardingValueUseCase.Input, Unit>() {
    override suspend fun execute(input: Input) {
        withContext(dispatchers.io) {
            settingsRepository.setEditOnboarding(input.isDone)
        }
    }

    data class Input(val isDone: Boolean)
}

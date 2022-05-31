package com.roxana.recipeapp.domain.onboarding

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAddOnboardingUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseFlowUseCase<Any?, GetAddOnboardingUseCase.Output>() {

    override fun execute(input: Any?): Flow<Output> {
        return settingsRepository.isAddOnboardingDone()
            .map { Output(it) }
    }

    data class Output(val isDone: Boolean)
}

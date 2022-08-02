package com.roxana.recipeapp.domain.home.filters

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.CommonDispatchers
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMaxTimesUseCaseTest {
    private val recipeRepository: RecipeRepository = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val dispatchers = CommonDispatchers(
        main = testDispatcher,
        io = testDispatcher,
        default = testDispatcher
    )
    private lateinit var useCase: GetMaxTimesUseCase

    @Before
    fun setUp() {
        useCase = GetMaxTimesUseCase(recipeRepository, dispatchers)
    }

    @Test
    fun returnMaxTimes_when_launched() = runTest(testDispatcher) {
        // Given
        val maxPreparation: Short = 1
        val maxCooking: Short = 2
        val maxTotal: Short = 3
        every { recipeRepository.getMaxTotalTime() } returns flow { emit(maxTotal) }
        every { recipeRepository.getMaxPreparationTime() } returns flow { emit(maxPreparation) }
        every { recipeRepository.getMaxCookingTime() } returns flow { emit(maxCooking) }

        // When
        val result = useCase(null)

        // Then
        val expected = GetMaxTimesUseCase.Output(
            maxPreparation = maxPreparation,
            maxCooking = maxCooking,
            maxTotal = maxTotal
        )
        result.first() shouldBe Result.success(expected)
    }
}

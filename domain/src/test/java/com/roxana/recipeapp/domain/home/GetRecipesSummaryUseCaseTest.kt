package com.roxana.recipeapp.domain.home

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.CommonDispatchers
import com.roxana.recipeapp.domain.model.CategoryType
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetRecipesSummaryUseCaseTest {
    private val recipeRepository: RecipeRepository = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val dispatchers = CommonDispatchers(
        main = testDispatcher,
        io = testDispatcher,
        default = testDispatcher
    )
    private lateinit var useCase: GetRecipesSummaryUseCase

    @Before
    fun setUp() {
        useCase = GetRecipesSummaryUseCase(recipeRepository, dispatchers)
    }

    @Test
    fun callRepositoryWithQuery_when_launched_given_queryNoExtraSpace() = runTest(testDispatcher) {
        // Given
        val query = "Query"
        val input = GetRecipesSummaryUseCase.Input(
            query = query,
            totalTime = 1,
            cookingTime = 2,
            preparationTime = 3,
            category = CategoryType.BREAKFAST
        )

        // When
        useCase(input)

        // Then
        coVerify { recipeRepository.getRecipesSummary(query, 1, 2, 3, CategoryType.BREAKFAST) }
    }

    @Test
    fun callRepositoryWithTrimmedQuery_when_launched_given_queryWithExtraSpace() = runTest(testDispatcher) {
        // Given
        val query = "  Query  "
        val input = GetRecipesSummaryUseCase.Input(
            query = query,
            totalTime = 1,
            cookingTime = 2,
            preparationTime = 3,
            category = CategoryType.BREAKFAST
        )

        // When
        useCase(input)

        // Then
        val expectedQuery = "Query"
        coVerify { recipeRepository.getRecipesSummary(expectedQuery, 1, 2, 3, CategoryType.BREAKFAST) }
    }
}

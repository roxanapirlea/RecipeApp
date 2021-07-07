package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.Recipe
import com.roxana.recipeapp.domain.RecipeRepository
import io.kotest.matchers.result.shouldBeFailureOfType
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddRecipeUseCaseTest {
    private val recipeRepository: RecipeRepository = mockk(relaxed = true)
    private lateinit var useCase: AddRecipeUseCase

    @Before
    fun setUp() {
        useCase = AddRecipeUseCase(recipeRepository)
    }

    @Test
    fun returnSuccess_when_repositoryNoError() = runBlocking {
        // Given
        val recipe = Recipe(
            "fake",
            null,
            emptyList(),
            emptyList(),
            emptyList(),
            null,
            null,
            null,
            null,
            null,
            emptyList()
        )
        coEvery { recipeRepository.addRecipe(recipe) } just runs

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeSuccess()
        coVerify { recipeRepository.addRecipe(recipe) }
    }

    @Test
    fun returnFailure_when_repositoryThrowsError() = runBlocking {
        // Given
        val recipe = Recipe(
            "fake",
            null,
            emptyList(),
            emptyList(),
            emptyList(),
            null,
            null,
            null,
            null,
            null,
            emptyList()
        )
        val error = IllegalArgumentException("")
        coEvery { recipeRepository.addRecipe(recipe) } throws error

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeFailureOfType<IllegalArgumentException>()
        coVerify { recipeRepository.addRecipe(recipe) }
    }
}

package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.model.CreationRecipe
import io.kotest.matchers.result.shouldBeFailureOfType
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddRecipeUseCaseTest {
    private val recipeRepository: RecipeRepository = mockk(relaxed = true)
    private val creationRepository: RecipeCreationRepository = mockk(relaxed = true)
    private lateinit var useCase: AddRecipeUseCase

    @Before
    fun setUp() {
        useCase = AddRecipeUseCase(recipeRepository, creationRepository)
    }

    @Test
    fun returnSuccess_when_noError() = runBlocking {
        // Given
        val recipe = CreationRecipe(
            null,
            "fake",
            null,
            1,
            emptyList(),
            emptyList(),
            emptyList(),
            null,
            null,
            null,
            null,
            null,
            null,
            emptyList()
        )
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { recipeRepository.addRecipe(any()) } just runs
        coEvery { creationRepository.reset() } just runs

        // When
        val result = useCase(null)

        // Then
        result.shouldBeSuccess()
        coVerify { recipeRepository.addRecipe(recipe) }
        coVerify { creationRepository.reset() }
    }

    @Test
    fun returnFailure_when_getRecipeThrowsError() = runBlocking {
        // Given
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { throw error }
        coEvery { recipeRepository.addRecipe(any()) } just runs
        coEvery { creationRepository.reset() } just runs

        // When
        val result = useCase(null)

        // Then
        result.shouldBeFailureOfType<IllegalArgumentException>()
        coVerify(exactly = 0) { recipeRepository.addRecipe(any()) }
        coVerify(exactly = 0) { creationRepository.reset() }
    }

    @Test
    fun returnFailure_when_addRecipeThrowsError() = runBlocking {
        // Given
        val recipe = CreationRecipe(
            null,
            "fake",
            null,
            1,
            emptyList(),
            emptyList(),
            emptyList(),
            null,
            null,
            null,
            null,
            null,
            null,
            emptyList()
        )
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { recipeRepository.addRecipe(recipe) } throws error
        coEvery { creationRepository.reset() } just runs

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeFailureOfType<IllegalArgumentException>()
        coVerify(exactly = 0) { creationRepository.reset() }
    }

    @Test
    fun returnSuccess_when_resetThrowsError() = runBlocking {
        // Given
        val recipe = CreationRecipe(
            null,
            "fake",
            null,
            1,
            emptyList(),
            emptyList(),
            emptyList(),
            null,
            null,
            null,
            null,
            null,
            null,
            emptyList()
        )
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { recipeRepository.addRecipe(recipe) } just runs
        coEvery { creationRepository.reset() } throws error

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeSuccess()
    }
}

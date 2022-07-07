package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.PhotoRepository
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

class SaveRecipeUseCaseTest {
    private val recipeRepository: RecipeRepository = mockk(relaxed = true)
    private val creationRepository: RecipeCreationRepository = mockk(relaxed = true)
    private val photoRepository: PhotoRepository = mockk(relaxed = true)
    private lateinit var useCase: SaveRecipeUseCase

    @Before
    fun setUp() {
        useCase = SaveRecipeUseCase(recipeRepository, creationRepository, photoRepository)
    }

    private val baseCreationRecipe = CreationRecipe(
        id = null,
        name = "fake",
        photoPath = null,
        portions = 1,
        categories = emptyList(),
        instructions = emptyList(),
        ingredients = emptyList(),
        timeTotal = null,
        timePreparation = null,
        timeCooking = null,
        timeWaiting = null,
        temperature = null,
        temperatureUnit = null,
        comments = emptyList()
    )

    private val newCreationRecipe = baseCreationRecipe
    private val existingCreationRecipe = baseCreationRecipe.copy(id = 123)

    @Test
    fun returnSuccess_when_noError_given_newRecipe() = runBlocking {
        // Given
        val recipe = newCreationRecipe
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
    fun returnSuccess_when_noError_given_existingRecipe() = runBlocking {
        // Given
        val recipe = existingCreationRecipe
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { recipeRepository.updateRecipe(any()) } just runs
        coEvery { creationRepository.reset() } just runs

        // When
        val result = useCase(null)

        // Then
        result.shouldBeSuccess()
        coVerify { recipeRepository.updateRecipe(existingCreationRecipe) }
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
    fun returnFailure_when_addRecipeThrowsError_given_newRecipe() = runBlocking {
        // Given
        val recipe = newCreationRecipe
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
    fun returnFailure_when_addRecipeThrowsError_given_existingRecipe() = runBlocking {
        // Given
        val recipe = existingCreationRecipe
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { recipeRepository.updateRecipe(any()) } throws error
        coEvery { creationRepository.reset() } just runs

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeFailureOfType<IllegalArgumentException>()
        coVerify(exactly = 0) { creationRepository.reset() }
    }

    @Test
    fun returnSuccess_when_resetThrowsError_given_newRecipe() = runBlocking {
        // Given
        val recipe = newCreationRecipe
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { recipeRepository.addRecipe(recipe) } just runs
        coEvery { creationRepository.reset() } throws error

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeSuccess()
    }

    @Test
    fun returnSuccess_when_resetThrowsError_given_existingRecipe() = runBlocking {
        // Given
        val recipe = existingCreationRecipe
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { recipeRepository.updateRecipe(any()) } just runs
        coEvery { creationRepository.reset() } throws error

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeSuccess()
    }
}

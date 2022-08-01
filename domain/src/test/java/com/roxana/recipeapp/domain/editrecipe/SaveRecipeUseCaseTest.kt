package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.PhotoRepository
import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.models.fakeCreationRecipe
import io.kotest.matchers.result.shouldBeFailureOfType
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveRecipeUseCaseTest {
    private val recipeRepository: RecipeRepository = mockk(relaxed = true)
    private val creationRepository: RecipeCreationRepository = mockk(relaxed = true)
    private val photoRepository: PhotoRepository = mockk(relaxed = true)
    private lateinit var useCase: SaveRecipeUseCase

    @Before
    fun setUp() {
        useCase =
            SaveRecipeUseCase(recipeRepository, creationRepository, photoRepository)
    }

    private val newCreationRecipe = fakeCreationRecipe
    private val existingCreationRecipe = fakeCreationRecipe.copy(id = 123)

    @Test
    fun returnSuccess_when_noError_given_newRecipe() = runTest {
        // Given
        val recipe = newCreationRecipe
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }

        val photoPath = "new photo path"
        coEvery { photoRepository.copyTempFileToPermFile(any()) } returns photoPath

        // When
        val result = useCase(null)

        // Then
        result.shouldBeSuccess()
        coVerify { photoRepository.copyTempFileToPermFile(recipe.photoPath!!) }
        val expectedRecipe = recipe.copy(photoPath = photoPath)
        coVerify { recipeRepository.addRecipe(expectedRecipe) }
        coVerify { creationRepository.reset() }
    }

    @Test
    fun returnSuccess_when_noError_given_existingRecipe() = runTest {
        // Given
        val recipe = existingCreationRecipe
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        val photoPath = "new photo path"
        coEvery { photoRepository.copyTempFileToPermFile(any()) } returns photoPath

        // When
        val result = useCase(null)

        // Then
        result.shouldBeSuccess()
        coVerify { photoRepository.copyTempFileToPermFile(recipe.photoPath!!) }
        val expectedRecipe = recipe.copy(photoPath = photoPath)
        coVerify { recipeRepository.updateRecipe(expectedRecipe) }
        coVerify { creationRepository.reset() }
    }

    @Test
    fun returnFailure_when_getRecipeThrowsError() = runTest {
        // Given
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { throw error }

        // When
        val result = useCase(null)

        // Then
        result.shouldBeFailureOfType<IllegalArgumentException>()
        coVerify(exactly = 0) { photoRepository.copyTempFileToPermFile(any()) }
        coVerify(exactly = 0) { recipeRepository.addRecipe(any()) }
        coVerify(exactly = 0) { creationRepository.reset() }
    }

    @Test
    fun returnFailure_when_addRecipeThrowsError_given_newRecipe() = runTest {
        // Given
        val recipe = newCreationRecipe
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { recipeRepository.addRecipe(any()) } throws error

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeFailureOfType<IllegalArgumentException>()
        coVerify { photoRepository.copyTempFileToPermFile(recipe.photoPath!!) }
        coVerify(exactly = 0) { creationRepository.reset() }
    }

    @Test
    fun returnFailure_when_addRecipeThrowsError_given_existingRecipe() = runTest {
        // Given
        val recipe = existingCreationRecipe
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { recipeRepository.updateRecipe(any()) } throws error

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeFailureOfType<IllegalArgumentException>()
        coVerify { photoRepository.copyTempFileToPermFile(recipe.photoPath!!) }
        coVerify(exactly = 0) { creationRepository.reset() }
    }

    @Test
    fun returnSuccess_when_resetThrowsError_given_newRecipe() = runTest {
        // Given
        val recipe = newCreationRecipe
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { creationRepository.reset() } throws error

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeSuccess()
    }

    @Test
    fun returnSuccess_when_resetThrowsError_given_existingRecipe() = runTest {
        // Given
        val recipe = existingCreationRecipe
        val error = IllegalArgumentException("")
        coEvery { creationRepository.getRecipe() } returns flow { emit(recipe) }
        coEvery { creationRepository.reset() } throws error

        // When
        val result = useCase(recipe)

        // Then
        result.shouldBeSuccess()
    }
}

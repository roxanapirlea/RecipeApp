package com.roxana.recipeapp.domain.detail

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.CommonDispatchers
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.models.fakeRecipe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StartRecipeEditingUseCaseTest {
    private val recipeRepository: RecipeRepository = mockk(relaxed = true)
    private val creationRepository: RecipeCreationRepository = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val dispatchers = CommonDispatchers(
        main = testDispatcher,
        io = testDispatcher,
        default = testDispatcher
    )
    private lateinit var useCase: StartRecipeEditingUseCase

    @Before
    fun setUp() {
        useCase = StartRecipeEditingUseCase(recipeRepository, creationRepository, dispatchers)
    }

    @Test
    fun setCreationRecipeWithRightRecipe_when_noError() = runTest(testDispatcher) {
        // Given
        val id = 1234
        val recipe = fakeRecipe.copy(id = id)
        coEvery { recipeRepository.getRecipeById(id) } returns recipe

        // When
        useCase(id)

        // Then
        val expected = CreationRecipe(
            id = recipe.id,
            name = recipe.name,
            photoPath = recipe.photoPath,
            portions = recipe.portions,
            categories = recipe.categories,
            instructions = recipe.instructions.map { CreationInstruction(it.name, it.ordinal) },
            ingredients = recipe.ingredients
                .map { CreationIngredient(it.id, it.name, it.quantity, it.quantityType) },
            timeTotal = recipe.timeTotal,
            timeCooking = recipe.timeCooking,
            timePreparation = recipe.timePreparation,
            timeWaiting = recipe.timeWaiting,
            temperature = recipe.temperature,
            temperatureUnit = recipe.temperatureUnit,
            comments = recipe.comments.map { CreationComment(it.name, it.ordinal) }
        )
        coVerify { creationRepository.setRecipe(expected) }
    }

    @Test
    fun notSetCreationRecipe_when_error() = runTest(testDispatcher) {
        // Given
        val id = 1234
        val error = IllegalArgumentException()
        coEvery { recipeRepository.getRecipeById(id) } throws error

        // When
        useCase(id)

        // Then
        coVerify(exactly = 0) { creationRepository.setRecipe(any()) }
    }
}

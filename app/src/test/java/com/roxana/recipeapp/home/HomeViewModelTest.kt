package com.roxana.recipeapp.home

import com.roxana.recipeapp.domain.home.GetRecipesSummaryUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.RecipeSummary
import com.roxana.recipeapp.helpers.MainCoroutineRule
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val getRecipesSummaryUseCase: GetRecipesSummaryUseCase = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    @Test
    fun setEmptyState_when_init_given_useCaseEmptyList() = runBlockingTest {
        // Given
        every {
            getRecipesSummaryUseCase(null)
        } returns flow { emit(Result.success(emptyList<RecipeSummary>())) }

        // When
        viewModel = HomeViewModel(getRecipesSummaryUseCase)

        // Then
        viewModel.state.value shouldBe HomeViewState.Empty
    }

    @Test
    fun setContentState_when_init_given_useCaseList() = runBlockingTest {
        // Given
        val recipe1 = RecipeSummary(
            0,
            "Crepes",
            listOf(CategoryType.BREAKFAST, CategoryType.DESSERT)
        )
        val recipe2 = RecipeSummary(
            1,
            "Omlette",
            listOf(CategoryType.BREAKFAST)
        )

        every {
            getRecipesSummaryUseCase(null)
        } returns flow { emit(Result.success(listOf(recipe1, recipe2))) }

        // When
        viewModel = HomeViewModel(getRecipesSummaryUseCase)

        // Then
        viewModel.state.value shouldBe HomeViewState.Content(
            listOf(
                RecipeState(0, "Crepes", listOf(CategoryType.BREAKFAST, CategoryType.DESSERT)),
                RecipeState(1, "Omlette", listOf(CategoryType.BREAKFAST))
            )
        )
    }
}

package com.roxana.recipeapp.home

import com.roxana.recipeapp.domain.editrecipe.GetAvailableCategoriesUseCase
import com.roxana.recipeapp.domain.home.GetRecipesSummaryUseCase
import com.roxana.recipeapp.domain.home.filters.GetMaxTimesUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.RecipeSummary
import com.roxana.recipeapp.helpers.MainCoroutineRule
import com.roxana.recipeapp.uimodel.UiCategoryType
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
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
    private val getCategoriesUseCase: GetAvailableCategoriesUseCase = mockk(relaxed = true)
    private val getMaxTimesUseCase: GetMaxTimesUseCase = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    @Test
    fun setEmptyState_when_init_given_useCaseEmptyList() = runBlockingTest {
        // Given
        every {
            getRecipesSummaryUseCase(any())
        } returns flow { emit(Result.success(emptyList())) }
        coEvery {
            getCategoriesUseCase(null)
        } returns Result.success(listOf(CategoryType.BREAKFAST))
        every {
            getMaxTimesUseCase(null)
        } returns flow { emit(Result.success(GetMaxTimesUseCase.Output(null, null, null))) }

        // When
        viewModel =
            HomeViewModel(getRecipesSummaryUseCase, getCategoriesUseCase, getMaxTimesUseCase)

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
            getRecipesSummaryUseCase(any())
        } returns flow { emit(Result.success(listOf(recipe1, recipe2))) }
        coEvery {
            getCategoriesUseCase(null)
        } returns Result.success(listOf(CategoryType.BREAKFAST))
        every {
            getMaxTimesUseCase(null)
        } returns flow { emit(Result.success(GetMaxTimesUseCase.Output(1, 2, 3))) }

        // When
        viewModel =
            HomeViewModel(getRecipesSummaryUseCase, getCategoriesUseCase, getMaxTimesUseCase)

        // Then
        viewModel.state.value shouldBe HomeViewState.Content(
            listOf(
                RecipeState(0, "Crepes", listOf(UiCategoryType.Breakfast, UiCategoryType.Dessert)),
                RecipeState(1, "Omlette", listOf(UiCategoryType.Breakfast))
            ),
            false,
            FiltersState(
                maxTotalTime = 1,
                maxPreparationTime = 2,
                maxCookingTime = 3,
                categories = listOf(UiCategoryType.Breakfast),
                selectedTotalTime = 1,
                selectedPreparationTime = 2,
                selectedCookingTime = 3,
                selectedCategory = null
            ),
            0
        )
    }
}

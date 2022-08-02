package com.roxana.recipeapp.edit.categories

import com.roxana.recipeapp.domain.editrecipe.GetAvailableCategoriesUseCase
import com.roxana.recipeapp.domain.editrecipe.GetCategoriesUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetCategoriesUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.helpers.MainCoroutineRule
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.toUiModel
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditRecipeCategoriesViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private val isExistingUC: IsRecipeExistingUseCase = mockk(relaxed = true)
    private val getAvailableCatUC: GetAvailableCategoriesUseCase = mockk(relaxed = true)
    private val getCatUC: GetCategoriesUseCase = mockk(relaxed = true)
    private val setCatUC: SetCategoriesUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRecipeCategoriesViewModel

    private val available =
        listOf(CategoryType.BREAKFAST, CategoryType.LUNCH, CategoryType.DINNER, CategoryType.SNACK)
    private val selected = setOf(CategoryType.DINNER)

    @Before
    fun setUp() {
        coEvery { isExistingUC(null) } returns Result.success(true)
        coEvery { getAvailableCatUC(null) } returns Result.success(available)
        every { getCatUC(null) } returns flow { emit(Result.success(selected)) }
    }

    @Test
    fun setExistingRecipe_when_init_given_existingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(true)

        // When
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeTrue()
    }

    @Test
    fun setNewRecipe_when_init_given_noExistingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)

        // When
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setNewRecipe_when_init_given_existingRecipeError() {
        // Given
        coEvery {
            isExistingUC(null)
        } returns Result.failure(IllegalAccessException())

        // When
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setCategories_when_init_given_noError() {
        // When
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)

        // Then
        viewModel.state.value.categories shouldContainExactlyInAnyOrder listOf(
            CategoryState(CategoryType.BREAKFAST.toUiModel(), false),
            CategoryState(CategoryType.LUNCH.toUiModel(), false),
            CategoryState(CategoryType.DINNER.toUiModel(), true),
            CategoryState(CategoryType.SNACK.toUiModel(), false)
        )
    }

    @Test
    fun setCategories_when_init_given_selectedError() {
        // Given
        every {
            getCatUC(null)
        } returns flow { emit(Result.failure(IllegalStateException())) }

        // When
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)

        // Then
        viewModel.state.value.categories shouldContainExactlyInAnyOrder listOf(
            CategoryState(CategoryType.BREAKFAST.toUiModel(), false),
            CategoryState(CategoryType.LUNCH.toUiModel(), false),
            CategoryState(CategoryType.DINNER.toUiModel(), false),
            CategoryState(CategoryType.SNACK.toUiModel(), false)
        )
    }

    @Test
    fun setEmptyCategories_when_init_given_categoriesError() {
        // Given
        coEvery {
            getAvailableCatUC(null)
        } returns Result.failure(IllegalStateException())

        // When
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)

        // Then
        viewModel.state.value.categories.shouldBeEmpty()
    }

    @Test
    fun toggleSelection_when_onCategoryClicked() {
        // Given
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)

        // When - then
        viewModel.onCategoryClicked(UiCategoryType.Snack)
        viewModel.state.value.categories shouldContain CategoryState(UiCategoryType.Snack, true)
        viewModel.onCategoryClicked(UiCategoryType.Snack)
        viewModel.state.value.categories shouldContain CategoryState(UiCategoryType.Snack, false)
    }

    @Test
    fun saveSelectionAndNavigate_when_onValidate_given_noErrorAndExistingRecipe() {
        // Given
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)
        coEvery { setCatUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setCatUC(setOf(CategoryType.DINNER)) }
    }

    @Test
    fun saveSelectionAndNavigate_when_onValidate_given_noErrorAndNewRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)
        coEvery { setCatUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardCreation
        coVerify { setCatUC(setOf(CategoryType.DINNER)) }
    }

    @Test
    fun saveSelectionAndNavigate_when_onValidate_given_error() {
        // Given
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)
        coEvery { setCatUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setCatUC(setOf(CategoryType.DINNER)) }
    }

    @Test
    fun saveSelectionAndBack_when_onBack_given_noError() {
        // Given
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)
        coEvery { setCatUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setCatUC(setOf(CategoryType.DINNER)) }
    }

    @Test
    fun saveSelectionAndBack_when_onBack_given_error() {
        // Given
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)
        coEvery { setCatUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setCatUC(setOf(CategoryType.DINNER)) }
    }

    @Test
    fun saveSelectionAndToPage_when_onSelectPage_given_noError() {
        // Given
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)
        coEvery { setCatUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setCatUC(setOf(CategoryType.DINNER)) }
    }

    @Test
    fun saveSelectionAndBack_when_onSelectPage_given_error() {
        // Given
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)
        coEvery { setCatUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setCatUC(setOf(CategoryType.DINNER)) }
    }

    @Test
    fun resetNavigation_when_onNavigationDone() {
        // Given
        viewModel =
            EditRecipeCategoriesViewModel(isExistingUC, getAvailableCatUC, getCatUC, setCatUC)
        coEvery { setCatUC(any()) } returns Result.success(Unit)

        // When - then
        viewModel.onBack()
        viewModel.state.value.navigation shouldBe Navigation.Back
        viewModel.onNavigationDone()
        viewModel.state.value.navigation.shouldBeNull()
    }
}

package com.roxana.recipeapp.edit.portions

import com.roxana.recipeapp.domain.editrecipe.GetPortionsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetPortionsUseCase
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.helpers.MainCoroutineRule
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
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
class EditRecipePortionsViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val isExistingUC: IsRecipeExistingUseCase = mockk(relaxed = true)
    private val getPortionsUC: GetPortionsUseCase = mockk(relaxed = true)
    private val setPortionsUC: SetPortionsUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRecipePortionsViewModel

    private val portions: Short = 2

    @Before
    fun setUp() {
        coEvery { isExistingUC(null) } returns Result.success(true)
        every { getPortionsUC(null) } returns flow { emit(Result.success(portions)) }
    }

    @Test
    fun setExistingRecipe_when_init_given_existingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(true)

        // When
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeTrue()
    }

    @Test
    fun setNewRecipe_when_init_given_noExistingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)

        // When
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)

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
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setPortions_when_init() {
        // Given
        coEvery { getPortionsUC(null) } returns flow { emit(Result.success(portions)) }

        // When
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)

        // Then
        viewModel.state.value.portions shouldBe "$portions"
    }

    @Test
    fun setNoPortions_when_init_given_error() {
        // Given
        coEvery {
            getPortionsUC(null)
        } returns flow { emit(Result.failure(IllegalStateException())) }

        // When
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)

        // Then
        viewModel.state.value.portions.shouldBeEmpty()
    }

    @Test
    fun changePortion_when_onPortionsChanged() {
        // Given
        val newPortions = "10"
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)

        // When
        viewModel.onPortionsChanged(newPortions)

        // Then
        viewModel.state.value.portions shouldBe newPortions
    }

    @Test
    fun goForwardAndSaveState_when_onValidate_given_noError() {
        // Given
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)
        coEvery { setPortionsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setPortionsUC(portions) }
    }

    @Test
    fun goForward_when_onValidate_given_error() {
        // Given
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)
        coEvery { setPortionsUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
    }

    @Test
    fun resetNavigation_when_onNavigationDone() {
        // Given
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)
        coEvery { setPortionsUC(any()) } returns Result.failure(IllegalStateException())

        // When - then
        viewModel.onValidate()
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        viewModel.onNavigationDone()
        viewModel.state.value.navigation.shouldBeNull()
    }

    @Test
    fun goBackAndSaveState_when_onBack_given_noError() {
        // Given
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)
        coEvery { setPortionsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setPortionsUC(portions) }
    }

    @Test
    fun goBack_when_onBack_given_error() {
        // Given
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)
        coEvery { setPortionsUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noError() {
        // Given
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)
        coEvery { setPortionsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setPortionsUC(portions) }
    }

    @Test
    fun goToPage_when_onSelectPage_given_error() {
        // Given
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)
        coEvery { setPortionsUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel = EditRecipePortionsViewModel(isExistingUC, getPortionsUC, setPortionsUC)
        coEvery { setPortionsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, false)
        coVerify { setPortionsUC(portions) }
    }
}

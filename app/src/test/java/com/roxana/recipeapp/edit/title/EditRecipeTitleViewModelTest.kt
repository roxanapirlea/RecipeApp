package com.roxana.recipeapp.edit.title

import com.roxana.recipeapp.domain.editrecipe.GetTitleUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SetTitleUseCase
import com.roxana.recipeapp.domain.onboarding.GetEditOnboardingUseCase
import com.roxana.recipeapp.domain.onboarding.SetEditOnboardingDoneUseCase
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
class EditRecipeTitleViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val isExistingUC: IsRecipeExistingUseCase = mockk(relaxed = true)
    private val getTitleUC: GetTitleUseCase = mockk(relaxed = true)
    private val setTitleUC: SetTitleUseCase = mockk(relaxed = true)
    private val resetRecipeUC: ResetRecipeUseCase = mockk(relaxed = true)
    private val onboardUC: GetEditOnboardingUseCase = mockk(relaxed = true)
    private val setOnboardDoneUC: SetEditOnboardingDoneUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRecipeTitleViewModel

    private val title = "Title"

    @Before
    fun setUp() {
        coEvery { isExistingUC(null) } returns Result.success(true)
        every { getTitleUC(null) } returns flow { emit(Result.success(title)) }
        every { onboardUC(null) } returns flow {
            emit(Result.success(GetEditOnboardingUseCase.Output(true)))
        }
    }

    @Test
    fun setExistingRecipe_when_init_given_existingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(true)

        // When
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeTrue()
    }

    @Test
    fun setNewRecipe_when_init_given_noExistingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)

        // When
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

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
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setTitle_when_init() {
        // Given
        coEvery { getTitleUC(null) } returns flow { emit(Result.success(title)) }

        // When
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

        // Then
        viewModel.state.value.title shouldBe title
    }

    @Test
    fun setNoTitle_when_init_given_error() {
        // Given
        coEvery {
            getTitleUC(null)
        } returns flow { emit(Result.failure(IllegalStateException())) }

        // When
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

        // Then
        viewModel.state.value.title.shouldBeEmpty()
    }

    @Test
    fun setOnboarding_when_init() {
        // Given
        coEvery { onboardUC(null) } returns flow {
            emit(Result.success(GetEditOnboardingUseCase.Output(true)))
        }

        // When
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

        // Then
        viewModel.state.value.shouldRevealBackdrop.shouldBeFalse()
    }

    @Test
    fun setOnboardingNotDone_when_init_given_error() {
        // Given
        coEvery {
            onboardUC(null)
        } returns flow { emit(Result.failure(IllegalStateException())) }

        // When
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

        // Then
        viewModel.state.value.shouldRevealBackdrop.shouldBeTrue()
    }

    @Test
    fun resetBackdrop_when_onBackdropRevealed() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { setOnboardDoneUC(null) } returns Result.success(Unit)

        // When
        viewModel.onBackdropRevealed()

        // Then
        viewModel.state.value.shouldRevealBackdrop.shouldBeFalse()
    }

    @Test
    fun changeTitle_when_onTitleChanged() {
        // Given
        val newTitle = "New title"
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

        // When
        viewModel.onTitleChanged(newTitle)

        // Then
        viewModel.state.value.title shouldBe newTitle
    }

    @Test
    fun goForwardAndSaveState_when_onValidate_given_noError() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { setTitleUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setTitleUC(title) }
    }

    @Test
    fun goForward_when_onValidate_given_error() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { setTitleUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
    }

    @Test
    fun resetNavigation_when_onNavigationDone() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { setTitleUC(any()) } returns Result.failure(IllegalStateException())

        // When - then
        viewModel.onValidate()
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        viewModel.onNavigationDone()
        viewModel.state.value.navigation.shouldBeNull()
    }

    @Test
    fun showCloseDialog_when_onCheckShouldClose() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

        // When
        viewModel.onCheckShouldClose()

        // Then
        viewModel.state.value.showSaveDialog.shouldBeTrue()
    }

    @Test
    fun dismissCloseDialog_when_onDismissDialog() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )

        // When - then
        viewModel.onCheckShouldClose()
        viewModel.state.value.showSaveDialog.shouldBeTrue()
        viewModel.onDismissDialog()
        viewModel.state.value.showSaveDialog.shouldBeFalse()
    }

    @Test
    fun closeAndSaveState_when_onSaveAndClose_given_noError() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { setTitleUC(any()) } returns Result.success(Unit)
        viewModel.onCheckShouldClose()

        // When
        viewModel.onSaveAndClose()

        // Then
        viewModel.state.value.showSaveDialog.shouldBeFalse()
        viewModel.state.value.navigation shouldBe Navigation.Close
        coVerify { setTitleUC(title) }
        coVerify(exactly = 0) { resetRecipeUC(null) }
    }

    @Test
    fun close_when_onSaveAndClose_given_error() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { setTitleUC(any()) } returns Result.failure(IllegalStateException())
        viewModel.onCheckShouldClose()

        // When
        viewModel.onSaveAndClose()

        // Then
        viewModel.state.value.showSaveDialog.shouldBeFalse()
        viewModel.state.value.navigation shouldBe Navigation.Close
        coVerify(exactly = 0) { resetRecipeUC(null) }
    }

    @Test
    fun closeAndResetState_when_onResetAndClose_given_noError() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { resetRecipeUC(null) } returns Result.success(Unit)
        viewModel.onCheckShouldClose()

        // When
        viewModel.onResetAndClose()

        // Then
        viewModel.state.value.showSaveDialog.shouldBeFalse()
        viewModel.state.value.navigation shouldBe Navigation.Close
        coVerify(exactly = 0) { setTitleUC(title) }
        coVerify { resetRecipeUC(null) }
    }

    @Test
    fun close_when_onResetAndClose_given_error() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { resetRecipeUC(any()) } returns Result.failure(IllegalStateException())
        viewModel.onCheckShouldClose()

        // When
        viewModel.onResetAndClose()

        // Then
        viewModel.state.value.showSaveDialog.shouldBeFalse()
        viewModel.state.value.navigation shouldBe Navigation.Close
        coVerify(exactly = 0) { setTitleUC(title) }
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noError() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { setTitleUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setTitleUC(title) }
    }

    @Test
    fun goToPage_when_onSelectPage_given_error() {
        // Given
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { setTitleUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel = EditRecipeTitleViewModel(
            isExistingUC,
            getTitleUC,
            setTitleUC,
            resetRecipeUC,
            onboardUC,
            setOnboardDoneUC,
        )
        coEvery { setTitleUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, false)
        coVerify { setTitleUC(title) }
    }
}

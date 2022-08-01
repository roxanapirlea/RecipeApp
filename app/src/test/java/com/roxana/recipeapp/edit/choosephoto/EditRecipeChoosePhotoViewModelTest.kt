package com.roxana.recipeapp.edit.choosephoto

import com.roxana.recipeapp.domain.editrecipe.ClearPhotoUseCase
import com.roxana.recipeapp.domain.editrecipe.GetPhotoUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetPhotoUseCase
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.helpers.MainCoroutineRule
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
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
class EditRecipeChoosePhotoViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private val isExistingUC: IsRecipeExistingUseCase = mockk(relaxed = true)
    private val getPhotoUC: GetPhotoUseCase = mockk(relaxed = true)
    private val setPhotoUC: SetPhotoUseCase = mockk(relaxed = true)
    private val clearPhotoUC: ClearPhotoUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRecipeChoosePhotoViewModel

    private val path = "path"

    @Before
    fun setUp() {
        coEvery { isExistingUC(null) } returns Result.success(true)
        every { getPhotoUC(null) } returns flow { emit(Result.success(path)) }
    }

    @Test
    fun setExistingRecipe_when_init_given_existingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(true)

        // When
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeTrue()
    }

    @Test
    fun setNewRecipe_when_init_given_noExistingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)

        // When
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)

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
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setPhoto_when_init_given_photo() {
        // Given
        coEvery { getPhotoUC(null) } returns flow { emit(Result.success(path)) }

        // When
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)

        // Then
        viewModel.state.value.photoPath shouldBe path
    }

    @Test
    fun setNoPhoto_when_init_given_noPhoto() {
        // Given
        coEvery { getPhotoUC(null) } returns flow { emit(Result.success(null)) }

        // When
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)

        // Then
        viewModel.state.value.photoPath.shouldBeNull()
    }

    @Test
    fun setNoPhoto_when_init_given_error() {
        // Given
        coEvery {
            getPhotoUC(null)
        } returns flow { emit(Result.failure(IllegalAccessException())) }

        // When
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)

        // Then
        viewModel.state.value.photoPath.shouldBeNull()
    }

    @Test
    fun clearPhoto_when_onClearPhoto() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { clearPhotoUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onClearPhoto()

        // Then
        viewModel.state.value.navigation.shouldBeNull()
        coVerify { clearPhotoUC(path) }
    }

    @Test
    fun clearPhotoAndNavigate_when_onRecapturePhoto_given_noError() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { clearPhotoUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onRecapturePhoto()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.PhotoCapture
        coVerify { clearPhotoUC(path) }
    }

    @Test
    fun clearPhotoAndNavigate_when_onRecapturePhoto_given_error() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { clearPhotoUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onRecapturePhoto()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.PhotoCapture
        coVerify { clearPhotoUC(path) }
    }

    @Test
    fun savePhotoAndNavigate_when_onValidate_given_noErrorPhotoAndExistingRecipe() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setPhotoUC(path) }
    }

    @Test
    fun savePhotoAndNavigate_when_onValidate_given_noErrorPhotoAndNewRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardCreation
        coVerify { setPhotoUC(path) }
    }

    @Test
    fun savePhotoAndNavigate_when_onValidate_given_errorAndPhoto() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setPhotoUC(path) }
    }

    @Test
    fun navigate_when_onValidate_given_noErrorAndNoPhoto() {
        // Given
        every { getPhotoUC(null) } returns flow { emit(Result.success(null)) }
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.success(Unit)

        // When - then
        viewModel.onClearPhoto()
        viewModel.state.value.photoPath.shouldBeNull()
        viewModel.onValidate()
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify(exactly = 0) { setPhotoUC(path) }
    }

    @Test
    fun savePhotoAndBack_when_onBack_given_noErrorAndPhoto() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setPhotoUC(path) }
    }

    @Test
    fun savePhotoAndBack_when_onBack_given_errorAndPhoto() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setPhotoUC(path) }
    }

    @Test
    fun goBack_when_onBack_given_noErrorAndNoPhoto() {
        // Given
        every { getPhotoUC(null) } returns flow { emit(Result.success(null)) }
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.success(Unit)

        // When - then
        viewModel.onClearPhoto()
        viewModel.state.value.photoPath.shouldBeNull()
        viewModel.onBack()
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify(exactly = 0) { setPhotoUC(path) }
    }

    @Test
    fun savePhotoAndToPage_when_onSelectPage_given_noErrorAndPhoto() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setPhotoUC(path) }
    }

    @Test
    fun savePhotoAndBack_when_onSelectPage_given_errorAndPhoto() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setPhotoUC(path) }
    }

    @Test
    fun goToPage_when_onSelectPage_given_noErrorAndNoPhoto() {
        // Given
        every { getPhotoUC(null) } returns flow { emit(Result.success(null)) }
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(any()) } returns Result.success(Unit)

        // When - then
        viewModel.onClearPhoto()
        viewModel.state.value.photoPath.shouldBeNull()
        viewModel.onSelectPage(PageType.Ingredients)
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify(exactly = 0) { setPhotoUC(path) }
    }

    @Test
    fun resetNavigation_when_onNavigationDone() {
        // Given
        viewModel =
            EditRecipeChoosePhotoViewModel(isExistingUC, getPhotoUC, setPhotoUC, clearPhotoUC)
        coEvery { setPhotoUC(path) } returns Result.success(Unit)

        // When - then
        viewModel.onBack()
        viewModel.state.value.navigation shouldBe Navigation.Back
        viewModel.onNavigationDone()
        viewModel.state.value.navigation.shouldBeNull()
    }
}

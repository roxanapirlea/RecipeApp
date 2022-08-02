package com.roxana.recipeapp.edit.temperature

import com.roxana.recipeapp.domain.editrecipe.GetTemperatureUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetTemperatureUseCase
import com.roxana.recipeapp.domain.model.Temperature
import com.roxana.recipeapp.domain.temperature.GetPreferredTemperatureUseCase
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.helpers.MainCoroutineRule
import com.roxana.recipeapp.uimodel.UiTemperature
import com.roxana.recipeapp.uimodel.toUiModel
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
class EditRecipeTemperatureViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val isExistingUC: IsRecipeExistingUseCase = mockk(relaxed = true)
    private val getTempUC: GetTemperatureUseCase = mockk(relaxed = true)
    private val setTempUC: SetTemperatureUseCase = mockk(relaxed = true)
    private val getPrefTempUC: GetPreferredTemperatureUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRecipeTemperatureViewModel

    private val prefTempUnit = Temperature.CELSIUS
    private val temperature: Short = 75

    @Before
    fun setUp() {
        coEvery { isExistingUC(null) } returns Result.success(true)
        every { getTempUC(null) } returns flow { emit(Result.success(temperature)) }
        every { getPrefTempUC(null) } returns flow { emit(Result.success(prefTempUnit)) }
    }

    @Test
    fun setExistingRecipe_when_init_given_existingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(true)

        // When
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeTrue()
    }

    @Test
    fun setNewRecipe_when_init_given_noExistingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)

        // When
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)

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
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setTemp_when_init() {
        // Given
        coEvery { getTempUC(null) } returns flow { emit(Result.success(temperature)) }

        // When
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)

        // Then
        viewModel.state.value.temperature shouldBe "$temperature"
    }

    @Test
    fun setNoTemp_when_init_given_error() {
        // Given
        coEvery {
            getTempUC(null)
        } returns flow { emit(Result.failure(IllegalStateException())) }

        // When
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)

        // Then
        viewModel.state.value.temperature.shouldBeEmpty()
    }

    @Test
    fun setPrefTempUnit_when_init() {
        // Given
        coEvery { getPrefTempUC(null) } returns flow { emit(Result.success(prefTempUnit)) }

        // When
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)

        // Then
        viewModel.state.value.temperatureUnit shouldBe prefTempUnit.toUiModel()
    }

    @Test
    fun setDefaultTempUnit_when_init_given_error() {
        // Given
        coEvery {
            getPrefTempUC(null)
        } returns flow { emit(Result.failure(IllegalStateException())) }

        // When
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)

        // Then
        viewModel.state.value.temperatureUnit shouldBe UiTemperature.Celsius
    }

    @Test
    fun changeTemperature_when_onTemperatureChanged() {
        // Given
        val newTemp = "10"
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)

        // When
        viewModel.onTemperatureChanged(newTemp)

        // Then
        viewModel.state.value.temperature shouldBe newTemp
    }

    @Test
    fun goForwardAndSaveState_when_onValidate_given_noError() {
        // Given
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)
        coEvery { setTempUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setTempUC(SetTemperatureUseCase.Input(temperature, prefTempUnit)) }
    }

    @Test
    fun goForward_when_onValidate_given_error() {
        // Given
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)
        coEvery { setTempUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
    }

    @Test
    fun resetNavigation_when_onNavigationDone() {
        // Given
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)
        coEvery { setTempUC(any()) } returns Result.failure(IllegalStateException())

        // When - then
        viewModel.onValidate()
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        viewModel.onNavigationDone()
        viewModel.state.value.navigation.shouldBeNull()
    }

    @Test
    fun goBackAndSaveState_when_onBack_given_noError() {
        // Given
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)
        coEvery { setTempUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setTempUC(SetTemperatureUseCase.Input(temperature, prefTempUnit)) }
    }

    @Test
    fun goBack_when_onBack_given_error() {
        // Given
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)
        coEvery { setTempUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noError() {
        // Given
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)
        coEvery { setTempUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setTempUC(SetTemperatureUseCase.Input(temperature, prefTempUnit)) }
    }

    @Test
    fun goToPage_when_onSelectPage_given_error() {
        // Given
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)
        coEvery { setTempUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel =
            EditRecipeTemperatureViewModel(isExistingUC, getTempUC, setTempUC, getPrefTempUC)
        coEvery { setTempUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, false)
        coVerify { setTempUC(SetTemperatureUseCase.Input(temperature, prefTempUnit)) }
    }
}

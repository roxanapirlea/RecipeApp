package com.roxana.recipeapp.edit.time

import com.roxana.recipeapp.domain.editrecipe.GetCookingTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.GetPreparationTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.GetTotalTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.GetWaitingTimeUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetTimeUseCase
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
class EditRecipeTimeViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val isExistingUC: IsRecipeExistingUseCase = mockk(relaxed = true)
    private val getCookingUC: GetCookingTimeUseCase = mockk(relaxed = true)
    private val getPrepUC: GetPreparationTimeUseCase = mockk(relaxed = true)
    private val getWaitingUC: GetWaitingTimeUseCase = mockk(relaxed = true)
    private val getTotalUC: GetTotalTimeUseCase = mockk(relaxed = true)
    private val setTimeUC: SetTimeUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRecipeTimeViewModel

    private val total: Short = 4
    private val cooking: Short = 1
    private val prep: Short = 2
    private val waiting: Short = 3

    @Before
    fun setUp() {
        coEvery { isExistingUC(null) } returns Result.success(true)
        every { getCookingUC(null) } returns flow { emit(Result.success(cooking)) }
        every { getPrepUC(null) } returns flow { emit(Result.success(prep)) }
        every { getWaitingUC(null) } returns flow { emit(Result.success(waiting)) }
        every { getTotalUC(null) } returns flow { emit(Result.success(total)) }
    }

    @Test
    fun setExistingRecipe_when_init_given_existingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(true)

        // When
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeTrue()
    }

    @Test
    fun setNewRecipe_when_init_given_noExistingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)

        // When
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
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
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setTime_when_init() {
        // When
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )

        // Then
        viewModel.state.value.total shouldBe "$total"
        viewModel.state.value.cooking shouldBe "$cooking"
        viewModel.state.value.preparation shouldBe "$prep"
        viewModel.state.value.waiting shouldBe "$waiting"
    }

    @Test
    fun setNoTime_when_init_given_error() {
        // Given
        every { getCookingUC(null) } returns flow { emit(Result.failure(IllegalStateException())) }
        every { getPrepUC(null) } returns flow { emit(Result.failure(IllegalStateException())) }
        every { getWaitingUC(null) } returns flow { emit(Result.failure(IllegalStateException())) }
        every { getTotalUC(null) } returns flow { emit(Result.failure(IllegalStateException())) }

        // When
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )

        // Then
        viewModel.state.value.total shouldBe ""
        viewModel.state.value.cooking shouldBe ""
        viewModel.state.value.preparation shouldBe ""
        viewModel.state.value.waiting shouldBe ""
    }

    @Test
    fun changeTotal_when_onTotalChanged() {
        // Given
        val newTotal = "10"
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )

        // When
        viewModel.onTotalChanged(newTotal)

        // Then
        viewModel.state.value.total shouldBe newTotal
    }

    @Test
    fun changeCooking_when_onCookingChanged() {
        // Given
        val newCooking = "10"
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )

        // When
        viewModel.onCookingChanged(newCooking)

        // Then
        viewModel.state.value.cooking shouldBe newCooking
    }

    @Test
    fun changePrep_when_onPrepChanged() {
        // Given
        val newPrep = "10"
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )

        // When
        viewModel.onPreparationChanged(newPrep)

        // Then
        viewModel.state.value.preparation shouldBe newPrep
    }

    @Test
    fun changeWaiting_when_onWaitingChanged() {
        // Given
        val newWaiting = "10"
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )

        // When
        viewModel.onWaitingChanged(newWaiting)

        // Then
        viewModel.state.value.waiting shouldBe newWaiting
    }

    @Test
    fun computeTotal_when_onComputeTotal() {
        // Given
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )

        // When - then
        viewModel.onComputeTotal()
        viewModel.state.value.total shouldBe (cooking + waiting + prep).toString()

        viewModel.onCookingChanged("")
        viewModel.onComputeTotal()
        viewModel.state.value.total shouldBe (0 + waiting + prep).toString()

        viewModel.onWaitingChanged("")
        viewModel.onComputeTotal()
        viewModel.state.value.total shouldBe (0 + prep).toString()

        viewModel.onPreparationChanged("")
        viewModel.onComputeTotal()
        viewModel.state.value.total shouldBe 0.toString()
    }

    @Test
    fun goForwardAndSaveState_when_onValidate_given_noError() {
        // Given
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )
        coEvery { setTimeUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setTimeUC(SetTimeUseCase.Input(cooking, prep, waiting, total)) }
    }

    @Test
    fun goForward_when_onValidate_given_error() {
        // Given
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )
        coEvery { setTimeUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
    }

    @Test
    fun resetNavigation_when_onNavigationDone() {
        // Given
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )
        coEvery { setTimeUC(any()) } returns Result.failure(IllegalStateException())

        // When - then
        viewModel.onValidate()
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        viewModel.onNavigationDone()
        viewModel.state.value.navigation.shouldBeNull()
    }

    @Test
    fun goBackAndSaveState_when_onBack_given_noError() {
        // Given
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )
        coEvery { setTimeUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setTimeUC(SetTimeUseCase.Input(cooking, prep, waiting, total)) }
    }

    @Test
    fun goBack_when_onBack_given_error() {
        // Given
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )
        coEvery { setTimeUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noError() {
        // Given
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )
        coEvery { setTimeUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setTimeUC(SetTimeUseCase.Input(cooking, prep, waiting, total)) }
    }

    @Test
    fun goToPage_when_onSelectPage_given_error() {
        // Given
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )
        coEvery { setTimeUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel = EditRecipeTimeViewModel(
            isExistingUC,
            getCookingUC,
            getPrepUC,
            getWaitingUC,
            getTotalUC,
            setTimeUC
        )
        coEvery { setTimeUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, false)
        coVerify { setTimeUC(SetTimeUseCase.Input(cooking, prep, waiting, total)) }
    }
}

package com.roxana.recipeapp.edit.ingredients

import com.roxana.recipeapp.domain.editrecipe.GetIngredientsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetIngredientsUseCase
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.quantities.GetAllQuantityTypesUseCase
import com.roxana.recipeapp.domain.quantities.GetPreferredQuantitiesUseCase
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.helpers.MainCoroutineRule
import com.roxana.recipeapp.uimodel.UiQuantityType
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
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
class EditRecipeIngredientsViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val isExistingUC: IsRecipeExistingUseCase = mockk(relaxed = true)
    private val getPrefQuantitiesUC: GetPreferredQuantitiesUseCase = mockk(relaxed = true)
    private val getAllQuantitiesUC: GetAllQuantityTypesUseCase = mockk(relaxed = true)
    private val getIngrUC: GetIngredientsUseCase = mockk(relaxed = true)
    private val setIngrUC: SetIngredientsUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRecipeIngredientsViewModel

    private val ingredientsState =
        listOf(IngredientState(0, "ingr", "2", false, UiQuantityType.Cup))
    private val ingredients = listOf(CreationIngredient(0, "ingr", 2.0, QuantityType.CUP))

    @Before
    fun setUp() {
        coEvery { isExistingUC(null) } returns Result.success(true)
        every {
            getPrefQuantitiesUC(null)
        } returns flow { emit(Result.success(listOf(QuantityType.CUP))) }
        coEvery {
            getAllQuantitiesUC(null)
        } returns Result.success(listOf(QuantityType.CUP, QuantityType.LITER))
        every { getIngrUC(null) } returns flow { emit(Result.success(ingredients)) }
    }

    @Test
    fun setExistingRecipe_when_init_given_existingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(true)

        // When
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeTrue()
    }

    @Test
    fun setNewRecipe_when_init_given_noExistingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)

        // When
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
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
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setPreferredQuantities_when_init_given_preferredQuantities() {
        // When
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )

        // Then
        viewModel.state.value.quantityTypes shouldBe listOf(UiQuantityType.Cup)
    }

    @Test
    fun setAllQuantities_when_init_given_noPreferredQuantities() {
        // Given
        every {
            getPrefQuantitiesUC(null)
        } returns flow { emit(Result.success(listOf())) }

        // When
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )

        // Then
        viewModel.state.value.quantityTypes shouldContainExactlyInAnyOrder listOf(
            UiQuantityType.Cup,
            UiQuantityType.Liter
        )
    }

    @Test
    fun setIngredients_when_init() {
        // When
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )

        // Then
        viewModel.state.value.ingredients shouldBe ingredientsState
    }

    @Test
    fun changeIngredientName_when_onIngredientNameChanged() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )

        // When - then
        viewModel.onIngredientNameChanged("name")
        viewModel.state.value.editingIngredient.name shouldBe "name"
        viewModel.state.value.canAddIngredient.shouldBeTrue()
        viewModel.onIngredientNameChanged(" ")
        viewModel.state.value.editingIngredient.name shouldBe " "
        viewModel.state.value.canAddIngredient.shouldBeFalse()
    }

    @Test
    fun changeIngredientQuantity_when_onIngredientQuantityChanged() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        viewModel.onIngredientNameChanged("name")

        // When - then
        viewModel.onIngredientQuantityChanged("3.0")
        viewModel.state.value.editingIngredient.quantity shouldBe "3.0"
        viewModel.state.value.editingIngredient.isQuantityError.shouldBeFalse()
        viewModel.state.value.canAddIngredient.shouldBeTrue()

        viewModel.onIngredientQuantityChanged("...")
        viewModel.state.value.editingIngredient.quantity shouldBe "..."
        viewModel.state.value.editingIngredient.isQuantityError.shouldBeTrue()
        viewModel.state.value.canAddIngredient.shouldBeFalse()
    }

    @Test
    fun changeIngredientQuantityType_when_onIngredientQuantityTypeChanged() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )

        // When - then
        viewModel.onIngredientQuantityTypeChanged(UiQuantityType.Liter)
        viewModel.state.value.editingIngredient.quantityType shouldBe UiQuantityType.Liter
    }

    @Test
    fun saveIngredient_when_onSaveIngredient_given_validIngredient() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        viewModel.onIngredientNameChanged("name")

        // When - then
        viewModel.onSaveIngredient()
        viewModel.state.value.editingIngredient shouldBe IngredientState()
        viewModel.state.value.canAddIngredient.shouldBeFalse()
        viewModel.state.value.ingredients shouldContain IngredientState(name = "name")
    }

    @Test
    fun goForwardAndSaveState_when_onValidate_given_noError() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        coEvery { setIngrUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setIngrUC(ingredients) }
    }

    @Test
    fun goForward_when_onValidate_given_error() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        coEvery { setIngrUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
    }

    @Test
    fun resetNavigation_when_onNavigationDone() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        coEvery { setIngrUC(any()) } returns Result.failure(IllegalStateException())

        // When - then
        viewModel.onValidate()
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        viewModel.onNavigationDone()
        viewModel.state.value.navigation.shouldBeNull()
    }

    @Test
    fun goBackAndSaveState_when_onBack_given_noError() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        coEvery { setIngrUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setIngrUC(ingredients) }
    }

    @Test
    fun goBack_when_onBack_given_error() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        coEvery { setIngrUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noError() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        coEvery { setIngrUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setIngrUC(ingredients) }
    }

    @Test
    fun goToPage_when_onSelectPage_given_error() {
        // Given
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        coEvery { setIngrUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel = EditRecipeIngredientsViewModel(
            isExistingUC,
            getPrefQuantitiesUC,
            getAllQuantitiesUC,
            getIngrUC,
            setIngrUC
        )
        coEvery { setIngrUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, false)
        coVerify { setIngrUC(ingredients) }
    }
}

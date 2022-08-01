package com.roxana.recipeapp.cooking.ingredients

import androidx.lifecycle.SavedStateHandle
import com.roxana.recipeapp.VaryIngredientQuantitiesNode
import com.roxana.recipeapp.domain.detail.GetRecipeByIdUseCase
import com.roxana.recipeapp.domain.model.Ingredient
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.helpers.MainCoroutineRule
import com.roxana.recipeapp.helpers.fakeEmptyRecipe
import com.roxana.recipeapp.uimodel.toUiModel
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VaryIngredientsViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val getRecipeByIdUseCase: GetRecipeByIdUseCase = mockk(relaxed = true)
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private lateinit var viewModel: VaryIngredientsViewModel

    private val recipeId = 1234
    private val ingr1 = Ingredient(1, "ingr1", 2.0, QuantityType.CUP)
    private val ingr2 = Ingredient(2, "ingr2", 1.0, null)
    private val ingr3 = Ingredient(3, "ingr3", null, null)
    private val recipe =
        fakeEmptyRecipe.copy(id = recipeId, ingredients = listOf(ingr1, ingr2, ingr3))

    @Before
    fun setUp() {
        every {
            savedStateHandle.get<Int>(VaryIngredientQuantitiesNode.KEY_RECIPE_ID)!!
        } returns recipeId
        coEvery { getRecipeByIdUseCase(any()) } returns Result.success(recipe)

        viewModel = VaryIngredientsViewModel(savedStateHandle, getRecipeByIdUseCase)
    }

    @Test
    fun setQuantifiableIngredients_when_init() {
        // Then
        viewModel.state.value shouldBe VaryIngredientsState(
            listOf(
                IngredientState(1, "ingr1", 2.0, "2.0", ingr1.quantityType.toUiModel()),
                IngredientState(2, "ingr2", 1.0, "1.0", ingr2.quantityType.toUiModel())
            )
        )
    }

    @Test
    fun setSelectedIngredient_when_onIngredientSelected() {
        // When - then
        viewModel.onIngredientSelected(ingr1.id)
        viewModel.state.value.updatedIngredient shouldBe IngredientState(
            1,
            "ingr1",
            2.0,
            "2.0",
            ingr1.quantityType.toUiModel()
        )
        viewModel.onIngredientSelected(null)
        viewModel.state.value.updatedIngredient.shouldBeNull()
    }

    @Test
    fun setNoErrorQuantity_when_onIngredientQuantityChanged_given_DoubleQuantity() {
        // Given
        val quantity = "2.7"
        viewModel.onIngredientSelected(ingr1.id)

        // When
        viewModel.onIngredientQuantityChanged(quantity)

        // Then
        viewModel.state.value.updatedIngredient!!.isQuantityInError.shouldBeFalse()
        viewModel.state.value.updatedIngredient!!.quantityText shouldBe quantity
    }

    @Test
    fun setErrorQuantity_when_onIngredientQuantityChanged_given_textQuantity() {
        // Given
        val quantity = "qwerty"
        viewModel.onIngredientSelected(ingr1.id)

        // When
        viewModel.onIngredientQuantityChanged(quantity)

        // Then
        viewModel.state.value.updatedIngredient!!.isQuantityInError.shouldBeTrue()
        viewModel.state.value.updatedIngredient!!.quantityText shouldBe quantity
    }

    @Test
    fun doNothing_when_onValidate_given_noUpdatedIngredient() {
        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.updatedIngredient.shouldBeNull()
        viewModel.state.value.validation.shouldBeNull()
    }

    @Test
    fun doNothing_when_onValidate_given_quantityError() {
        // Given
        val quantity = "qwerty"
        viewModel.onIngredientSelected(ingr1.id)
        viewModel.onIngredientQuantityChanged(quantity)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.updatedIngredient.shouldNotBeNull()
        viewModel.state.value.updatedIngredient!!.isQuantityInError.shouldBeTrue()
        viewModel.state.value.validation.shouldBeNull()
    }

    @Test
    fun setValidation_when_onValidate_given_noQuantityError() {
        // Given
        val multiplier = 2.0
        val quantity = ingr1.quantity!! * multiplier
        viewModel.onIngredientSelected(ingr1.id)
        viewModel.onIngredientQuantityChanged(quantity.toString())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.updatedIngredient.shouldNotBeNull()
        viewModel.state.value.updatedIngredient!!.isQuantityInError.shouldBeFalse()
        viewModel.state.value.validation shouldBe Validation(recipeId, multiplier)
    }

    @Test
    fun setValidationNull_when_onValidateDone() {
        // Given
        val multiplier = 2.0
        val quantity = ingr1.quantity!! * multiplier
        viewModel.onIngredientSelected(ingr1.id)
        viewModel.onIngredientQuantityChanged(quantity.toString())

        // When - then
        viewModel.onValidate()
        viewModel.state.value.validation shouldBe Validation(recipeId, multiplier)
        viewModel.onValidateDone()
        viewModel.state.value.validation.shouldBeNull()
    }
}

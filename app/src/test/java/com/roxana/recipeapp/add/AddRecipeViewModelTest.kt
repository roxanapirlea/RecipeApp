package com.roxana.recipeapp.add

import com.roxana.recipeapp.domain.addrecipe.AddRecipeUseCase
import com.roxana.recipeapp.domain.addrecipe.GetAvailableCategoriesUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature
import com.roxana.recipeapp.domain.quantities.GetPreferredQuantitiesUseCase
import com.roxana.recipeapp.domain.temperature.GetPreferredTemperatureUseCase
import com.roxana.recipeapp.helpers.MainCoroutineRule
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class AddRecipeViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val getCategoriesUseCase: GetAvailableCategoriesUseCase = mockk(relaxed = true)
    private val getQuantityTypesUseCase: GetPreferredQuantitiesUseCase = mockk(relaxed = true)
    private val addRecipeUseCase: AddRecipeUseCase = mockk(relaxed = true)
    private val getTemperatureUseCase: GetPreferredTemperatureUseCase = mockk(relaxed = true)

    private lateinit var viewModel: AddRecipeViewModel

    @Before
    fun setUp() {
        mockkStatic("com.roxana.recipeapp.add.StateToRecipeMapperKt")
        coEvery {
            getCategoriesUseCase.invoke(null)
        } returns Result.success(listOf(CategoryType.BREAKFAST, CategoryType.DESSERT))
        every {
            getQuantityTypesUseCase.invoke(null)
        } returns flow { emit(Result.success(listOf(QuantityType.TABLESPOON, QuantityType.CUP))) }
        every {
            getTemperatureUseCase(null)
        } returns flow { emit(Result.success(Temperature.CELSIUS)) }

        viewModel =
            AddRecipeViewModel(
                getCategoriesUseCase,
                getQuantityTypesUseCase,
                addRecipeUseCase,
                getTemperatureUseCase
            )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun setTitleAndValid_when_onTitleChanged_given_titleFilled() {
        // Given
        val title = "fake title"

        // When
        viewModel.onTitleChanged(title)

        // Then
        viewModel.state.value.title.text shouldBe title
        viewModel.state.value.title.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setTitleAndInvalid_when_onTitleChanged_given_titleEmpty() {
        // Given
        val title = ""

        // When
        viewModel.onTitleChanged(title)

        // Then
        viewModel.state.value.title.text shouldBe title
        viewModel.state.value.title.isValid.shouldBeFalse()
        viewModel.state.value.isValid.shouldBeFalse()
    }

    @Test
    fun setCategorySelected_when_onCategoryClicked_given_wasNotSelected() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                categories = listOf(
                    CategoryState(UiCategoryType.Breakfast, false),
                    CategoryState(UiCategoryType.Dessert, false)
                )
            )
        val categoryType = UiCategoryType.Breakfast

        // When
        viewModel.onCategoryClicked(categoryType)

        // Then
        viewModel.state.value.categories.shouldContainAll(
            CategoryState(UiCategoryType.Breakfast, true),
            CategoryState(UiCategoryType.Dessert, false)
        )
    }

    @Test
    fun setCategoryUnselected_when_onCategoryClicked_given_wasSelected() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                categories = listOf(
                    CategoryState(UiCategoryType.Breakfast, true),
                    CategoryState(UiCategoryType.Dessert, false)
                )
            )
        val categoryType = UiCategoryType.Breakfast

        // When
        viewModel.onCategoryClicked(categoryType)

        // Then
        viewModel.state.value.categories.shouldContainAll(
            CategoryState(UiCategoryType.Breakfast, false),
            CategoryState(UiCategoryType.Dessert, false)
        )
    }

    @Test
    fun setPotionsAndValid_when_onPortionsChanged_given_portionsEmpty() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val portions = ""

        // When
        viewModel.onPortionsChanged(portions)

        // Then
        viewModel.state.value.portions.text shouldBe portions
        viewModel.state.value.portions.value.shouldBeNull()
        viewModel.state.value.portions.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setPotionsAndValid_when_onPortionsChanged_given_portionsValid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val portions = "12"

        // When
        viewModel.onPortionsChanged(portions)

        // Then
        viewModel.state.value.portions.text shouldBe portions
        viewModel.state.value.portions.value shouldBe 12
        viewModel.state.value.portions.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setPotionsAndInvalid_when_onPortionsChanged_given_portionsInvalid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val portions = "12./asd"

        // When
        viewModel.onPortionsChanged(portions)

        // Then
        viewModel.state.value.portions.text shouldBe portions
        viewModel.state.value.portions.value shouldBe null
        viewModel.state.value.portions.isValid.shouldBeFalse()
        viewModel.state.value.isValid.shouldBeFalse()
    }

    @Test
    fun addIngredient_when_onAddIngredient() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(IngredientState(EmptyFieldState("ingr1")))
            )

        // When
        viewModel.onAddIngredient()

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(2)
        viewModel.state.value.ingredients.shouldContainInOrder(
            IngredientState(EmptyFieldState("ingr1")),
            IngredientState(isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun removeEmptyIngredient_when_onAddIngredient_given_emptyEditingIngredient() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(
                    IngredientState(isEditing = true),
                    IngredientState(EmptyFieldState("ingr1"))
                )
            )

        // When
        viewModel.onAddIngredient()

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(2)
        viewModel.state.value.ingredients.shouldContainInOrder(
            IngredientState(EmptyFieldState("ingr1")),
            IngredientState(isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun removeIngredient_when_onDeleteIngredient() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(
                    IngredientState(EmptyFieldState("ingr1")),
                    IngredientState(EmptyFieldState("ingr2")),
                    IngredientState(EmptyFieldState("ingr3"))
                )
            )

        // When
        viewModel.onDeleteIngredient(1)

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(2)
        viewModel.state.value.ingredients.shouldContainInOrder(
            IngredientState(EmptyFieldState("ingr1")),
            IngredientState(EmptyFieldState("ingr3"))
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun editIngredient_when_onIngredientClicked() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(
                    IngredientState(EmptyFieldState("ingr1"), isEditing = true),
                    IngredientState(EmptyFieldState("ingr2")),
                    IngredientState(EmptyFieldState("ingr3"))
                )
            )

        // When
        viewModel.onIngredientClicked(1)

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(3)
        viewModel.state.value.ingredients.shouldContainInOrder(
            IngredientState(EmptyFieldState("ingr1"), isEditing = false),
            IngredientState(EmptyFieldState("ingr2"), isEditing = true),
            IngredientState(EmptyFieldState("ingr3"))
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun removeEmptyIngredient_when_onIngredientClicked_given_emptyEditingIngredient() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(
                    IngredientState(isEditing = true),
                    IngredientState(EmptyFieldState("ingr1"))
                )
            )

        // When
        viewModel.onIngredientClicked(1)

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(1)
        viewModel.state.value.ingredients.shouldContain(
            IngredientState(EmptyFieldState("ingr1"), isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setIngredientName_when_onIngredientNameChanged() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(IngredientState())
            )
        val name = "fake ingredient"

        // When
        viewModel.onIngredientNameChanged(0, name)

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(1)
        viewModel.state.value.ingredients.first().name shouldBe EmptyFieldState(name)
        viewModel.state.value.ingredients.first().isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setIngredientQuantityAndValid_when_onIngredientQuantityChanged_given_validQuantity() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(
                    IngredientState(),
                    IngredientState(EmptyFieldState("ingr1"))
                )
            )
        val quantity = "15.6"

        // When
        viewModel.onIngredientQuantityChanged(0, quantity)

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(2)
        viewModel.state.value.ingredients.shouldContainInOrder(
            IngredientState(quantity = DoubleFieldState(quantity, 15.6)),
            IngredientState(EmptyFieldState("ingr1"))
        )
        viewModel.state.value.ingredients.first().quantity.isValid.shouldBeTrue()
        viewModel.state.value.ingredients.first().isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setIngredientQuantityAndInvalid_when_onIngredientQuantityChanged_given_invalidQuantity() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(IngredientState())
            )
        val quantity = "15qwdsx"

        // When
        viewModel.onIngredientQuantityChanged(0, quantity)

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(1)
        viewModel.state.value.ingredients.first()
            .quantity shouldBe DoubleFieldState(quantity, null)
        viewModel.state.value.ingredients.first().quantity.isValid.shouldBeFalse()
        viewModel.state.value.ingredients.first().isValid.shouldBeFalse()
        viewModel.state.value.isValid.shouldBeFalse()
    }

    @Test
    fun setQuantityType_when_onQuantityTypeChanged_given_notIsLastAndCompletelyFilled() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(
                    IngredientState(),
                    IngredientState(EmptyFieldState("ingr1"))
                )
            )
        val quantityType = UiQuantityType.Cup

        // When
        viewModel.onIngredientQuantityTypeChanged(0, quantityType)

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(2)
        viewModel.state.value.ingredients.shouldContainInOrder(
            IngredientState(quantityType = quantityType, isEditing = true),
            IngredientState(EmptyFieldState("ingr1"))
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun addNewIngredient_when_onQuantityTypeChanged_given_notIsLastAndCompletelyFilled() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                ingredients = listOf(
                    IngredientState(),
                    IngredientState(
                        EmptyFieldState("ingr1"),
                        DoubleFieldState("2.0", 2.0)
                    )
                )
            )
        val quantityType = UiQuantityType.Cup

        // When
        viewModel.onIngredientQuantityTypeChanged(1, quantityType)

        // Then
        viewModel.state.value.ingredients.shouldHaveSize(3)
        viewModel.state.value.ingredients.shouldContainInOrder(
            IngredientState(),
            IngredientState(
                EmptyFieldState("ingr1"),
                DoubleFieldState("2.0", 2.0),
                quantityType,
                isEditing = false
            ),
            IngredientState(isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun addInstruction_when_onAddInstruction() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                instructions = listOf(EditingState(EmptyFieldState("instr1")))
            )

        // When
        viewModel.onAddInstruction()

        // Then
        viewModel.state.value.instructions.shouldHaveSize(2)
        viewModel.state.value.instructions.shouldContainInOrder(
            EditingState(EmptyFieldState("instr1"), isEditing = false),
            EditingState(isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun removeEmptyInstruction_when_onAddInstruction_given_emptyEditingInstruction() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                instructions = listOf(
                    EditingState(isEditing = true),
                    EditingState(EmptyFieldState("instr1"))
                )
            )

        // When
        viewModel.onAddInstruction()

        // Then
        viewModel.state.value.instructions.shouldHaveSize(2)
        viewModel.state.value.instructions.shouldContainInOrder(
            EditingState(EmptyFieldState("instr1"), isEditing = false),
            EditingState(isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun removeInstruction_when_onDeleteInstruction() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                instructions = listOf(
                    EditingState(EmptyFieldState("instr1")),
                    EditingState(EmptyFieldState("instr2")),
                    EditingState(EmptyFieldState("instr3"))
                )
            )

        // When
        viewModel.onDeleteInstruction(1)

        // Then
        viewModel.state.value.instructions.shouldHaveSize(2)
        viewModel.state.value.instructions.shouldContainInOrder(
            EditingState(EmptyFieldState("instr1")),
            EditingState(EmptyFieldState("instr3"))
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun editInstruction_when_onInstructionClicked() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                instructions = listOf(
                    EditingState(EmptyFieldState("instr1"), isEditing = true),
                    EditingState(EmptyFieldState("instr2")),
                    EditingState(EmptyFieldState("instr3"))
                )
            )

        // When
        viewModel.onInstructionClicked(1)

        // Then
        viewModel.state.value.instructions.shouldHaveSize(3)
        viewModel.state.value.instructions.shouldContainInOrder(
            EditingState(EmptyFieldState("instr1"), isEditing = false),
            EditingState(EmptyFieldState("instr2"), isEditing = true),
            EditingState(EmptyFieldState("instr3"), isEditing = false)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun removeEmptyInstruction_when_onInstructionClicked_given_emptyEditingInstruction() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                instructions = listOf(
                    EditingState(isEditing = true),
                    EditingState(EmptyFieldState("instr1"))
                )
            )

        // When
        viewModel.onInstructionClicked(1)

        // Then
        viewModel.state.value.instructions.shouldHaveSize(1)
        viewModel.state.value.instructions.shouldContain(
            EditingState(EmptyFieldState("instr1"), isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setInstruction_when_onInstructionChanged() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                instructions = listOf(EditingState())
            )
        val instruction = "fake instruction"

        // When
        viewModel.onInstructionChanged(0, instruction)

        // Then
        viewModel.state.value.instructions.shouldHaveSize(1)
        viewModel.state.value.instructions.first().fieldState shouldBe EmptyFieldState(instruction)
        viewModel.state.value.instructions.first().fieldState.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun addComment_when_onAddComment() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                comments = listOf(EditingState(EmptyFieldState("comm1")))
            )

        // When
        viewModel.onAddComment()

        // Then
        viewModel.state.value.comments.shouldHaveSize(2)
        viewModel.state.value.comments.shouldContainInOrder(
            EditingState(EmptyFieldState("comm1"), isEditing = false),
            EditingState(isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun removeEmptyComment_when_onAddComment_given_emptyEditingComment() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                comments = listOf(
                    EditingState(isEditing = true),
                    EditingState(EmptyFieldState("comm1"))
                )
            )

        // When
        viewModel.onAddComment()

        // Then
        viewModel.state.value.comments.shouldHaveSize(2)
        viewModel.state.value.comments.shouldContainInOrder(
            EditingState(EmptyFieldState("comm1"), isEditing = false),
            EditingState(isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun removeComment_when_onDeleteComment() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                comments = listOf(
                    EditingState(EmptyFieldState("comm1")),
                    EditingState(EmptyFieldState("comm2")),
                    EditingState(EmptyFieldState("comm3"))
                )
            )

        // When
        viewModel.onDeleteComment(1)

        // Then
        viewModel.state.value.comments.shouldHaveSize(2)
        viewModel.state.value.comments.shouldContainInOrder(
            EditingState(EmptyFieldState("comm1")),
            EditingState(EmptyFieldState("comm3"))
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun editComment_when_onCommentClicked() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                comments = listOf(
                    EditingState(EmptyFieldState("comm1"), isEditing = true),
                    EditingState(EmptyFieldState("comm2")),
                    EditingState(EmptyFieldState("comm3"))
                )
            )

        // When
        viewModel.onCommentClicked(1)

        // Then
        viewModel.state.value.comments.shouldHaveSize(3)
        viewModel.state.value.comments.shouldContainInOrder(
            EditingState(EmptyFieldState("comm1"), isEditing = false),
            EditingState(EmptyFieldState("comm2"), isEditing = true),
            EditingState(EmptyFieldState("comm3"), isEditing = false)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun removeEmptyComment_when_onCommentClicked_given_emptyEditingComment() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                comments = listOf(
                    EditingState(isEditing = true),
                    EditingState(EmptyFieldState("comm1"))
                )
            )

        // When
        viewModel.onCommentClicked(1)

        // Then
        viewModel.state.value.comments.shouldHaveSize(1)
        viewModel.state.value.comments.shouldContain(
            EditingState(EmptyFieldState("comm1"), isEditing = true)
        )
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setComment_when_onCommentChanged() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                comments = listOf(EditingState())
            )
        val comment = "fake comment"

        // When
        viewModel.onCommentChanged(0, comment)

        // Then
        viewModel.state.value.comments.shouldHaveSize(1)
        viewModel.state.value.comments.first().fieldState shouldBe EmptyFieldState(comment)
        viewModel.state.value.comments.first().fieldState.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setTimeCookingAndValid_when_onTimeCookingChanged_given_timeCookingValid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val time = "123"

        // When
        viewModel.onTimeCookingChanged(time)

        // Then
        viewModel.state.value.time.cooking shouldBe ShortFieldState(time, 123)
        viewModel.state.value.time.cooking.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setTimeCookingAndInvalid_when_onTimeCookingChanged_given_timeCookingInvalid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val time = "123ytr"

        // When
        viewModel.onTimeCookingChanged(time)

        // Then
        viewModel.state.value.time.cooking shouldBe ShortFieldState(time, null)
        viewModel.state.value.time.cooking.isValid.shouldBeFalse()
        viewModel.state.value.isValid.shouldBeFalse()
    }

    @Test
    fun setTimePreparationAndValid_when_onTimePreparationChanged_given_timePreparationValid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val time = "123"

        // When
        viewModel.onTimePreparationChanged(time)

        // Then
        viewModel.state.value.time.preparation shouldBe ShortFieldState(time, 123)
        viewModel.state.value.time.preparation.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setTimePreparationAndInvalid_when_onTimePreparationChanged_given_timePreparationInvalid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val time = "123ytr"

        // When
        viewModel.onTimePreparationChanged(time)

        // Then
        viewModel.state.value.time.preparation shouldBe ShortFieldState(time, null)
        viewModel.state.value.time.preparation.isValid.shouldBeFalse()
        viewModel.state.value.isValid.shouldBeFalse()
    }

    @Test
    fun setTimeWaitingAndValid_when_onTimeWaitingChanged_given_timeWaitingValid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val time = "123"

        // When
        viewModel.onTimeWaitingChanged(time)

        // Then
        viewModel.state.value.time.waiting shouldBe ShortFieldState(time, 123)
        viewModel.state.value.time.waiting.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setTimeWaitingAndInvalid_when_onTimeWaitingChanged_given_timeWaitingInvalid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val time = "123ytr"

        // When
        viewModel.onTimeWaitingChanged(time)

        // Then
        viewModel.state.value.time.waiting shouldBe ShortFieldState(time, null)
        viewModel.state.value.time.waiting.isValid.shouldBeFalse()
        viewModel.state.value.isValid.shouldBeFalse()
    }

    @Test
    fun setTimeTotalAndValid_when_onTimeTotalChanged_given_timeTotalValid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val time = "123"

        // When
        viewModel.onTimeTotalChanged(time)

        // Then
        viewModel.state.value.time.total shouldBe ShortFieldState(time, 123)
        viewModel.state.value.time.total.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setTimeTotalAndInvalid_when_onTimeTotalChanged_given_timeTotalInvalid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val time = "123ytr"

        // When
        viewModel.onTimeTotalChanged(time)

        // Then
        viewModel.state.value.time.total shouldBe ShortFieldState(time, null)
        viewModel.state.value.time.total.isValid.shouldBeFalse()
        viewModel.state.value.isValid.shouldBeFalse()
    }

    @Test
    fun computeTimeTotal_when_computeTotal_given_timesFilled() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                time = TimeState(
                    cooking = ShortFieldState("1", 1),
                    preparation = ShortFieldState("2", 2),
                    waiting = ShortFieldState("3", 3)
                )
            )

        // When
        viewModel.computeTotal()

        // Then
        viewModel.state.value.time.total shouldBe ShortFieldState("6", 6)
        viewModel.state.value.time.total.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun computeTimeTotal_when_computeTotal_given_timesEmpty() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(
                title = NonEmptyFieldState("fake title"),
                time = TimeState(
                    cooking = ShortFieldState("", null),
                    preparation = ShortFieldState("", null),
                    waiting = ShortFieldState("", null)
                )
            )

        // When
        viewModel.computeTotal()

        // Then
        viewModel.state.value.time.total shouldBe ShortFieldState("0", 0)
        viewModel.state.value.time.total.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setTemperatureAndValid_when_onTemperatureChanged_given_temperatureValid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val temperature = "123"

        // When
        viewModel.onTemperatureChanged(temperature)

        // Then
        viewModel.state.value.temperature shouldBe ShortFieldState(temperature, 123)
        viewModel.state.value.temperature.isValid.shouldBeTrue()
        viewModel.state.value.isValid.shouldBeTrue()
    }

    @Test
    fun setTemperatureAndInvalid_when_onTemperatureChanged_given_temperatureInvalid() {
        // Given
        viewModel._state.value =
            viewModel.state.value.copy(title = NonEmptyFieldState("fake title"))
        val temperature = "123ytr"

        // When
        viewModel.onTemperatureChanged(temperature)

        // Then
        viewModel.state.value.temperature shouldBe ShortFieldState(temperature, null)
        viewModel.state.value.temperature.isValid.shouldBeFalse()
        viewModel.state.value.isValid.shouldBeFalse()
    }
}

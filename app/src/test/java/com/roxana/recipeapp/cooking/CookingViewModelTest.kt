package com.roxana.recipeapp.cooking

import androidx.lifecycle.SavedStateHandle
import com.roxana.recipeapp.CookingNode
import com.roxana.recipeapp.domain.comment.DeleteCommentUseCase
import com.roxana.recipeapp.domain.detail.GetRecipeByIdAsFlowUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.Comment
import com.roxana.recipeapp.domain.model.Ingredient
import com.roxana.recipeapp.domain.model.Instruction
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Recipe
import com.roxana.recipeapp.domain.model.Temperature
import com.roxana.recipeapp.helpers.MainCoroutineRule
import com.roxana.recipeapp.helpers.fakeEmptyRecipe
import com.roxana.recipeapp.uimodel.toUiModel
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CookingViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val getRecipeByIdUseCase: GetRecipeByIdAsFlowUseCase = mockk(relaxed = true)
    private val deleteCommentUseCase: DeleteCommentUseCase = mockk(relaxed = true)
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private lateinit var viewModel: CookingViewModel

    private val recipeId = 1234
    private val recipe = fakeEmptyRecipe.copy(id = recipeId)

    @Before
    fun setUp() {
        every { savedStateHandle.get<Int>(CookingNode.KEY_ID)!! } returns recipeId
        every { savedStateHandle.get<String>(CookingNode.KEY_PORTIONS_MULTIPLIER) } returns null
        every {
            savedStateHandle.get<Double>(CookingViewModel.KEY_SAVE_PORTIONS_COEF)
        } returns null
        every {
            savedStateHandle.get<List<Int>>(CookingViewModel.KEY_SAVE_CHECKED_INGREDIENTS)
        } returns null
        every {
            savedStateHandle.get<List<Short>>(CookingViewModel.KEY_SAVE_CHECKED_INSTRUCTIONS)
        } returns null
    }

    @Test
    fun setIsLoading_when_init_given_nothingCalledYet() = runTest {
        // When
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // Then
        viewModel.state.value.isLoading.shouldBeTrue()
        viewModel.state.value.isFetchingError.shouldBeFalse()
    }

    @Test
    fun setFetchingError_when_init_given_getRecipeError() = runTest {
        // Given
        coEvery { getRecipeByIdUseCase(any()) } returns flow {
            emit(Result.failure(IllegalAccessException()))
        }

        // When
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // Then
        viewModel.state.value.isFetchingError.shouldBeTrue()
    }

    @Test
    fun dismissError_when_onDismissError_given_getRecipeError() = runTest {
        // Given
        coEvery { getRecipeByIdUseCase(any()) } returns flow {
            emit(Result.failure(IllegalAccessException()))
        }

        // When - then
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)
        viewModel.state.value.isFetchingError.shouldBeTrue()
        viewModel.onDismissError()
        viewModel.state.value.isFetchingError.shouldBeFalse()
    }

    @Test
    fun initStateWithRecipe_when_init_given_noSavingsAndNoMultiplierAndNoPortions() = runTest {
        // Given
        val portions: Short? = null
        val ingredient = Ingredient(1, "ingredient", 6.0, null)
        val instruction = Instruction(2, "Instruction")
        val otherInstruction = Instruction(1, "Other instruction")
        val comment = Comment(2, "Comment")
        val otherComment = Comment(1, "Other comment")
        val localRecipe = recipe.copy(
            portions = portions,
            ingredients = listOf(ingredient),
            instructions = listOf(instruction, otherInstruction),
            comments = listOf(comment, otherComment)
        )
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }

        // When
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // Then
        viewModel.state.value.title shouldBe recipe.name
        viewModel.state.value.portions shouldBe portions
        viewModel.state.value.selectedPortions shouldBe 1.0
        viewModel.state.value.ingredients shouldBe listOf(
            IngredientState(
                ingredient.id,
                ingredient.name,
                ingredient.quantity,
                ingredient.quantity,
                ingredient.quantityType.toUiModel(),
                false
            )
        )
        viewModel.state.value.instructions shouldBe listOf(
            InstructionState(otherInstruction.ordinal, otherInstruction.name, false, true),
            InstructionState(instruction.ordinal, instruction.name, false, false)
        )
        viewModel.state.value.commentState shouldBe CommentState(
            listOf(
                Comment(otherComment.name, otherComment.ordinal.toInt()),
                Comment(comment.name, comment.ordinal.toInt())
            ),
            false
        )
        viewModel.state.value.temperature shouldBe recipe.temperature
        viewModel.state.value.temperatureUnit shouldBe recipe.temperatureUnit?.toUiModel()
        viewModel.state.value.isLoading.shouldBeFalse()
        viewModel.state.value.isFetchingError.shouldBeFalse()
    }

    @Test
    fun initStateWithPortions_when_init_given_portionsAndNoSavingsNoMultiplier() = runTest {
        // Given
        val portions: Short = 3
        val localRecipe = recipe.copy(
            portions = portions,
        )
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }

        // When
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // Then
        viewModel.state.value.portions shouldBe portions
        viewModel.state.value.selectedPortions shouldBe portions
    }

    @Test
    fun initIngredientsWithDestinationMultiplier_when_init_given_noSavedMultiplier() = runTest {
        // Given
        val portions: Short = 3
        val multiplier = 2
        val ingredient = Ingredient(1, "ingredient", 6.0, null)
        val localRecipe = recipe.copy(
            portions = portions,
            ingredients = listOf(ingredient),
        )
        every {
            savedStateHandle.get<String>(CookingNode.KEY_PORTIONS_MULTIPLIER)
        } returns multiplier.toString()
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }

        // When
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // Then
        viewModel.state.value.title shouldBe localRecipe.name
        viewModel.state.value.portions shouldBe portions
        viewModel.state.value.selectedPortions shouldBe portions * multiplier
        viewModel.state.value.ingredients shouldBe listOf(
            IngredientState(
                ingredient.id,
                ingredient.name,
                ingredient.quantity,
                ingredient.quantity!! * multiplier,
                ingredient.quantityType.toUiModel(),
                false
            )
        )
    }

    @Test
    fun initIngredientsWithSavedMultiplier_when_init_given_savedMultiplier() = runTest {
        // Given
        val portions: Short = 3
        val destMultiplier = 2
        val savedMultiplier = 3.0
        val ingredient = Ingredient(1, "ingredient", 6.0, null)
        val localRecipe = recipe.copy(
            portions = portions,
            ingredients = listOf(ingredient),
        )
        every {
            savedStateHandle.get<String>(CookingNode.KEY_PORTIONS_MULTIPLIER)
        } returns destMultiplier.toString()
        every {
            savedStateHandle.get<Double>(CookingViewModel.KEY_SAVE_PORTIONS_COEF)
        } returns savedMultiplier
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }

        // When
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // Then
        viewModel.state.value.title shouldBe localRecipe.name
        viewModel.state.value.portions shouldBe portions
        viewModel.state.value.selectedPortions shouldBe portions * savedMultiplier
        viewModel.state.value.ingredients shouldBe listOf(
            IngredientState(
                ingredient.id,
                ingredient.name,
                ingredient.quantity,
                ingredient.quantity!! * savedMultiplier,
                ingredient.quantityType.toUiModel(),
                false
            )
        )
    }

    @Test
    fun checkIngredients_when_init_given_savedIngredients() = runTest {
        // Given
        val portions: Short? = null
        val ingredient1 = Ingredient(1, "ingredient", 6.0, null)
        val ingredient2 = Ingredient(2, "ingredient2", 5.0, null)
        val localRecipe = recipe.copy(
            portions = portions,
            ingredients = listOf(ingredient1, ingredient2),
        )
        every {
            savedStateHandle.get<List<Int>>(CookingViewModel.KEY_SAVE_CHECKED_INGREDIENTS)
        } returns listOf(ingredient1.id)
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }

        // When
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // Then
        viewModel.state.value.ingredients shouldBe listOf(
            IngredientState(
                ingredient1.id,
                ingredient1.name,
                ingredient1.quantity,
                ingredient1.quantity,
                ingredient1.quantityType.toUiModel(),
                true
            ),
            IngredientState(
                ingredient2.id,
                ingredient2.name,
                ingredient2.quantity,
                ingredient2.quantity,
                ingredient2.quantityType.toUiModel(),
                false
            )
        )
    }

    @Test
    fun setCheckedInstructions_when_init_given_savedInstructions() = runTest {
        // Given
        val instruction = Instruction(2, "Instruction")
        val otherInstruction = Instruction(1, "Other instruction")
        val localRecipe = recipe.copy(
            instructions = listOf(instruction, otherInstruction),
        )
        every {
            savedStateHandle.get<List<Short>>(CookingViewModel.KEY_SAVE_CHECKED_INSTRUCTIONS)
        } returns listOf(otherInstruction.ordinal)
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }

        // When
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // Then
        viewModel.state.value.instructions shouldBe listOf(
            InstructionState(otherInstruction.ordinal, otherInstruction.name, true, false),
            InstructionState(instruction.ordinal, instruction.name, false, true)
        )
    }

    @Test
    fun incrementPortions_when_incrementPortions() = runTest {
        // Given
        val portions: Short = 3
        val ingredient = Ingredient(1, "ingredient", 6.0, null)
        val localRecipe = recipe.copy(portions = portions, ingredients = listOf(ingredient))
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)
        val multiplier: Double = (portions + 1.0) / portions

        // When
        viewModel.onIncrementPortions()

        // Then
        viewModel.state.value.portions shouldBe portions
        viewModel.state.value.selectedPortions shouldBe portions + 1
        viewModel.state.value.ingredients shouldBe listOf(
            IngredientState(
                ingredient.id,
                ingredient.name,
                ingredient.quantity,
                ingredient.quantity!! * multiplier,
                ingredient.quantityType.toUiModel(),
                false
            )
        )
        verify { savedStateHandle[CookingViewModel.KEY_SAVE_PORTIONS_COEF] = multiplier }
    }

    @Test
    fun decrementPortions_when_decrementPortions() = runTest {
        // Given
        val portions: Short = 3
        val ingredient = Ingredient(1, "ingredient", 6.0, null)
        val localRecipe = recipe.copy(portions = portions, ingredients = listOf(ingredient))
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)
        val multiplier: Double = (portions - 1.0) / portions

        // When
        viewModel.onDecrementPortions()

        // Then
        viewModel.state.value.portions shouldBe portions
        viewModel.state.value.selectedPortions shouldBe portions - 1
        viewModel.state.value.ingredients shouldBe listOf(
            IngredientState(
                ingredient.id,
                ingredient.name,
                ingredient.quantity,
                ingredient.quantity!! * multiplier,
                ingredient.quantityType.toUiModel(),
                false
            )
        )
        verify { savedStateHandle[CookingViewModel.KEY_SAVE_PORTIONS_COEF] = multiplier }
    }

    @Test
    fun doNothing_when_decrementPortionsToZero() = runTest {
        // Given
        val portions: Short = 1
        val ingredient = Ingredient(1, "ingredient", 6.0, null)
        val localRecipe = recipe.copy(portions = portions, ingredients = listOf(ingredient))
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // When
        viewModel.onDecrementPortions()

        // Then
        viewModel.state.value.portions shouldBe portions
        viewModel.state.value.selectedPortions shouldBe portions
        viewModel.state.value.ingredients shouldBe listOf(
            IngredientState(
                ingredient.id,
                ingredient.name,
                ingredient.quantity,
                ingredient.quantity!!,
                ingredient.quantityType.toUiModel(),
                false
            )
        )
    }

    @Test
    fun resetPortions_when_resetPortions() = runTest {
        // Given
        val portions: Short = 3
        val ingredient = Ingredient(1, "ingredient", 6.0, null)
        val localRecipe = recipe.copy(portions = portions, ingredients = listOf(ingredient))
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)
        viewModel.onDecrementPortions()

        // When
        viewModel.onResetPortions()

        // Then
        viewModel.state.value.portions shouldBe portions
        viewModel.state.value.selectedPortions shouldBe portions
        viewModel.state.value.ingredients shouldBe listOf(
            IngredientState(
                ingredient.id,
                ingredient.name,
                ingredient.quantity,
                ingredient.quantity!!,
                ingredient.quantityType.toUiModel(),
                false
            )
        )
        verify { savedStateHandle.remove<Double>(CookingViewModel.KEY_SAVE_PORTIONS_COEF) }
    }

    @Test
    fun toggleIngredient_when_toggleIngredientCheck() = runTest {
        // Given
        val portions: Short = 3
        val ingredient = Ingredient(1, "ingredient", 6.0, null)
        val localRecipe = recipe.copy(portions = portions, ingredients = listOf(ingredient))
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // When - then
        viewModel.toggleIngredientCheck(ingredient.id, true)
        viewModel.state.value.ingredients.single().isChecked.shouldBeTrue()
        verify {
            savedStateHandle[CookingViewModel.KEY_SAVE_CHECKED_INGREDIENTS] = listOf(ingredient.id)
        }

        viewModel.toggleIngredientCheck(ingredient.id, false)
        viewModel.state.value.ingredients.single().isChecked.shouldBeFalse()
        verify { savedStateHandle[CookingViewModel.KEY_SAVE_CHECKED_INGREDIENTS] = listOf<Int>() }
    }

    @Test
    fun toggleInstruction_when_toggleInstructionCheck() = runTest {
        // Given
        val instruction = Instruction(2, "Instruction")
        val otherInstruction = Instruction(1, "Other instruction")
        val localRecipe = recipe.copy(
            instructions = listOf(instruction, otherInstruction),
        )
        coEvery { getRecipeByIdUseCase(any()) } returns flow { emit(Result.success(localRecipe)) }
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // When - then
        viewModel.toggleInstructionCheck(otherInstruction.ordinal, true)
        viewModel.state.value.instructions shouldBe listOf(
            InstructionState(otherInstruction.ordinal, otherInstruction.name, true, false),
            InstructionState(instruction.ordinal, instruction.name, false, true)
        )
        verify {
            savedStateHandle[CookingViewModel.KEY_SAVE_CHECKED_INSTRUCTIONS] =
                listOf(otherInstruction.ordinal)
        }

        viewModel.toggleInstructionCheck(otherInstruction.ordinal, false)
        viewModel.state.value.instructions shouldBe listOf(
            InstructionState(otherInstruction.ordinal, otherInstruction.name, false, true),
            InstructionState(instruction.ordinal, instruction.name, false, false)
        )
        verify {
            savedStateHandle[CookingViewModel.KEY_SAVE_CHECKED_INSTRUCTIONS] = listOf<Short>()
        }

        viewModel.toggleInstructionCheck(instruction.ordinal, true)
        viewModel.state.value.instructions shouldBe listOf(
            InstructionState(otherInstruction.ordinal, otherInstruction.name, false, true),
            InstructionState(instruction.ordinal, instruction.name, true, false)
        )
        verify {
            savedStateHandle[CookingViewModel.KEY_SAVE_CHECKED_INSTRUCTIONS] =
                listOf(instruction.ordinal)
        }
    }

    @Test
    fun changeEditingComment_when_onEditCommentsAndOnDoneEditComments() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(CookingNode.KEY_ID)!! } returns recipeId
        val recipeModel = Recipe(
            id = recipeId,
            name = "fake name",
            photoPath = null,
            portions = 2,
            categories = listOf(CategoryType.DINNER, CategoryType.DESSERT),
            ingredients = listOf(Ingredient(1, "ingr", 3.0, QuantityType.CUP)),
            instructions = listOf(Instruction(1, "instr1"), Instruction(2, "instr2")),
            comments = listOf(Comment(1, "comm1"), Comment(2, "comm2")),
            timeTotal = 7,
            timeCooking = 4,
            timeWaiting = 2,
            timePreparation = 1,
            temperature = 150,
            temperatureUnit = Temperature.CELSIUS
        )
        every { getRecipeByIdUseCase(recipeId) } returns flow { emit(Result.success(recipeModel)) }
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // When - then
        viewModel.onEditComments()
        viewModel.state.value.commentState.isEditing.shouldBeTrue()
        viewModel.onDoneEditComments()
        viewModel.state.value.commentState.isEditing.shouldBeFalse()
    }

    @Test
    fun deleteComment_when_OnDeleteComment() {
        // Given
        val commentToDeleteId = 123
        val recipeId = 1
        every { savedStateHandle.get<Int>(CookingNode.KEY_ID)!! } returns recipeId
        val recipeModel = fakeEmptyRecipe.copy(id = recipeId)
        every { getRecipeByIdUseCase(recipeId) } returns flow { emit(Result.success(recipeModel)) }
        viewModel = CookingViewModel(savedStateHandle, getRecipeByIdUseCase, deleteCommentUseCase)

        // When
        viewModel.onDeleteComment(commentToDeleteId)

        // Then
        coVerify { deleteCommentUseCase(DeleteCommentUseCase.Input(recipeId, commentToDeleteId)) }
    }
}

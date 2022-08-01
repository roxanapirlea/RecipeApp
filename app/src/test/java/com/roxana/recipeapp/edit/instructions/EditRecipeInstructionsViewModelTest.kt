package com.roxana.recipeapp.edit.instructions

import com.roxana.recipeapp.domain.editrecipe.GetInstructionsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetInstructionsUseCase
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.helpers.MainCoroutineRule
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldNotContain
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
class EditRecipeInstructionsViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val isExistingUC: IsRecipeExistingUseCase = mockk(relaxed = true)
    private val getInstructionsUC: GetInstructionsUseCase = mockk(relaxed = true)
    private val setInstructionsUC: SetInstructionsUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRecipeInstructionsViewModel

    private val instructions =
        listOf(CreationInstruction("Instruction 1", 0), CreationInstruction("Instruction 2", 1))

    @Before
    fun setUp() {
        coEvery { isExistingUC(null) } returns Result.success(true)
        every { getInstructionsUC(null) } returns flow { emit(Result.success(instructions)) }
    }

    @Test
    fun setExistingRecipe_when_init_given_existingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(true)

        // When
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeTrue()
    }

    @Test
    fun setNewRecipe_when_init_given_noExistingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)

        // When
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)

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
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setInstructions_when_init() {
        // Given
        coEvery { getInstructionsUC(null) } returns flow { emit(Result.success(instructions)) }

        // When
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)

        // Then
        viewModel.state.value.instructions shouldContainInOrder listOf(
            "Instruction 1",
            "Instruction 2"
        )
    }

    @Test
    fun setNoInstructions_when_init_given_error() {
        // Given
        coEvery {
            getInstructionsUC(null)
        } returns flow { emit(Result.failure(IllegalStateException())) }

        // When
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)

        // Then
        viewModel.state.value.instructions.shouldBeEmpty()
    }

    @Test
    fun changeInstruction_when_onInstructionChanged() {
        // Given
        val instruction = "New instruction"
        val emptyInstruction = "  "
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)

        // When - then
        viewModel.onInstructionChanged(instruction)
        viewModel.state.value.editingInstruction shouldBe instruction
        viewModel.state.value.canAddInstruction.shouldBeTrue()

        viewModel.onInstructionChanged(emptyInstruction)
        viewModel.state.value.editingInstruction shouldBe emptyInstruction
        viewModel.state.value.canAddInstruction.shouldBeFalse()
    }

    @Test
    fun saveInstruction_when_onSaveInstruction_given_validInstruction() {
        // Given
        val instruction = "New instruction"
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)

        // When - then
        viewModel.onInstructionChanged(instruction)
        viewModel.state.value.canAddInstruction.shouldBeTrue()
        viewModel.onSaveInstruction()
        viewModel.state.value.editingInstruction shouldBe ""
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.state.value.instructions shouldContainInOrder listOf(
            "Instruction 1",
            "Instruction 2",
            instruction
        )
    }

    @Test
    fun doNothing_when_onSaveInstruction_given_invalidInstruction() {
        // Given
        val instruction = " "
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)

        // When - then
        viewModel.onInstructionChanged(instruction)
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.onSaveInstruction()
        viewModel.state.value.editingInstruction shouldBe instruction
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.state.value.instructions shouldNotContain instruction
    }

    @Test
    fun saveInstruction_when_onInstructionDone_given_validInstruction() {
        // Given
        val instruction = "New instruction"
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)

        // When - then
        viewModel.onInstructionChanged(instruction)
        viewModel.state.value.canAddInstruction.shouldBeTrue()
        viewModel.onInstructionDone()
        viewModel.state.value.editingInstruction shouldBe ""
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.state.value.instructions shouldContainInOrder listOf(
            "Instruction 1",
            "Instruction 2",
            instruction
        )
    }

    @Test
    fun goForwardAndSaveState_when_onInstructionDone_given_invalidInstructionAndNoError() {
        // Given
        val instruction = " "
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.success(Unit)

        // When - then
        viewModel.onInstructionChanged(instruction)
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.onInstructionDone()
        viewModel.state.value.editingInstruction shouldBe " "
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.state.value.instructions shouldContainInOrder listOf(
            "Instruction 1",
            "Instruction 2"
        )
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setInstructionsUC(instructions.sortedBy { it.ordinal }) }
    }

    @Test
    fun goForward_when_onInstructionDone_given_invalidInstructionAndError() {
        // Given
        val instruction = " "
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.failure(IllegalStateException())

        // When - then
        viewModel.onInstructionChanged(instruction)
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.onInstructionDone()
        viewModel.state.value.editingInstruction shouldBe " "
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.state.value.instructions shouldContainInOrder listOf(
            "Instruction 1",
            "Instruction 2"
        )
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
    }

    @Test
    fun goForwardAndSaveState_when_onInstructionDone_given_invalidInstructionAndNoSavedRecipe() {
        // Given
        val instruction = " "
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.success(Unit)

        // When - then
        viewModel.onInstructionChanged(instruction)
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.onInstructionDone()
        viewModel.state.value.editingInstruction shouldBe " "
        viewModel.state.value.canAddInstruction.shouldBeFalse()
        viewModel.state.value.instructions shouldContainInOrder listOf(
            "Instruction 1",
            "Instruction 2"
        )
        viewModel.state.value.navigation shouldBe Navigation.ForwardCreation
        coVerify { setInstructionsUC(instructions.sortedBy { it.ordinal }) }
    }

    @Test
    fun goForwardAndSaveState_when_onValidate_given_noError() {
        // Given
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setInstructionsUC(instructions.sortedBy { it.ordinal }) }
    }

    @Test
    fun goForward_when_onValidate_given_error() {
        // Given
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
    }

    @Test
    fun resetNavigation_when_onNavigationDone() {
        // Given
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.failure(IllegalStateException())

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
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setInstructionsUC(instructions) }
    }

    @Test
    fun goBack_when_onBack_given_error() {
        // Given
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noError() {
        // Given
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setInstructionsUC(instructions.sortedBy { it.ordinal }) }
    }

    @Test
    fun goToPage_when_onSelectPage_given_error() {
        // Given
        viewModel =
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.failure(IllegalStateException())

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
            EditRecipeInstructionsViewModel(isExistingUC, getInstructionsUC, setInstructionsUC)
        coEvery { setInstructionsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, false)
        coVerify { setInstructionsUC(instructions.sortedBy { it.ordinal }) }
    }
}

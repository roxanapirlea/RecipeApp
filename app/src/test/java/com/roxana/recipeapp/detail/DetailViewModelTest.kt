package com.roxana.recipeapp.detail

import androidx.lifecycle.SavedStateHandle
import com.roxana.recipeapp.RecipeDetailNode.KEY_ID
import com.roxana.recipeapp.domain.comment.DeleteCommentUseCase
import com.roxana.recipeapp.domain.detail.DeleteRecipeUseCase
import com.roxana.recipeapp.domain.detail.GetRecipeByIdAsFlowUseCase
import com.roxana.recipeapp.domain.detail.StartRecipeEditingUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.Comment
import com.roxana.recipeapp.domain.model.Ingredient
import com.roxana.recipeapp.domain.model.Instruction
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Recipe
import com.roxana.recipeapp.domain.model.Temperature
import com.roxana.recipeapp.helpers.MainCoroutineRule
import com.roxana.recipeapp.helpers.fakeEmptyRecipe
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getRecipeByIdUseCase: GetRecipeByIdAsFlowUseCase = mockk(relaxed = true)
    private val startRecipeEditingUseCase: StartRecipeEditingUseCase = mockk(relaxed = true)
    private val deleteRecipeUseCase: DeleteRecipeUseCase = mockk(relaxed = true)
    private val deleteCommentUseCase: DeleteCommentUseCase = mockk(relaxed = true)
    private lateinit var viewModel: DetailViewModel

    @Test
    fun setContentState_when_init() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
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

        // When
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // Then
        val content = viewModel.state.value
        content.title shouldBe "fake name"
        content.portions shouldBe 2
        content.categories.shouldContainAll(UiCategoryType.Dinner, UiCategoryType.Dessert)
        content.ingredients shouldBe listOf(
            IngredientState("ingr", 3.0, UiQuantityType.Cup)
        )
        content.time shouldBe TimeState(7, 4, 2, 1)
        content.temperature shouldBe 150
        content.temperatureUnit shouldBe UiTemperature.Celsius
    }

    @Test
    fun orderInstructionsByOrdinal_when_init() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
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

        // When
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // Then
        val content = viewModel.state.value
        content.instructions.shouldContainInOrder("instr1", "instr2")
    }

    @Test
    fun orderCommentsByOrdinal_when_init() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
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

        // When
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // Then
        val content = viewModel.state.value
        content.commentState.isEditing.shouldBeFalse()
        content.commentState.comments
            .shouldContainInOrder(Comment("comm1", 1), Comment("comm2", 2))
    }

    @Test
    fun setError_when_init_given_recipeError() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        every {
            getRecipeByIdUseCase(recipeId)
        } returns flow { emit(Result.failure(IllegalAccessException())) }

        // When
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // Then
        viewModel.state.value.isFetchingError.shouldBeTrue()
    }

    @Test
    fun dismissError_when_onErrorDismissed_given_recipeError() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        every {
            getRecipeByIdUseCase(recipeId)
        } returns flow { emit(Result.failure(IllegalAccessException())) }

        // When - then
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )
        viewModel.state.value.isFetchingError.shouldBeTrue()

        viewModel.onErrorDismissed()
        viewModel.state.value.isFetchingError.shouldBeFalse()
    }

    @Test
    fun setEditNav_when_onEdit_given_no_error() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        every {
            getRecipeByIdUseCase(recipeId)
        } returns flow { emit(Result.success(fakeEmptyRecipe)) }
        coEvery { startRecipeEditingUseCase(recipeId) } returns Result.success(Unit)
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // When
        viewModel.onEdit()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.EDIT
        coVerify { startRecipeEditingUseCase(recipeId) }
    }

    @Test
    fun doNoting_when_onEdit_givenStartEditError() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        every {
            getRecipeByIdUseCase(recipeId)
        } returns flow { emit(Result.success(fakeEmptyRecipe)) }
        coEvery {
            startRecipeEditingUseCase(recipeId)
        } returns Result.failure(IllegalAccessException())
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // When
        viewModel.onEdit()

        // Then
        viewModel.state.value.navigation.shouldBeNull()
    }

    @Test
    fun resetNav_when_onNavigationDone() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        every {
            getRecipeByIdUseCase(recipeId)
        } returns flow { emit(Result.success(fakeEmptyRecipe)) }
        coEvery { startRecipeEditingUseCase(recipeId) } returns Result.success(Unit)
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // When - then
        viewModel.onEdit()
        viewModel.state.value.navigation shouldBe Navigation.EDIT
        viewModel.onNavigationDone()
        viewModel.state.value.navigation.shouldBeNull()
    }

    @Test
    fun shouldShowDeleteMessage_when_onDelete() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        every {
            getRecipeByIdUseCase(recipeId)
        } returns flow { emit(Result.success(fakeEmptyRecipe)) }
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // When
        viewModel.onDelete()

        // Then
        viewModel.state.value.shouldShowDeleteMessage.shouldBeTrue()
    }

    @Test
    fun shouldStopShowingDeleteMessage_when_onUndoDelete() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        every {
            getRecipeByIdUseCase(recipeId)
        } returns flow { emit(Result.success(fakeEmptyRecipe)) }
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // When - then
        viewModel.onDelete()
        viewModel.state.value.shouldShowDeleteMessage.shouldBeTrue()
        viewModel.onUndoDelete()
        viewModel.state.value.shouldShowDeleteMessage.shouldBeFalse()
    }

    @Test
    fun shouldDeleteRecipe_when_onDeleteMessageDismissed() = runTest {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        every {
            getRecipeByIdUseCase(recipeId)
        } returns flow { emit(Result.success(fakeEmptyRecipe)) }
        coEvery { deleteRecipeUseCase(recipeId) } returns Result.success(Unit)
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )
        viewModel.onDelete()

        // When
        viewModel.onDeleteMessageDismissed()

        // Then
        viewModel.state.value.shouldShowDeleteMessage.shouldBeFalse()
        coVerify { deleteRecipeUseCase(recipeId) }
        viewModel.state.value.navigation shouldBe Navigation.BACK
    }

    @Test
    fun doNothing_when_onDeleteMessageDismissed_given_deleteError() = runTest {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        every {
            getRecipeByIdUseCase(recipeId)
        } returns flow { emit(Result.success(fakeEmptyRecipe)) }
        coEvery { deleteRecipeUseCase(recipeId) } returns Result.failure(IllegalAccessException())
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )
        viewModel.onDelete()

        // When
        viewModel.onDeleteMessageDismissed()

        // Then
        viewModel.state.value.shouldShowDeleteMessage.shouldBeFalse()
        viewModel.state.value.navigation.shouldBeNull()
    }

    @Test
    fun changeEditingComment_when_onEditCommentsAndOnDoneEditComments() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
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
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

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
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        val recipeModel = fakeEmptyRecipe.copy(id = recipeId)
        every { getRecipeByIdUseCase(recipeId) } returns flow { emit(Result.success(recipeModel)) }
        viewModel =
            DetailViewModel(
                savedStateHandle,
                getRecipeByIdUseCase,
                startRecipeEditingUseCase,
                deleteRecipeUseCase,
                deleteCommentUseCase
            )

        // When
        viewModel.onDeleteComment(commentToDeleteId)

        // Then
        coVerify { deleteCommentUseCase(DeleteCommentUseCase.Input(recipeId, commentToDeleteId)) }
    }
}

package com.roxana.recipeapp.detail

import androidx.lifecycle.SavedStateHandle
import com.roxana.recipeapp.Screen.RecipeDetail.KEY_ID
import com.roxana.recipeapp.domain.detail.GetRecipeByIdAsFlowUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.Comment
import com.roxana.recipeapp.domain.model.Ingredient
import com.roxana.recipeapp.domain.model.Instruction
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Recipe
import com.roxana.recipeapp.domain.model.Temperature
import com.roxana.recipeapp.helpers.MainCoroutineRule
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getRecipeByIdUseCase: GetRecipeByIdAsFlowUseCase = mockk(relaxed = true)
    private lateinit var viewModel: DetailViewModel

    @Test
    fun setContentState_when_init() {
        // Given
        val recipeId = 1
        every { savedStateHandle.get<Int>(KEY_ID)!! } returns recipeId
        val recipeModel = Recipe(
            id = recipeId,
            name = "fake name",
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
        viewModel = DetailViewModel(savedStateHandle, getRecipeByIdUseCase)

        // Then
        viewModel.state.value.shouldBeTypeOf<DetailViewState.Content>()
        val content = viewModel.state.value as DetailViewState.Content
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
        viewModel = DetailViewModel(savedStateHandle, getRecipeByIdUseCase)

        // Then
        viewModel.state.value.shouldBeTypeOf<DetailViewState.Content>()
        val content = viewModel.state.value as DetailViewState.Content
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
        viewModel = DetailViewModel(savedStateHandle, getRecipeByIdUseCase)

        // Then
        viewModel.state.value.shouldBeTypeOf<DetailViewState.Content>()
        val content = viewModel.state.value as DetailViewState.Content
        content.comments.shouldContainInOrder("comm1", "comm2")
    }
}

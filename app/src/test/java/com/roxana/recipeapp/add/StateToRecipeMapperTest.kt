package com.roxana.recipeapp.add

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContainAnyOf
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.Test

class StateToRecipeMapperTest {
    private val recipeStateModel = AddRecipeViewState(
        title = NonEmptyFieldState(text = ""),
        categories = listOf(),
        quantities = listOf(),
        portions = ShortFieldState("1u", 1),
        ingredients = listOf(),
        instructions = listOf(),
        comments = listOf(),
        time = TimeState(
            cooking = ShortFieldState("2", 2),
            preparation = ShortFieldState("3", 3),
            waiting = ShortFieldState("4", 4),
            total = ShortFieldState("5", 5),
        ),
        temperature = ShortFieldState("6", 6)
    )

    @Test
    fun return_ingredientWithType_when_ingredientStateToDomainModel_given_nonNullQuantity() {
        // Given
        val name = "fake ingredient"
        val quantity = "2.0"
        val quantityValue = 2.0
        val quantityType = UiQuantityType.Cup
        val ingredientState = IngredientState(
            EmptyFieldState(name),
            DoubleFieldState(quantity, quantityValue),
            quantityType
        )

        // When
        val ingredient = ingredientState.toDomainModel()

        // Then
        ingredient shouldBe CreationIngredient(name, quantityValue, QuantityType.CUP)
    }

    @Test
    fun return_ingredientWithNoType_when_ingredientStateToDomainModel_given_nullQuantity() {
        // Given
        val name = "fake ingredient"
        val quantity = ""
        val quantityValue = null
        val quantityType = UiQuantityType.Cup
        val ingredientState = IngredientState(
            EmptyFieldState(name),
            DoubleFieldState(quantity, quantityValue),
            quantityType
        )

        // When
        val ingredient = ingredientState.toDomainModel()

        // Then
        ingredient shouldBe CreationIngredient(name, quantityValue, null)
    }

    @Test
    fun setRecipeNonIterableFields_when_recipeStateToRecipe() {
        // Given
        val recipeState = recipeStateModel.copy(
            title = NonEmptyFieldState(text = "name"),
            portions = ShortFieldState("1u", 1),
            time = TimeState(
                cooking = ShortFieldState("2", 2),
                preparation = ShortFieldState("3", 3),
                waiting = ShortFieldState("4", 4),
                total = ShortFieldState("5", 5),
            ),
            temperature = ShortFieldState("6", 6)
        )

        // When
        val recipe = recipeState.toRecipe()

        // Then
        recipe.name shouldBe "name"
        recipe.photoPath.shouldBeNull()
        recipe.portions shouldBe 1
        recipe.timeCooking shouldBe 2
        recipe.timePreparation shouldBe 3
        recipe.timeWaiting shouldBe 4
        recipe.timeTotal shouldBe 5
        recipe.temperature shouldBe 6
    }

    @Test
    fun setRecipeCategories_when_recipeStateToRecipe_given_selectedCategories() {
        // Given
        val recipeState = recipeStateModel.copy(
            categories = listOf(
                CategoryState(UiCategoryType.Breakfast, true),
                CategoryState(UiCategoryType.Lunch, false),
                CategoryState(UiCategoryType.Dinner, false),
                CategoryState(UiCategoryType.Dessert, true),
            )
        )

        // When
        val recipe = recipeState.toRecipe()

        // Then
        recipe.categories shouldHaveSize 2
        recipe.categories.shouldContainAll(CategoryType.BREAKFAST, CategoryType.DESSERT)
        recipe.categories.shouldNotContainAnyOf(CategoryType.LUNCH, CategoryType.DINNER)
    }

    @Test
    fun setRecipeInstructions_when_recipeStateToRecipe_given_instructions() {
        // Given
        val recipeState = recipeStateModel.copy(
            instructions = listOf(
                EditingState(EmptyFieldState("Mix everything")),
                EditingState(EmptyFieldState("Cook in a pan")),
                EditingState(EmptyFieldState(""))
            )
        )

        // When
        val recipe = recipeState.toRecipe()

        // Then
        recipe.instructions shouldHaveSize 2
        recipe.instructions.shouldContainAll(
            CreationInstruction(ordinal = 0, name = "Mix everything"),
            CreationInstruction(ordinal = 1, name = "Cook in a pan")
        )
    }

    @Test
    fun setRecipeIngredient_when_recipeStateToRecipe_given_ingredients() {
        // Given
        val recipeState = recipeStateModel.copy(
            ingredients = listOf(
                IngredientState(
                    EmptyFieldState("Eggs"),
                    DoubleFieldState("1.0", 1.0),
                    UiQuantityType.None
                ),
                IngredientState(EmptyFieldState(""), DoubleFieldState(""), UiQuantityType.None),
                IngredientState(
                    EmptyFieldState("Flour"),
                    DoubleFieldState("3.0", 3.0),
                    UiQuantityType.Tablespoon
                )
            )
        )

        // When
        val recipe = recipeState.toRecipe()

        // Then
        recipe.ingredients shouldHaveSize 2
        recipe.ingredients.shouldContainAll(
            CreationIngredient("Eggs", 1.0, null),
            CreationIngredient("Flour", 3.0, QuantityType.TABLESPOON)
        )
    }

    @Test
    fun setRecipeComments_when_recipeStateToRecipe_given_comments() {
        // Given
        val recipeState = recipeStateModel.copy(
            comments = listOf(
                EditingState(EmptyFieldState("Put oil in the pan")),
                EditingState(EmptyFieldState("Excellent with chocolate")),
                EditingState(EmptyFieldState(""))
            )
        )

        // When
        val recipe = recipeState.toRecipe()

        // Then
        recipe.comments shouldHaveSize 2
        recipe.comments.shouldContainAll(
            CreationComment(ordinal = 0, detail = "Put oil in the pan"),
            CreationComment(ordinal = 1, detail = "Excellent with chocolate")
        )
    }
}

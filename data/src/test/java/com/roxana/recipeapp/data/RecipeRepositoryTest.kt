package com.roxana.recipeapp.data

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.RecipeSummary
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.roxana.recipeapp.data.Ingredient as DataIngredient
import com.roxana.recipeapp.data.Recipe as DataRecipe

internal class RecipeRepositoryTest {
    private val recipeQueries: RecipeQueries = mockk(relaxed = true)
    private val ingredientQueries: IngredientQueries = mockk(relaxed = true)
    private val ingredientForRecipeQueries: IngredientForRecipeQueries = mockk(relaxed = true)
    private val categoryForRecipeQueries: CategoryForRecipeQueries = mockk(relaxed = true)
    private val instructionQueries: InstructionQueries = mockk(relaxed = true)
    private val commentQueries: CommentQueries = mockk(relaxed = true)

    private lateinit var repository: RecipeRepositoryImpl

    @Before
    fun setUp() {
        repository = RecipeRepositoryImpl(
            recipeQueries,
            ingredientQueries,
            ingredientForRecipeQueries,
            categoryForRecipeQueries,
            instructionQueries,
            commentQueries
        )
        mockkStatic("com.roxana.recipeapp.data.DataMappersKt")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun callRecipeQueriesInsert_when_addRecipe() = runBlocking {
        // Given
        val name = "Crepes"
        val photoPath = null
        val portions: Short = 4
        val timeTotal: Short = 25
        val timeCooking: Short = 20
        val timePreparation: Short = 5
        val timeWaiting: Short = 0
        val temperature = null
        val recipe = creationRecipeModel.copy(
            name = name,
            photoPath = photoPath,
            portions = portions,
            timeTotal = timeTotal,
            timeCooking = timeCooking,
            timePreparation = timePreparation,
            timeWaiting = timeWaiting,
            temperature = temperature
        )
        every { recipeQueries.getByName(name).executeAsOne() } returns dataRecipeModel
        every {
            ingredientQueries.getByName(any()).executeAsOne()
        } returns DataIngredient(0, "")

        // When
        repository.addRecipe(recipe)

        // Then
        verify {
            recipeQueries.insert(
                name,
                photoPath,
                portions,
                timeTotal,
                timeCooking,
                timePreparation,
                timeWaiting,
                temperature
            )
        }
    }

    @Test
    fun callIngredientQueriesInsert_when_addRecipe() = runBlocking {
        // Given
        val name = "Crepes"
        val ingredient1 = CreationIngredient("Eggs", 2.0, null)
        val ingredient2 =
            CreationIngredient("Flour", 6.0, QuantityType.TABLESPOON)
        val recipe = creationRecipeModel.copy(
            name = name,
            ingredients = listOf(ingredient1, ingredient2)
        )
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeQueries.getByName(name).executeAsOne() } returns dataRecipe
        every {
            ingredientQueries.getByName(any()).executeAsOne()
        } returns DataIngredient(0, "")

        // When
        repository.addRecipe(recipe)

        // Then
        verify { recipeQueries.getByName(name).executeAsOne() }
        verify { ingredientQueries.insert(ingredient1.name) }
        verify { ingredientQueries.insert(ingredient2.name) }
    }

    @Test
    fun callIngredientForRecipeQueriesInsert_when_addRecipe() = runBlocking {
        // Given
        val ingredient1 = CreationIngredient("Eggs", 2.0, null)
        val ingredient2 =
            CreationIngredient("Flour", 6.0, QuantityType.TABLESPOON)
        val recipe = creationRecipeModel.copy(ingredients = listOf(ingredient1, ingredient2))
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeQueries.getByName(recipe.name).executeAsOne() } returns dataRecipe
        every {
            ingredientQueries.getByName(ingredient1.name).executeAsOne()
        } returns DataIngredient(0, "Eggs")
        every {
            ingredientQueries.getByName(ingredient2.name).executeAsOne()
        } returns DataIngredient(1, "Flour")
        every { ingredient1.quantityType.toDataModel() } returns null
        every { ingredient2.quantityType.toDataModel() } returns DbQuantityType.TABLESPOON

        // When
        repository.addRecipe(recipe)

        // Then
        verify { ingredientQueries.getByName(ingredient1.name).executeAsOne() }
        verify { ingredientQueries.getByName(ingredient2.name).executeAsOne() }
        verify {
            ingredientForRecipeQueries.insert(
                ingredient1.quantity,
                null,
                null,
                0,
                recipeId
            )
        }
        verify {
            ingredientForRecipeQueries.insert(
                ingredient2.quantity,
                DbQuantityType.TABLESPOON,
                null,
                1,
                recipeId
            )
        }
    }

    @Test
    fun callCategoryForRecipeQueriesInsert_when_addRecipe() = runBlocking {
        // Given
        val category1 = CategoryType.BREAKFAST
        val category2 = CategoryType.DESSERT
        val recipe = creationRecipeModel.copy(categories = listOf(category1, category2))
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeQueries.getByName(any()).executeAsOne() } returns dataRecipe
        every {
            ingredientQueries.getByName(any()).executeAsOne()
        } returns DataIngredient(0, "")
        every { category1.toDataModel() } returns DbCategoryType.BREAKFAST
        every { category2.toDataModel() } returns DbCategoryType.DESSERT

        // When
        repository.addRecipe(recipe)

        // Then
        verify {
            categoryForRecipeQueries.insert(DbCategoryType.BREAKFAST, null, recipeId)
        }
        verify {
            categoryForRecipeQueries.insert(DbCategoryType.DESSERT, null, recipeId)
        }
    }

    @Test
    fun callInstructionQueriesInsert_when_addRecipe() = runBlocking {
        // Given
        val instruction1 = CreationInstruction("Mix everything", 0)
        val instruction2 = CreationInstruction("Cook in a pan", 1)
        val recipe = creationRecipeModel.copy(instructions = listOf(instruction1, instruction2))
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeQueries.getByName(any()).executeAsOne() } returns dataRecipe
        every {
            ingredientQueries.getByName(any()).executeAsOne()
        } returns DataIngredient(0, "")

        // When
        repository.addRecipe(recipe)

        // Then
        verify {
            instructionQueries.insert(instruction1.name, instruction1.ordinal, recipeId)
        }
        verify {
            instructionQueries.insert(instruction2.name, instruction2.ordinal, recipeId)
        }
    }

    @Test
    fun callCommentQueriesInsert_when_addRecipe() = runBlocking {
        // Given
        val comment1 = CreationComment("Put oil in the pan", 0)
        val comment2 = CreationComment("Excellent with chocolate", 1)
        val recipe = creationRecipeModel.copy(comments = listOf(comment1, comment2))
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeQueries.getByName(any()).executeAsOne() } returns dataRecipe
        every {
            ingredientQueries.getByName(any()).executeAsOne()
        } returns DataIngredient(0, "")

        // When
        repository.addRecipe(recipe)

        // Then
        verify {
            commentQueries.insert(comment1.detail, comment1.ordinal, recipeId)
        }
        verify {
            commentQueries.insert(comment2.detail, comment2.ordinal, recipeId)
        }
    }

    @Test
    fun returnRecipeSummary_when_mapSummary() {
        // Given
        val dbSummaries = listOf(
            GetRecipesSummary(1, "recipe 1", DbCategoryType.LUNCH),
            GetRecipesSummary(1, "recipe 1", DbCategoryType.DINNER),
            GetRecipesSummary(1, "recipe 1", null),
            GetRecipesSummary(2, "recipe 2", null)
        )

        // When
        val summaries = repository.mapSummary(dbSummaries)

        // Then
        summaries shouldBe listOf(
            RecipeSummary(1, "recipe 1", listOf(CategoryType.LUNCH, CategoryType.DINNER)),
            RecipeSummary(2, "recipe 2", listOf())
        )
    }

    private val creationRecipeModel = CreationRecipe(
        name = "fake name",
        photoPath = null,
        portions = 1,
        categories = listOf(CategoryType.DINNER),
        instructions = listOf(CreationInstruction("fake instruction", 1)),
        ingredients = listOf(
            CreationIngredient("fake ingredient", 2.0, QuantityType.POUND)
        ),
        timeTotal = 8,
        timePreparation = 7,
        timeCooking = 3,
        timeWaiting = 2,
        temperature = 150,
        comments = listOf(CreationComment("fake comment", 1)),
    )

    private val dataRecipeModel = DataRecipe(
        id = 0,
        name = "",
        photo_path = null,
        portions = 1,
        time_total = 2,
        time_preparation = 3,
        time_cooking = 4,
        time_waiting = 5,
        temperature = 6,
    )
}

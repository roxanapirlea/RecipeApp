package com.roxana.recipeapp.data.recipe

import com.roxana.recipeapp.data.GetCategoryByRecipeId
import com.roxana.recipeapp.data.GetIngredientByRecipeId
import com.roxana.recipeapp.data.GetRecipesSummary
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.Comment
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.Ingredient
import com.roxana.recipeapp.domain.model.Instruction
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.RecipeSummary
import com.roxana.recipeapp.domain.model.Temperature
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.roxana.recipeapp.data.Comment as DataComment
import com.roxana.recipeapp.data.Ingredient as DataIngredient
import com.roxana.recipeapp.data.Instruction as DataInstruction
import com.roxana.recipeapp.data.Recipe as DataRecipe

@OptIn(ExperimentalCoroutinesApi::class)
internal class RecipeRepositoryTest {
    private val recipeDao: RecipeDao = mockk(relaxed = true)
    private val ingredientDao: IngredientDao = mockk(relaxed = true)
    private val ingredientForRecipeDao: IngredientForRecipeDao = mockk(relaxed = true)
    private val categoryForRecipeDao: CategoryForRecipeDao = mockk(relaxed = true)
    private val instructionDao: InstructionDao = mockk(relaxed = true)
    private val commentDao: CommentDao = mockk(relaxed = true)

    private lateinit var repository: RecipeRepositoryImpl

    @Before
    fun setUp() {
        repository = RecipeRepositoryImpl(
            recipeDao,
            ingredientDao,
            ingredientForRecipeDao,
            categoryForRecipeDao,
            instructionDao,
            commentDao
        )
    }

    @Test
    fun callRecipeQueriesInsert_when_addRecipe() = runTest {
        // Given
        val name = "Crepes"
        val photoPath = null
        val portions: Short = 4
        val timeTotal: Short = 25
        val timeCooking: Short = 20
        val timePreparation: Short = 5
        val timeWaiting: Short = 0
        val temperature = null
        val temperatureType = null
        val recipe = creationRecipeModel.copy(
            name = name,
            photoPath = photoPath,
            portions = portions,
            timeTotal = timeTotal,
            timeCooking = timeCooking,
            timePreparation = timePreparation,
            timeWaiting = timeWaiting,
            temperature = temperature,
            temperatureUnit = temperatureType
        )
        every { recipeDao.getByName(name) } returns dataRecipeModel
        every { ingredientDao.getByName(any()) } returns DataIngredient(0, "")

        // When
        repository.addRecipe(recipe)

        // Then
        verify {
            recipeDao.insert(
                name,
                photoPath,
                portions,
                timeTotal,
                timeCooking,
                timePreparation,
                timeWaiting,
                temperature,
                temperatureType
            )
        }
    }

    @Test
    fun callIngredientQueriesInsert_when_addRecipe() = runTest {
        // Given
        val name = "Crepes"
        val ingredient1 = CreationIngredient(null, "Eggs", 2.0, null)
        val ingredient2 =
            CreationIngredient(null, "Flour", 6.0, QuantityType.TABLESPOON)
        val recipe = creationRecipeModel.copy(
            name = name,
            ingredients = listOf(ingredient1, ingredient2)
        )
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeDao.getByName(name) } returns dataRecipe
        every { ingredientDao.getByName(any()) } returns DataIngredient(0, "")

        // When
        repository.addRecipe(recipe)

        // Then
        verify { recipeDao.getByName(name) }
        verify { ingredientDao.insert(ingredient1.name) }
        verify { ingredientDao.insert(ingredient2.name) }
    }

    @Test
    fun callIngredientForRecipeQueriesInsert_when_addRecipe() = runTest {
        // Given
        val ingredient1 = CreationIngredient(null, "Eggs", 2.0, null)
        val ingredient2 =
            CreationIngredient(null, "Flour", 6.0, QuantityType.TABLESPOON)
        val recipe = creationRecipeModel.copy(ingredients = listOf(ingredient1, ingredient2))
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeDao.getByName(recipe.name) } returns dataRecipe
        every { ingredientDao.getByName(ingredient1.name) } returns DataIngredient(0, "Eggs")
        every { ingredientDao.getByName(ingredient2.name) } returns DataIngredient(1, "Flour")

        // When
        repository.addRecipe(recipe)

        // Then
        verify { ingredientDao.getByName(ingredient1.name) }
        verify { ingredientDao.getByName(ingredient2.name) }
        verify {
            ingredientForRecipeDao.insert(
                ingredient1.quantity,
                null,
                0,
                recipeId
            )
        }
        verify {
            ingredientForRecipeDao.insert(
                ingredient2.quantity,
                DbQuantityType.TABLESPOON,
                1,
                recipeId
            )
        }
    }

    @Test
    fun callCategoryForRecipeQueriesInsert_when_addRecipe() = runTest {
        // Given
        val category1 = CategoryType.BREAKFAST
        val category2 = CategoryType.DESSERT
        val recipe = creationRecipeModel.copy(categories = listOf(category1, category2))
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeDao.getByName(any()) } returns dataRecipe
        every { ingredientDao.getByName(any()) } returns DataIngredient(0, "")

        // When
        repository.addRecipe(recipe)

        // Then
        verify { categoryForRecipeDao.insert(DbCategoryType.BREAKFAST, recipeId) }
        verify { categoryForRecipeDao.insert(DbCategoryType.DESSERT, recipeId) }
    }

    @Test
    fun callInstructionQueriesInsert_when_addRecipe() = runTest {
        // Given
        val instruction1 = CreationInstruction("Mix everything", 0)
        val instruction2 = CreationInstruction("Cook in a pan", 1)
        val recipe = creationRecipeModel.copy(instructions = listOf(instruction1, instruction2))
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeDao.getByName(any()) } returns dataRecipe
        every { ingredientDao.getByName(any()) } returns DataIngredient(0, "")

        // When
        repository.addRecipe(recipe)

        // Then
        verify { instructionDao.insert(instruction1.name, instruction1.ordinal, recipeId) }
        verify { instructionDao.insert(instruction2.name, instruction2.ordinal, recipeId) }
    }

    @Test
    fun callCommentQueriesInsert_when_addRecipe() = runTest {
        // Given
        val comment1 = CreationComment("Put oil in the pan", 0)
        val comment2 = CreationComment("Excellent with chocolate", 1)
        val recipe = creationRecipeModel.copy(comments = listOf(comment1, comment2))
        val recipeId = 12L
        val dataRecipe = dataRecipeModel.copy(id = recipeId)
        every { recipeDao.getByName(any()) } returns dataRecipe
        every { ingredientDao.getByName(any()) } returns DataIngredient(0, "")

        // When
        repository.addRecipe(recipe)

        // Then
        verify { commentDao.insert(comment1.detail, comment1.ordinal, recipeId) }
        verify { commentDao.insert(comment2.detail, comment2.ordinal, recipeId) }
    }

    @Test
    fun callRecipeQueriesUpdate_when_updateRecipe() = runTest {
        // Given
        val name = "Crepes"
        val photoPath = "path"
        val portions: Short = 4
        val timeTotal: Short = 25
        val timeCooking: Short = 20
        val timePreparation: Short = 5
        val timeWaiting: Short = 0
        val temperature: Short = 6
        val temperatureType = Temperature.CELSIUS
        val id = 123456
        val recipe = creationRecipeModel.copy(
            name = name,
            photoPath = photoPath,
            portions = portions,
            timeTotal = timeTotal,
            timeCooking = timeCooking,
            timePreparation = timePreparation,
            timeWaiting = timeWaiting,
            temperature = temperature,
            temperatureUnit = temperatureType,
            id = id
        )

        // When
        repository.updateRecipe(recipe)

        // Then
        verify {
            recipeDao.update(
                name,
                photoPath,
                portions,
                timeTotal,
                timeCooking,
                timePreparation,
                timeWaiting,
                temperature,
                DbTemperatureType.CELSIUS,
                id.toLong()
            )
        }
    }

    @Test
    fun reinsertIngredients_when_updateRecipe() = runTest {
        // Given
        val recipeId = 1234
        val creationIngredient1 = CreationIngredient(null, "Eggs", 2.0, null)
        val creationIngredient2 =
            CreationIngredient(null, "Flour", 6.0, QuantityType.TABLESPOON)
        val recipe =
            creationRecipeModel.copy(
                id = recipeId,
                ingredients = listOf(creationIngredient1, creationIngredient2)
            )
        val ingredient1 = DataIngredient(1, creationIngredient1.name)
        val ingredient2 = DataIngredient(2, creationIngredient2.name)
        every { ingredientDao.getByName(creationIngredient1.name) } returns ingredient1
        every { ingredientDao.getByName(creationIngredient2.name) } returns ingredient2

        // When
        repository.updateRecipe(recipe)

        // Then
        verify { ingredientDao.insert(creationIngredient1.name) }
        verify { ingredientDao.insert(creationIngredient2.name) }
        verify { ingredientForRecipeDao.deleteByRecipeId(recipeId.toLong()) }
        verify {
            ingredientForRecipeDao.insert(
                creationIngredient1.quantity,
                null,
                ingredient1.id,
                recipeId.toLong()
            )
        }
        verify {
            ingredientForRecipeDao.insert(
                creationIngredient2.quantity,
                DbQuantityType.TABLESPOON,
                ingredient2.id,
                recipeId.toLong()
            )
        }
    }

    @Test
    fun deleteOldAndInsertNewCategories_when_updateRecipe() = runTest {
        // Given
        val recipeId = 1234
        val oldCategory = CategoryType.DESSERT
        val existingCategory = CategoryType.SNACK
        val newCategory = CategoryType.BREAKFAST
        val recipe =
            creationRecipeModel.copy(
                id = recipeId,
                categories = listOf(newCategory, existingCategory)
            )
        every { categoryForRecipeDao.getByRecipeId(recipeId.toLong()) } returns listOf(
            GetCategoryByRecipeId(1, oldCategory.toDataModel(), null),
            GetCategoryByRecipeId(2, existingCategory.toDataModel(), null)
        )

        // When
        repository.updateRecipe(recipe)

        // Then
        verify { categoryForRecipeDao.delete(1) }
        verify(exactly = 0) { categoryForRecipeDao.delete(2) }
        verify { categoryForRecipeDao.insert(newCategory.toDataModel(), recipeId.toLong()) }
        verify(exactly = 0) {
            categoryForRecipeDao.insert(
                existingCategory.toDataModel(),
                recipeId.toLong()
            )
        }
        verify(exactly = 0) {
            categoryForRecipeDao.insert(oldCategory.toDataModel(), recipeId.toLong())
        }
    }

    @Test
    fun deleteOldAndInsertNewInstructions_when_updateRecipe() = runTest {
        // Given
        val recipeId = 1234
        val instruction1 = CreationInstruction("Mix everything", 0)
        val instruction2 = CreationInstruction("Cook in a pan", 1)
        val recipe =
            creationRecipeModel.copy(
                id = recipeId,
                instructions = listOf(instruction1, instruction2)
            )

        // When
        repository.updateRecipe(recipe)

        // Then
        verify { instructionDao.deleteByRecipeId(recipeId.toLong()) }
        verify { instructionDao.insert(instruction1.name, instruction1.ordinal, recipeId.toLong()) }
        verify { instructionDao.insert(instruction2.name, instruction2.ordinal, recipeId.toLong()) }
    }

    @Test
    fun deleteOldAndInsertNewComments_when_updateRecipe() = runTest {
        // Given
        val recipeId = 1234
        val comment1 = CreationComment("Mix everything", 0)
        val comment2 = CreationComment("Cook in a pan", 1)
        val recipe =
            creationRecipeModel.copy(
                id = recipeId,
                comments = listOf(comment1, comment2)
            )

        // When
        repository.updateRecipe(recipe)

        // Then
        verify { commentDao.deleteByRecipeId(recipeId.toLong()) }
        verify { commentDao.insert(comment1.detail, comment1.ordinal, recipeId.toLong()) }
        verify { commentDao.insert(comment2.detail, comment2.ordinal, recipeId.toLong()) }
    }

    @Test
    fun returnRecipeSummary_when_mapSummary() = runTest(UnconfinedTestDispatcher()) {
        // Given
        val dbSummaries = listOf(
            GetRecipesSummary(1, "recipe 1", null, DbCategoryType.LUNCH),
            GetRecipesSummary(1, "recipe 1", null, DbCategoryType.DINNER),
            GetRecipesSummary(1, "recipe 1", null, null),
            GetRecipesSummary(2, "recipe 2", null, null)
        )
        every {
            recipeDao.getRecipesSummary("a", 1, 2, 3, null)
        } returns flow { emit(dbSummaries) }

        // When
        val summaries = repository.getRecipesSummary("a", 1, 2, 3, null).first()

        // Then
        summaries shouldBe listOf(
            RecipeSummary(1, "recipe 1", null, listOf(CategoryType.LUNCH, CategoryType.DINNER)),
            RecipeSummary(2, "recipe 2", null, listOf())
        )
    }

    @Test
    fun returnRecipe_when_getRecipeById() = runTest {
        // Given
        val id = 0L

        every { recipeDao.getById(id) } returns dataRecipeModel
        every { categoryForRecipeDao.getByRecipeId(id) } returns listOf(
            GetCategoryByRecipeId(0, DbCategoryType.DINNER, null),
            GetCategoryByRecipeId(1, null, "custom")
        )
        every {
            ingredientForRecipeDao.getByRecipeId(id)
        } returns listOf(
            GetIngredientByRecipeId(0, 2.0, DbQuantityType.TABLESPOON, null, "fake ingredient")
        )
        every { instructionDao.getByRecipeId(id) } returns listOf(
            DataInstruction(0, "fake instruction", 1, 0)
        )
        every { commentDao.getByRecipeId(id) } returns listOf(DataComment(0, "fake comment", 1, 0))

        // When
        val recipe = repository.getRecipeById(id.toInt())

        // Then
        recipe.id shouldBe dataRecipeModel.id
        recipe.name shouldBe dataRecipeModel.name
        recipe.portions shouldBe dataRecipeModel.portions
        recipe.timeTotal shouldBe dataRecipeModel.time_total
        recipe.timeCooking shouldBe dataRecipeModel.time_cooking
        recipe.timeWaiting shouldBe dataRecipeModel.time_waiting
        recipe.timePreparation shouldBe dataRecipeModel.time_preparation
        recipe.temperature shouldBe dataRecipeModel.temperature
        recipe.categories shouldBe listOf(CategoryType.DINNER)
        recipe.ingredients shouldBe listOf(
            Ingredient(0, "fake ingredient", 2.0, QuantityType.TABLESPOON)
        )
        recipe.instructions shouldBe listOf(Instruction(1, "fake instruction"))
        recipe.comments shouldBe listOf(Comment(1, "fake comment"))
    }

    @Test
    fun returnRecipeFlow_when_getRecipeByIdAsFlow() = runTest {
        // Given
        val id = 0L

        every { recipeDao.getByIdAsFlow(id) } returns flow { emit(dataRecipeModel) }
        every { categoryForRecipeDao.getByRecipeIdAsFlow(id) } returns flow {
            emit(
                listOf(
                    GetCategoryByRecipeId(0, DbCategoryType.DINNER, null),
                    GetCategoryByRecipeId(1, null, "custom")
                )
            )
        }
        every {
            ingredientForRecipeDao.getByRecipeIdAsFlow(id)
        } returns flow {
            emit(
                listOf(
                    GetIngredientByRecipeId(
                        0,
                        2.0,
                        DbQuantityType.TABLESPOON,
                        null,
                        "fake ingredient"
                    )
                )
            )
        }
        every { instructionDao.getByRecipeIdAsFlow(id) } returns flow {
            emit(listOf(DataInstruction(0, "fake instruction", 1, 0)))
        }
        every { commentDao.getByRecipeIdAsFlow(id) } returns flow {
            emit(listOf(DataComment(0, "fake comment", 1, 0)))
        }

        // When
        val recipe = repository.getRecipeByIdAsFlow(id.toInt()).first()

        // Then
        recipe.id shouldBe dataRecipeModel.id
        recipe.name shouldBe dataRecipeModel.name
        recipe.portions shouldBe dataRecipeModel.portions
        recipe.timeTotal shouldBe dataRecipeModel.time_total
        recipe.timeCooking shouldBe dataRecipeModel.time_cooking
        recipe.timeWaiting shouldBe dataRecipeModel.time_waiting
        recipe.timePreparation shouldBe dataRecipeModel.time_preparation
        recipe.temperature shouldBe dataRecipeModel.temperature
        recipe.categories shouldBe listOf(CategoryType.DINNER)
        recipe.ingredients shouldBe listOf(
            Ingredient(0, "fake ingredient", 2.0, QuantityType.TABLESPOON)
        )
        recipe.instructions shouldBe listOf(Instruction(1, "fake instruction"))
        recipe.comments shouldBe listOf(Comment(1, "fake comment"))
    }

    @Test
    fun addCommentWithNextOrdinal_when_AddComment() = runTest {
        // Given
        val recipeId = 1234
        val existingComment = DataComment(123, "Mix everything", 0, recipeId.toLong())
        val commentToAddName = "Cook in a pan"
        every { commentDao.getByRecipeId(recipeId.toLong()) } returns listOf(existingComment)

        // When
        repository.addComment(recipeId, commentToAddName)

        // Then
        verify { commentDao.insert(commentToAddName, 1, recipeId.toLong()) }
    }

    private val creationRecipeModel = CreationRecipe(
        id = null,
        name = "fake name",
        photoPath = null,
        portions = 1,
        categories = listOf(CategoryType.DINNER),
        instructions = listOf(CreationInstruction("fake instruction", 1)),
        ingredients = listOf(
            CreationIngredient(null, "fake ingredient", 2.0, QuantityType.POUND)
        ),
        timeTotal = 8,
        timePreparation = 7,
        timeCooking = 3,
        timeWaiting = 2,
        temperature = 150,
        temperatureUnit = Temperature.CELSIUS,
        comments = listOf(CreationComment("fake comment", 1))
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
        temperature_type = DbTemperatureType.CELSIUS
    )
}

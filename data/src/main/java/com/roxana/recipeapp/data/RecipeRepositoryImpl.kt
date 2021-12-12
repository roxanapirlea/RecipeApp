package com.roxana.recipeapp.data

import com.roxana.recipeapp.domain.Recipe
import com.roxana.recipeapp.domain.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeQueries: RecipeQueries,
    private val ingredientQueries: IngredientQueries,
    private val ingredientForRecipeQueries: IngredientForRecipeQueries,
    private val categoryForRecipeQueries: CategoryForRecipeQueries,
    private val instructionQueries: InstructionQueries,
    private val commentQueries: CommentQueries
) : RecipeRepository {
    override suspend fun addRecipe(recipe: Recipe) {
        with(recipe) {
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
            val insertedRecipe = recipeQueries.getByName(name).executeAsOne()
            ingredients.forEach {
                ingredientQueries.insert(it.name)
                val ingredient = ingredientQueries.getByName(it.name).executeAsOne()
                ingredientForRecipeQueries.insert(
                    it.quantity,
                    it.quantityType.toDataModel(),
                    null,
                    ingredient.id,
                    insertedRecipe.id
                )
            }
            categories.forEach {
                categoryForRecipeQueries.insert(
                    it.toDataModel(),
                    null,
                    insertedRecipe.id
                )
            }
            instructions.forEach {
                instructionQueries.insert(it.name, it.ordinal, insertedRecipe.id)
            }
            comments.forEach {
                commentQueries.insert(it.detail, it.ordinal, insertedRecipe.id)
            }
        }
    }
}

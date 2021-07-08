package com.roxana.recipeapp.data.di

import android.content.Context
import com.roxana.recipeapp.data.CategoryForRecipe
import com.roxana.recipeapp.data.Database
import com.roxana.recipeapp.data.IngredientForRecipe
import com.roxana.recipeapp.data.RecipeRepositoryImpl
import com.roxana.recipeapp.domain.RecipeRepository
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
        val driver = AndroidSqliteDriver(
            schema = Database.Schema,
            context = context,
            name = "recipes.db"
        )
        return Database(
            driver,
            CategoryForRecipeAdapter = CategoryForRecipe.Adapter(nameAdapter = EnumColumnAdapter()),
            IngredientForRecipeAdapter = IngredientForRecipe.Adapter(
                quantity_nameAdapter = EnumColumnAdapter()
            )
        )
    }

    @Singleton
    @Provides
    fun provideCategoryQueries(database: Database) = database.customCategoryQueries

    @Singleton
    @Provides
    fun provideCategoryForRecipeQueries(database: Database) = database.categoryForRecipeQueries

    @Singleton
    @Provides
    fun provideCommentQueries(database: Database) = database.commentQueries

    @Singleton
    @Provides
    fun provideIngredientQueries(database: Database) = database.ingredientQueries

    @Singleton
    @Provides
    fun provideIngredientForRecipeQueries(database: Database) = database.ingredientForRecipeQueries

    @Singleton
    @Provides
    fun provideInstructionQueries(database: Database) = database.instructionQueries

    @Singleton
    @Provides
    fun provideQuantityTypeQueries(database: Database) = database.customQuantityTypeQueries

    @Singleton
    @Provides
    fun provideRecipeQueries(database: Database) = database.recipeQueries
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModuleBinds {
    @Binds
    abstract fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository
}

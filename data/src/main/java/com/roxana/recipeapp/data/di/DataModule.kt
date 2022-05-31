package com.roxana.recipeapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.roxana.recipeapp.data.CategoryForRecipe
import com.roxana.recipeapp.data.Database
import com.roxana.recipeapp.data.IngredientForRecipe
import com.roxana.recipeapp.data.Recipe
import com.roxana.recipeapp.data.RecipeCreation
import com.roxana.recipeapp.data.RecipeCreationRepositoryImpl
import com.roxana.recipeapp.data.RecipeCreationSerializer
import com.roxana.recipeapp.data.RecipeRepositoryImpl
import com.roxana.recipeapp.data.Settings
import com.roxana.recipeapp.data.SettingsRepositoryImpl
import com.roxana.recipeapp.data.SettingsSerializer
import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.SettingsRepository
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATA_STORE_RECIPE_CREATION_FILE_NAME = "recipe_creation.pb"
private const val DATA_STORE_SETTINGS_FILE_NAME = "app_settings.pb"

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
            ),
            RecipeAdapter = Recipe.Adapter(temperature_typeAdapter = EnumColumnAdapter())
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

    @Singleton
    @Provides
    fun provideRecipeCreationDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<RecipeCreation> {
        return DataStoreFactory.create(
            serializer = RecipeCreationSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_RECIPE_CREATION_FILE_NAME) },
            corruptionHandler = null
        )
    }

    @Singleton
    @Provides
    fun provideSettingsDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Settings> {
        return DataStoreFactory.create(
            serializer = SettingsSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_SETTINGS_FILE_NAME) },
            corruptionHandler = null
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModuleBinds {
    @Binds
    abstract fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository

    @Singleton
    @Binds
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Singleton
    @Binds
    abstract fun bindRecipeCreationRepository(
        impl: RecipeCreationRepositoryImpl
    ): RecipeCreationRepository
}

package com.roxana.recipeapp.data

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature

fun RecipeCreation.Category.toDomainModel(): CategoryType? = when (this) {
    RecipeCreation.Category.BREAKFAST -> CategoryType.BREAKFAST
    RecipeCreation.Category.LUNCH -> CategoryType.LUNCH
    RecipeCreation.Category.DINNER -> CategoryType.DINNER
    RecipeCreation.Category.SNACK -> CategoryType.SNACK
    RecipeCreation.Category.MAIN -> CategoryType.MAIN
    RecipeCreation.Category.SIDE -> CategoryType.SIDE
    RecipeCreation.Category.DESSERT -> CategoryType.DESSERT
    RecipeCreation.Category.DRINK -> CategoryType.DRINK
    RecipeCreation.Category.UNKNOWN_CATEGORY -> null
    RecipeCreation.Category.UNRECOGNIZED -> null
}

fun CategoryType.toProto(): RecipeCreation.Category = when (this) {
    CategoryType.BREAKFAST -> RecipeCreation.Category.BREAKFAST
    CategoryType.LUNCH -> RecipeCreation.Category.LUNCH
    CategoryType.DINNER -> RecipeCreation.Category.DINNER
    CategoryType.SNACK -> RecipeCreation.Category.SNACK
    CategoryType.MAIN -> RecipeCreation.Category.MAIN
    CategoryType.SIDE -> RecipeCreation.Category.SIDE
    CategoryType.DESSERT -> RecipeCreation.Category.DESSERT
    CategoryType.DRINK -> RecipeCreation.Category.DRINK
}

fun RecipeCreation.MeasuringUnit.toDomainModel(): QuantityType? = when (this) {
    RecipeCreation.MeasuringUnit.POUND -> QuantityType.POUND
    RecipeCreation.MeasuringUnit.OUNCE -> QuantityType.OUNCE
    RecipeCreation.MeasuringUnit.GRAM -> QuantityType.GRAM
    RecipeCreation.MeasuringUnit.KILOGRAM -> QuantityType.KILOGRAM
    RecipeCreation.MeasuringUnit.TEASPOON -> QuantityType.TEASPOON
    RecipeCreation.MeasuringUnit.TABLESPOON -> QuantityType.TABLESPOON
    RecipeCreation.MeasuringUnit.FLUID_OUNCE -> QuantityType.FLUID_OUNCE
    RecipeCreation.MeasuringUnit.GILL -> QuantityType.GILL
    RecipeCreation.MeasuringUnit.CUP -> QuantityType.CUP
    RecipeCreation.MeasuringUnit.PINT -> QuantityType.PINT
    RecipeCreation.MeasuringUnit.QUART -> QuantityType.QUART
    RecipeCreation.MeasuringUnit.GALLON -> QuantityType.GALLON
    RecipeCreation.MeasuringUnit.LITER -> QuantityType.LITER
    RecipeCreation.MeasuringUnit.DECILITER -> QuantityType.DECILITER
    RecipeCreation.MeasuringUnit.CENTILITER -> QuantityType.CENTILITER
    RecipeCreation.MeasuringUnit.MILLILITER -> QuantityType.MILLILITER
    RecipeCreation.MeasuringUnit.UNRECOGNIZED -> null
    RecipeCreation.MeasuringUnit.UNKNOWN_MEASURE -> null
}

fun QuantityType.toCreationProto(): RecipeCreation.MeasuringUnit = when (this) {
    QuantityType.POUND -> RecipeCreation.MeasuringUnit.POUND
    QuantityType.OUNCE -> RecipeCreation.MeasuringUnit.OUNCE
    QuantityType.GRAM -> RecipeCreation.MeasuringUnit.GRAM
    QuantityType.KILOGRAM -> RecipeCreation.MeasuringUnit.KILOGRAM
    QuantityType.TEASPOON -> RecipeCreation.MeasuringUnit.TEASPOON
    QuantityType.TABLESPOON -> RecipeCreation.MeasuringUnit.TABLESPOON
    QuantityType.FLUID_OUNCE -> RecipeCreation.MeasuringUnit.FLUID_OUNCE
    QuantityType.GILL -> RecipeCreation.MeasuringUnit.GILL
    QuantityType.CUP -> RecipeCreation.MeasuringUnit.CUP
    QuantityType.PINT -> RecipeCreation.MeasuringUnit.PINT
    QuantityType.QUART -> RecipeCreation.MeasuringUnit.QUART
    QuantityType.GALLON -> RecipeCreation.MeasuringUnit.GALLON
    QuantityType.LITER -> RecipeCreation.MeasuringUnit.LITER
    QuantityType.DECILITER -> RecipeCreation.MeasuringUnit.DECILITER
    QuantityType.CENTILITER -> RecipeCreation.MeasuringUnit.CENTILITER
    QuantityType.MILLILITER -> RecipeCreation.MeasuringUnit.MILLILITER
}

fun RecipeCreation.Temperature.toDomainModel(): Temperature? = when (this) {
    RecipeCreation.Temperature.CELSIUS -> Temperature.CELSIUS
    RecipeCreation.Temperature.FAHRENHEIT -> Temperature.FAHRENHEIT
    RecipeCreation.Temperature.UNRECOGNIZED -> null
}

fun Temperature.toCreationProto(): RecipeCreation.Temperature = when (this) {
    Temperature.CELSIUS -> RecipeCreation.Temperature.CELSIUS
    Temperature.FAHRENHEIT -> RecipeCreation.Temperature.FAHRENHEIT
}

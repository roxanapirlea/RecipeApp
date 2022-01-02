package com.roxana.recipeapp.data

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature

fun CategoryType.toDataModel(): DbCategoryType = when (this) {
    CategoryType.BREAKFAST -> DbCategoryType.BREAKFAST
    CategoryType.LUNCH -> DbCategoryType.LUNCH
    CategoryType.DINNER -> DbCategoryType.DINNER
    CategoryType.SNACK -> DbCategoryType.SNACK
    CategoryType.MAIN -> DbCategoryType.MAIN
    CategoryType.SIDE -> DbCategoryType.SIDE
    CategoryType.DESSERT -> DbCategoryType.DESSERT
    CategoryType.DRINK -> DbCategoryType.DRINK
}

fun QuantityType?.toDataModel(): DbQuantityType? = when (this) {
    QuantityType.POUND -> DbQuantityType.POUND
    QuantityType.OUNCE -> DbQuantityType.OUNCE
    QuantityType.GRAM -> DbQuantityType.GRAM
    QuantityType.KILOGRAM -> DbQuantityType.KILOGRAM
    QuantityType.TEASPOON -> DbQuantityType.TEASPOON
    QuantityType.TABLESPOON -> DbQuantityType.TABLESPOON
    QuantityType.FLUID_OUNCE -> DbQuantityType.FLUID_OUNCE
    QuantityType.GILL -> DbQuantityType.GILL
    QuantityType.CUP -> DbQuantityType.CUP
    QuantityType.PINT -> DbQuantityType.PINT
    QuantityType.QUART -> DbQuantityType.QUART
    QuantityType.GALLON -> DbQuantityType.GALLON
    QuantityType.LITER -> DbQuantityType.LITER
    QuantityType.DECILITER -> DbQuantityType.DECILITER
    QuantityType.CENTILITER -> DbQuantityType.CENTILITER
    QuantityType.MILLILITER -> DbQuantityType.MILLILITER
    null -> null
}

fun Temperature?.toDataModel(): DbTemperatureType? = when (this) {
    Temperature.CELSIUS -> DbTemperatureType.CELSIUS
    Temperature.FAHRENHEIT -> DbTemperatureType.FAHRENHEIT
    null -> null
}

fun DbCategoryType.toDomainModel(): CategoryType = when (this) {
    DbCategoryType.BREAKFAST -> CategoryType.BREAKFAST
    DbCategoryType.LUNCH -> CategoryType.LUNCH
    DbCategoryType.DINNER -> CategoryType.DINNER
    DbCategoryType.SNACK -> CategoryType.SNACK
    DbCategoryType.MAIN -> CategoryType.MAIN
    DbCategoryType.SIDE -> CategoryType.SIDE
    DbCategoryType.DESSERT -> CategoryType.DESSERT
    DbCategoryType.DRINK -> CategoryType.DRINK
}

fun DbQuantityType?.toDomainModel(): QuantityType? = when (this) {
    DbQuantityType.POUND -> QuantityType.POUND
    DbQuantityType.OUNCE -> QuantityType.OUNCE
    DbQuantityType.GRAM -> QuantityType.GRAM
    DbQuantityType.KILOGRAM -> QuantityType.KILOGRAM
    DbQuantityType.TEASPOON -> QuantityType.TEASPOON
    DbQuantityType.TABLESPOON -> QuantityType.TABLESPOON
    DbQuantityType.FLUID_OUNCE -> QuantityType.FLUID_OUNCE
    DbQuantityType.GILL -> QuantityType.GILL
    DbQuantityType.CUP -> QuantityType.CUP
    DbQuantityType.PINT -> QuantityType.PINT
    DbQuantityType.QUART -> QuantityType.QUART
    DbQuantityType.GALLON -> QuantityType.GALLON
    DbQuantityType.LITER -> QuantityType.LITER
    DbQuantityType.DECILITER -> QuantityType.DECILITER
    DbQuantityType.CENTILITER -> QuantityType.CENTILITER
    DbQuantityType.MILLILITER -> QuantityType.MILLILITER
    null -> null
}

fun DbTemperatureType?.toDomainModel(): Temperature? = when (this) {
    DbTemperatureType.CELSIUS -> Temperature.CELSIUS
    DbTemperatureType.FAHRENHEIT -> Temperature.FAHRENHEIT
    null -> null
}

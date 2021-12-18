package com.roxana.recipeapp.data

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.QuantityType

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
    null -> null
}

package com.roxana.recipeapp.uimodel

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.QuantityType

fun CategoryType.toUiModel(): UiCategoryType = when (this) {
    CategoryType.BREAKFAST -> UiCategoryType.Breakfast
    CategoryType.LUNCH -> UiCategoryType.Lunch
    CategoryType.DINNER -> UiCategoryType.Dinner
    CategoryType.SNACK -> UiCategoryType.Snack
    CategoryType.MAIN -> UiCategoryType.Main
    CategoryType.SIDE -> UiCategoryType.Side
    CategoryType.DESSERT -> UiCategoryType.Dessert
    CategoryType.DRINK -> UiCategoryType.Drink
}

fun QuantityType?.toUiModel(): UiQuantityType = when (this) {
    QuantityType.POUND -> UiQuantityType.Pound
    QuantityType.OUNCE -> UiQuantityType.Ounce
    QuantityType.GRAM -> UiQuantityType.Gram
    QuantityType.KILOGRAM -> UiQuantityType.Kilogram
    QuantityType.TEASPOON -> UiQuantityType.Teaspoon
    QuantityType.TABLESPOON -> UiQuantityType.Tablespoon
    QuantityType.FLUID_OUNCE -> UiQuantityType.FluidOunce
    QuantityType.GILL -> UiQuantityType.Gill
    QuantityType.CUP -> UiQuantityType.Cup
    QuantityType.PINT -> UiQuantityType.Pint
    QuantityType.QUART -> UiQuantityType.Quart
    QuantityType.GALLON -> UiQuantityType.Gallon
    QuantityType.LITER -> UiQuantityType.Liter
    QuantityType.DECILITER -> UiQuantityType.Deciliter
    QuantityType.CENTILITER -> UiQuantityType.Centiliter
    null -> UiQuantityType.None
}

fun UiCategoryType.toDomainModel(): CategoryType = when (this) {
    is UiCategoryType.Breakfast -> CategoryType.BREAKFAST
    is UiCategoryType.Lunch -> CategoryType.LUNCH
    is UiCategoryType.Dinner -> CategoryType.DINNER
    is UiCategoryType.Snack -> CategoryType.SNACK
    is UiCategoryType.Main -> CategoryType.MAIN
    is UiCategoryType.Side -> CategoryType.SIDE
    is UiCategoryType.Dessert -> CategoryType.DESSERT
    is UiCategoryType.Drink -> CategoryType.DRINK
}

fun UiQuantityType.toDomainModel(): QuantityType? = when (this) {
    UiQuantityType.Pound -> QuantityType.POUND
    UiQuantityType.Ounce -> QuantityType.OUNCE
    UiQuantityType.Gram -> QuantityType.GRAM
    UiQuantityType.Kilogram -> QuantityType.KILOGRAM
    UiQuantityType.Teaspoon -> QuantityType.TEASPOON
    UiQuantityType.Tablespoon -> QuantityType.TABLESPOON
    UiQuantityType.FluidOunce -> QuantityType.FLUID_OUNCE
    UiQuantityType.Gill -> QuantityType.GILL
    UiQuantityType.Cup -> QuantityType.CUP
    UiQuantityType.Pint -> QuantityType.PINT
    UiQuantityType.Quart -> QuantityType.QUART
    UiQuantityType.Gallon -> QuantityType.GALLON
    UiQuantityType.Liter -> QuantityType.LITER
    UiQuantityType.Deciliter -> QuantityType.DECILITER
    UiQuantityType.Centiliter -> QuantityType.CENTILITER
    UiQuantityType.None -> null
}

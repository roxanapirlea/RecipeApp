package com.roxana.recipeapp.misc

import androidx.annotation.StringRes
import com.roxana.recipeapp.R
import com.roxana.recipeapp.domain.CategoryType
import com.roxana.recipeapp.domain.QuantityType

@StringRes
fun CategoryType.toStringRes(): Int =
    when (this) {
        CategoryType.BREAKFAST -> R.string.all_category_breakfast
        CategoryType.LUNCH -> R.string.all_category_lunch
        CategoryType.DINNER -> R.string.all_category_dinner
        CategoryType.SNACK -> R.string.all_category_snack
        CategoryType.MAIN -> R.string.all_category_main
        CategoryType.SIDE -> R.string.all_category_side
        CategoryType.DESSERT -> R.string.all_category_dessert
        CategoryType.DRINK -> R.string.all_category_drink
    }

@StringRes
fun QuantityType?.toStringRes(): Int =
    when (this) {
        QuantityType.POUND -> R.string.all_quantity_pound
        QuantityType.OUNCE -> R.string.all_quantity_ounce
        QuantityType.GRAM -> R.string.all_quantity_g
        QuantityType.KILOGRAM -> R.string.all_quantity_kg
        QuantityType.TEASPOON -> R.string.all_quantity_teaspoon
        QuantityType.TABLESPOON -> R.string.all_quantity_tablespoon
        QuantityType.FLUID_OUNCE -> R.string.all_quantity_fl_ounce
        QuantityType.GILL -> R.string.all_quantity_gill
        QuantityType.CUP -> R.string.all_quantity_cup
        QuantityType.PINT -> R.string.all_quantity_pint
        QuantityType.QUART -> R.string.all_quantity_quart
        QuantityType.GALLON -> R.string.all_quantity_gallon
        QuantityType.LITER -> R.string.all_quantity_liter
        QuantityType.DECILITER -> R.string.all_quantity_deciliter
        QuantityType.CENTILITER -> R.string.all_quantity_centiliter
        null -> R.string.all_quantity_select
    }

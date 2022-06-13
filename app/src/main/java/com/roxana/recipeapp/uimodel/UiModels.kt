package com.roxana.recipeapp.uimodel

import androidx.annotation.StringRes
import com.roxana.recipeapp.R

sealed class UiCategoryType(@StringRes val text: Int) : java.io.Serializable {
    object Breakfast : UiCategoryType(R.string.all_category_breakfast)
    object Lunch : UiCategoryType(R.string.all_category_lunch)
    object Dinner : UiCategoryType(R.string.all_category_dinner)
    object Snack : UiCategoryType(R.string.all_category_snack)
    object Main : UiCategoryType(R.string.all_category_main)
    object Side : UiCategoryType(R.string.all_category_side)
    object Dessert : UiCategoryType(R.string.all_category_dessert)
    object Drink : UiCategoryType(R.string.all_category_drink)
}

sealed class UiQuantityType(
    @StringRes val textForSelect: Int,
    @StringRes val textForSelected: Int,
    @StringRes val longDescription: Int
) {
    object Pound : UiQuantityType(
        R.string.all_quantity_pound,
        R.string.all_quantity_pound,
        R.string.all_quantity_complete_pound
    )

    object Ounce : UiQuantityType(
        R.string.all_quantity_ounce,
        R.string.all_quantity_ounce,
        R.string.all_quantity_complete_ounce
    )

    object Gram : UiQuantityType(
        R.string.all_quantity_g,
        R.string.all_quantity_g,
        R.string.all_quantity_complete_g
    )

    object Kilogram : UiQuantityType(
        R.string.all_quantity_kg,
        R.string.all_quantity_kg,
        R.string.all_quantity_complete_kg
    )

    object Teaspoon : UiQuantityType(
        R.string.all_quantity_teaspoon,
        R.string.all_quantity_teaspoon,
        R.string.all_quantity_complete_teaspoon
    )

    object Tablespoon : UiQuantityType(
        R.string.all_quantity_tablespoon,
        R.string.all_quantity_tablespoon,
        R.string.all_quantity_complete_tablespoon
    )

    object FluidOunce : UiQuantityType(
        R.string.all_quantity_fl_ounce,
        R.string.all_quantity_fl_ounce,
        R.string.all_quantity_complete_fl_ounce
    )

    object Gill : UiQuantityType(
        R.string.all_quantity_gill,
        R.string.all_quantity_gill,
        R.string.all_quantity_complete_gill
    )

    object Cup : UiQuantityType(
        R.string.all_quantity_cup,
        R.string.all_quantity_cup,
        R.string.all_quantity_complete_cup
    )

    object Pint : UiQuantityType(
        R.string.all_quantity_pint,
        R.string.all_quantity_pint,
        R.string.all_quantity_complete_pint
    )

    object Quart : UiQuantityType(
        R.string.all_quantity_quart,
        R.string.all_quantity_quart,
        R.string.all_quantity_complete_quart
    )

    object Gallon : UiQuantityType(
        R.string.all_quantity_gallon,
        R.string.all_quantity_gallon,
        R.string.all_quantity_complete_gallon
    )

    object Liter : UiQuantityType(
        R.string.all_quantity_liter,
        R.string.all_quantity_liter,
        R.string.all_quantity_complete_liter
    )

    object Deciliter : UiQuantityType(
        R.string.all_quantity_deciliter,
        R.string.all_quantity_deciliter,
        R.string.all_quantity_complete_deciliter
    )

    object Centiliter : UiQuantityType(
        R.string.all_quantity_centiliter,
        R.string.all_quantity_centiliter,
        R.string.all_quantity_complete_centiliter
    )

    object Milliliter : UiQuantityType(
        R.string.all_quantity_milliliter,
        R.string.all_quantity_milliliter,
        R.string.all_quantity_complete_milliliter
    )

    object None :
        UiQuantityType(R.string.all_select, R.string.all_quantity_none, R.string.all_quantity_none)
}

sealed class UiTemperature(@StringRes val text: Int) {
    object Celsius : UiTemperature(R.string.all_temperature_celsius)
    object Fahrenheit : UiTemperature(R.string.all_temperature_fahrenheit)
}

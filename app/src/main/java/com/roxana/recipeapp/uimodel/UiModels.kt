package com.roxana.recipeapp.uimodel

import androidx.annotation.StringRes
import com.roxana.recipeapp.R

sealed class UiCategoryType(@StringRes val text: Int) {
    object Breakfast : UiCategoryType(R.string.all_category_breakfast)
    object Lunch : UiCategoryType(R.string.all_category_lunch)
    object Dinner : UiCategoryType(R.string.all_category_dinner)
    object Snack : UiCategoryType(R.string.all_category_snack)
    object Main : UiCategoryType(R.string.all_category_main)
    object Side : UiCategoryType(R.string.all_category_side)
    object Dessert : UiCategoryType(R.string.all_category_dessert)
    object Drink : UiCategoryType(R.string.all_category_drink)
}

sealed class UiQuantityType(@StringRes val text: Int) {
    object Pound : UiQuantityType(R.string.all_quantity_pound)
    object Ounce : UiQuantityType(R.string.all_quantity_ounce)
    object Gram : UiQuantityType(R.string.all_quantity_g)
    object Kilogram : UiQuantityType(R.string.all_quantity_kg)
    object Teaspoon : UiQuantityType(R.string.all_quantity_teaspoon)
    object Tablespoon : UiQuantityType(R.string.all_quantity_tablespoon)
    object FluidOunce : UiQuantityType(R.string.all_quantity_fl_ounce)
    object Gill : UiQuantityType(R.string.all_quantity_gill)
    object Cup : UiQuantityType(R.string.all_quantity_cup)
    object Pint : UiQuantityType(R.string.all_quantity_pint)
    object Quart : UiQuantityType(R.string.all_quantity_quart)
    object Gallon : UiQuantityType(R.string.all_quantity_gallon)
    object Liter : UiQuantityType(R.string.all_quantity_liter)
    object Deciliter : UiQuantityType(R.string.all_quantity_deciliter)
    object Centiliter : UiQuantityType(R.string.all_quantity_centiliter)
    object None : UiQuantityType(R.string.all_select)
}

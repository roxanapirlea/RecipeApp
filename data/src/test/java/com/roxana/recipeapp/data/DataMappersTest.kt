package com.roxana.recipeapp.data

import com.roxana.recipeapp.domain.CategoryType
import com.roxana.recipeapp.domain.QuantityType
import io.kotest.matchers.shouldBe
import org.junit.Test

class DataMappersTest {
    @Test
    fun return_breakfast_when_categoryToDataModel_given_breakfastDomainModel() {
        // Given
        val model = CategoryType.BREAKFAST

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbCategoryType.BREAKFAST
    }

    @Test
    fun return_lunch_when_categoryToDataModel_given_lunchDomainModel() {
        // Given
        val model = CategoryType.LUNCH

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbCategoryType.LUNCH
    }

    @Test
    fun return_dinner_when_categoryToDataModel_given_dinnerDomainModel() {
        // Given
        val model = CategoryType.DINNER

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbCategoryType.DINNER
    }

    @Test
    fun return_snack_when_categoryToDataModel_given_snackDomainModel() {
        // Given
        val model = CategoryType.SNACK

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbCategoryType.SNACK
    }

    @Test
    fun return_main_when_categoryToDataModel_given_mainDomainModel() {
        // Given
        val model = CategoryType.MAIN

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbCategoryType.MAIN
    }

    @Test
    fun return_side_when_categoryToDataModel_given_sideDomainModel() {
        // Given
        val model = CategoryType.SIDE

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbCategoryType.SIDE
    }

    @Test
    fun return_dessert_when_categoryToDataModel_given_dessertDomainModel() {
        // Given
        val model = CategoryType.DESSERT

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbCategoryType.DESSERT
    }

    @Test
    fun return_drink_when_categoryToDataModel_given_drinkDomainModel() {
        // Given
        val model = CategoryType.DRINK

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbCategoryType.DRINK
    }

    @Test
    fun return_pound_when_quantityToDataModel_given_poundDomainModel() {
        // Given
        val model = QuantityType.POUND

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.POUND
    }

    @Test
    fun return_ounce_when_quantityToDataModel_given_ounceDomainModel() {
        // Given
        val model = QuantityType.OUNCE

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.OUNCE
    }

    @Test
    fun return_gram_when_quantityToDataModel_given_gramDomainModel() {
        // Given
        val model = QuantityType.GRAM

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.GRAM
    }

    @Test
    fun return_kilogram_when_quantityToDataModel_given_kilogramDomainModel() {
        // Given
        val model = QuantityType.KILOGRAM

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.KILOGRAM
    }

    @Test
    fun return_teaspoon_when_quantityToDataModel_given_teaspoonDomainModel() {
        // Given
        val model = QuantityType.TEASPOON

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.TEASPOON
    }

    @Test
    fun return_tablespoon_when_quantityToDataModel_given_tablespoonDomainModel() {
        // Given
        val model = QuantityType.TABLESPOON

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.TABLESPOON
    }

    @Test
    fun return_FluidOunce_when_quantityToDataModel_given_FluidOunceDomainModel() {
        // Given
        val model = QuantityType.FLUID_OUNCE

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.FLUID_OUNCE
    }

    @Test
    fun return_gill_when_quantityToDataModel_given_gillDomainModel() {
        // Given
        val model = QuantityType.GILL

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.GILL
    }

    @Test
    fun return_cup_when_quantityToDataModel_given_cupDomainModel() {
        // Given
        val model = QuantityType.CUP

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.CUP
    }

    @Test
    fun return_pint_when_quantityToDataModel_given_pintDomainModel() {
        // Given
        val model = QuantityType.PINT

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.PINT
    }

    @Test
    fun return_quart_when_quantityToDataModel_given_quartDomainModel() {
        // Given
        val model = QuantityType.QUART

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.QUART
    }

    @Test
    fun return_gallon_when_quantityToDataModel_given_gallonDomainModel() {
        // Given
        val model = QuantityType.GALLON

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.GALLON
    }

    @Test
    fun return_liter_when_quantityToDataModel_given_literDomainModel() {
        // Given
        val model = QuantityType.LITER

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.LITER
    }

    @Test
    fun return_deciliter_when_quantityToDataModel_given_deciliterDomainModel() {
        // Given
        val model = QuantityType.DECILITER

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.DECILITER
    }

    @Test
    fun return_centiliter_when_quantityToDataModel_given_centiliterDomainModel() {
        // Given
        val model = QuantityType.CENTILITER

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.CENTILITER
    }

    @Test
    fun return_null_when_quantityToDataModel_given_nullDomainModel() {
        // Given
        val model: QuantityType? = null

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe null
    }
}

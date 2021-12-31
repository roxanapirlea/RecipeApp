package com.roxana.recipeapp.uimodel

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.Test

class UiMappersTest {

    @Test
    fun setUiCategoryTypeBreakfast_when_CategoryTypeBreakfast() {
        // Given
        val categoryType = CategoryType.BREAKFAST

        // When
        val uiCategoryType = categoryType.toUiModel()

        // Then
        uiCategoryType shouldBe UiCategoryType.Breakfast
    }

    @Test
    fun setUiCategoryTypeLunch_when_CategoryTypeLunch() {
        // Given
        val categoryType = CategoryType.LUNCH

        // When
        val uiCategoryType = categoryType.toUiModel()

        // Then
        uiCategoryType shouldBe UiCategoryType.Lunch
    }

    @Test
    fun setUiCategoryTypeDinner_when_CategoryTypeDinner() {
        // Given
        val categoryType = CategoryType.DINNER

        // When
        val uiCategoryType = categoryType.toUiModel()

        // Then
        uiCategoryType shouldBe UiCategoryType.Dinner
    }

    @Test
    fun setUiCategoryTypeSnack_when_CategoryTypeSnack() {
        // Given
        val categoryType = CategoryType.SNACK

        // When
        val uiCategoryType = categoryType.toUiModel()

        // Then
        uiCategoryType shouldBe UiCategoryType.Snack
    }

    @Test
    fun setUiCategoryTypeMain_when_CategoryTypeMain() {
        // Given
        val categoryType = CategoryType.MAIN

        // When
        val uiCategoryType = categoryType.toUiModel()

        // Then
        uiCategoryType shouldBe UiCategoryType.Main
    }

    @Test
    fun setUiCategoryTypeSide_when_CategoryTypeSide() {
        // Given
        val categoryType = CategoryType.SIDE

        // When
        val uiCategoryType = categoryType.toUiModel()

        // Then
        uiCategoryType shouldBe UiCategoryType.Side
    }

    @Test
    fun setUiCategoryTypeDessert_when_CategoryTypeDessert() {
        // Given
        val categoryType = CategoryType.DESSERT

        // When
        val uiCategoryType = categoryType.toUiModel()

        // Then
        uiCategoryType shouldBe UiCategoryType.Dessert
    }

    @Test
    fun setUiCategoryTypeDrink_when_CategoryTypeDrink() {
        // Given
        val categoryType = CategoryType.DRINK

        // When
        val uiCategoryType = categoryType.toUiModel()

        // Then
        uiCategoryType shouldBe UiCategoryType.Drink
    }

    @Test
    fun setCategoryTypeBreakfast_when_UiCategoryTypeBreakfast() {
        // Given
        val uiCategoryType = UiCategoryType.Breakfast

        // When
        val categoryType = uiCategoryType.toDomainModel()

        // Then
        categoryType shouldBe CategoryType.BREAKFAST
    }

    @Test
    fun setCategoryTypeLunch_when_UiCategoryTypeLunch() {
        // Given
        val uiCategoryType = UiCategoryType.Lunch

        // When
        val categoryType = uiCategoryType.toDomainModel()

        // Then
        categoryType shouldBe CategoryType.LUNCH
    }

    @Test
    fun setCategoryTypeDinner_when_UiCategoryTypeDinner() {
        // Given
        val uiCategoryType = UiCategoryType.Dinner

        // When
        val categoryType = uiCategoryType.toDomainModel()

        // Then
        categoryType shouldBe CategoryType.DINNER
    }

    @Test
    fun setCategoryTypeSnack_when_UiCategoryTypeSnack() {
        // Given
        val uiCategoryType = UiCategoryType.Snack

        // When
        val categoryType = uiCategoryType.toDomainModel()

        // Then
        categoryType shouldBe CategoryType.SNACK
    }

    @Test
    fun setCategoryTypeMain_when_UiCategoryTypeMain() {
        // Given
        val uiCategoryType = UiCategoryType.Main

        // When
        val categoryType = uiCategoryType.toDomainModel()

        // Then
        categoryType shouldBe CategoryType.MAIN
    }

    @Test
    fun setCategoryTypeSide_when_UiCategoryTypeSide() {
        // Given
        val uiCategoryType = UiCategoryType.Side

        // When
        val categoryType = uiCategoryType.toDomainModel()

        // Then
        categoryType shouldBe CategoryType.SIDE
    }

    @Test
    fun setCategoryTypeDessert_when_UiCategoryTypeDessert() {
        // Given
        val uiCategoryType = UiCategoryType.Dessert

        // When
        val categoryType = uiCategoryType.toDomainModel()

        // Then
        categoryType shouldBe CategoryType.DESSERT
    }

    @Test
    fun setCategoryTypeDrink_when_UiCategoryTypeDrink() {
        // Given
        val uiCategoryType = UiCategoryType.Drink

        // When
        val categoryType = uiCategoryType.toDomainModel()

        // Then
        categoryType shouldBe CategoryType.DRINK
    }

    @Test
    fun setUiQuantityTypePound_when_QuantityTypePound() {
        // Given
        val quantityType = QuantityType.POUND

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Pound
    }

    @Test
    fun setUiQuantityTypeOunce_when_QuantityTypeOunce() {
        // Given
        val quantityType = QuantityType.OUNCE

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Ounce
    }

    @Test
    fun setUiQuantityTypeGram_when_QuantityTypeGram() {
        // Given
        val quantityType = QuantityType.GRAM

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Gram
    }

    @Test
    fun setUiQuantityTypeKilogram_when_QuantityTypeKilogram() {
        // Given
        val quantityType = QuantityType.KILOGRAM

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Kilogram
    }

    @Test
    fun setUiQuantityTypeTeaspoon_when_QuantityTypeTeaspoon() {
        // Given
        val quantityType = QuantityType.TEASPOON

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Teaspoon
    }

    @Test
    fun setUiQuantityTypeTablespoon_when_QuantityTypeTablespoon() {
        // Given
        val quantityType = QuantityType.TABLESPOON

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Tablespoon
    }

    @Test
    fun setUiQuantityTypeFluidOunce_when_QuantityTypeFluidOunce() {
        // Given
        val quantityType = QuantityType.FLUID_OUNCE

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.FluidOunce
    }

    @Test
    fun setUiQuantityTypeGill_when_QuantityTypeGill() {
        // Given
        val quantityType = QuantityType.GILL

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Gill
    }

    @Test
    fun setUiQuantityTypeCup_when_QuantityTypeCup() {
        // Given
        val quantityType = QuantityType.CUP

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Cup
    }

    @Test
    fun setUiQuantityTypePint_when_QuantityTypePint() {
        // Given
        val quantityType = QuantityType.PINT

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Pint
    }

    @Test
    fun setUiQuantityTypeQuart_when_QuantityTypeQuart() {
        // Given
        val quantityType = QuantityType.QUART

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Quart
    }

    @Test
    fun setUiQuantityTypeGallon_when_QuantityTypeGallon() {
        // Given
        val quantityType = QuantityType.GALLON

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Gallon
    }

    @Test
    fun setUiQuantityTypeLitter_when_QuantityTypeLitter() {
        // Given
        val quantityType = QuantityType.LITER

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Liter
    }

    @Test
    fun setUiQuantityTypeDeciliter_when_QuantityTypeDeciliter() {
        // Given
        val quantityType = QuantityType.DECILITER

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Deciliter
    }

    @Test
    fun setUiQuantityTypeCentiliter_when_QuantityTypeCentiliter() {
        // Given
        val quantityType = QuantityType.CENTILITER

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.Centiliter
    }

    @Test
    fun setUiQuantityTypeNone_when_QuantityTypeNull() {
        // Given
        val quantityType = null

        // When
        val uiQuantityType = quantityType.toUiModel()

        // Then
        uiQuantityType shouldBe UiQuantityType.None
    }

    @Test
    fun setQuantityTypePound_when_UiQuantityTypePound() {
        // Given
        val uiQuantityType = UiQuantityType.Pound

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.POUND
    }

    @Test
    fun setQuantityTypeOunce_when_UiQuantityTypeOunce() {
        // Given
        val uiQuantityType = UiQuantityType.Ounce

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.OUNCE
    }

    @Test
    fun setQuantityTypeGram_when_UiQuantityTypeGram() {
        // Given
        val uiQuantityType = UiQuantityType.Gram

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.GRAM
    }

    @Test
    fun setQuantityTypeKilogram_when_UiQuantityTypeKilogram() {
        // Given
        val uiQuantityType = UiQuantityType.Kilogram

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.KILOGRAM
    }

    @Test
    fun setQuantityTypeTeaspoon_when_UiQuantityTypeTeaspoon() {
        // Given
        val uiQuantityType = UiQuantityType.Teaspoon

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.TEASPOON
    }

    @Test
    fun setQuantityTypeTablespoon_when_UiQuantityTypeTablespoon() {
        // Given
        val uiQuantityType = UiQuantityType.Tablespoon

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.TABLESPOON
    }

    @Test
    fun setQuantityTypeFluidOunce_when_UiQuantityTypeFluidOunce() {
        // Given
        val uiQuantityType = UiQuantityType.FluidOunce

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.FLUID_OUNCE
    }

    @Test
    fun setQuantityTypeGill_when_UiQuantityTypeGill() {
        // Given
        val uiQuantityType = UiQuantityType.Gill

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.GILL
    }

    @Test
    fun setQuantityTypeCup_when_UiQuantityTypeCup() {
        // Given
        val uiQuantityType = UiQuantityType.Cup

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.CUP
    }

    @Test
    fun setQuantityTypePint_when_UiQuantityTypePint() {
        // Given
        val uiQuantityType = UiQuantityType.Pint

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.PINT
    }

    @Test
    fun setQuantityTypeQuart_when_UiQuantityTypeQuart() {
        // Given
        val uiQuantityType = UiQuantityType.Quart

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.QUART
    }

    @Test
    fun setQuantityTypeGallon_when_UiQuantityTypeGallon() {
        // Given
        val uiQuantityType = UiQuantityType.Gallon

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.GALLON
    }

    @Test
    fun setQuantityTypeLitter_when_UiQuantityTypeLitter() {
        // Given
        val uiQuantityType = UiQuantityType.Liter

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.LITER
    }

    @Test
    fun setQuantityTypeDeciliter_when_UiQuantityTypeDeciliter() {
        // Given
        val uiQuantityType = UiQuantityType.Deciliter

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.DECILITER
    }

    @Test
    fun setQuantityTypeCentiliter_when_UiQuantityTypeCentiliter() {
        // Given
        val uiQuantityType = UiQuantityType.Centiliter

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType shouldBe QuantityType.CENTILITER
    }

    @Test
    fun setQuantityTypeNull_when_UiQuantityTypeNone() {
        // Given
        val uiQuantityType = UiQuantityType.None

        // When
        val quantityType = uiQuantityType.toDomainModel()

        // Then
        quantityType.shouldBeNull()
    }

    @Test
    fun setUiTemperatureCelsius_when_TemperatureCelsius() {
        // Given
        val temperature = Temperature.CELSIUS

        // When
        val uiTemperature = temperature.toUiModel()

        // Then
        uiTemperature shouldBe UiTemperature.Celsius
    }

    @Test
    fun setUiTemperatureFahrenheit_when_TemperatureFahrenheit() {
        // Given
        val temperature = Temperature.FAHRENHEIT

        // When
        val uiTemperature = temperature.toUiModel()

        // Then
        uiTemperature shouldBe UiTemperature.Fahrenheit
    }

    @Test
    fun setTemperatureCelsius_when_UiTemperatureCelsius() {
        // Given
        val uiTemperature = UiTemperature.Celsius

        // When
        val temperature = uiTemperature.toDomainModel()

        // Then
        temperature shouldBe Temperature.CELSIUS
    }

    @Test
    fun setTemperatureFahrenheit_when_UiTemperatureFahrenheit() {
        // Given
        val uiTemperature = UiTemperature.Fahrenheit

        // When
        val temperature = uiTemperature.toDomainModel()

        // Then
        temperature shouldBe Temperature.FAHRENHEIT
    }
}

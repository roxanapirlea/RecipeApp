package com.roxana.recipeapp.data.recipe

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature
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
    fun return_milliliter_when_quantityToDataModel_given_milliliterDomainModel() {
        // Given
        val model = QuantityType.MILLILITER

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbQuantityType.MILLILITER
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

    @Test
    fun return_celsius_when_temperatureToDataModel_given_celsiusDomainModel() {
        // Given
        val model = Temperature.CELSIUS

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbTemperatureType.CELSIUS
    }

    @Test
    fun return_fahrenheit_when_temperatureToDataModel_given_fahrenheitDomainModel() {
        // Given
        val model = Temperature.FAHRENHEIT

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe DbTemperatureType.FAHRENHEIT
    }

    @Test
    fun return_null_when_temperatureToDataModel_given_nullDomainModel() {
        // Given
        val model: Temperature? = null

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe null
    }

    @Test
    fun return_breakfast_when_categoryToDomainModel_given_breakfastDataModel() {
        // Given
        val model = DbCategoryType.BREAKFAST

        // When
        val dataModel = model.toDomainModel()

        // Then
        dataModel shouldBe CategoryType.BREAKFAST
    }

    @Test
    fun return_lunch_when_categoryToDomainModel_given_lunchDataModel() {
        // Given
        val model = DbCategoryType.LUNCH

        // When
        val dataModel = model.toDomainModel()

        // Then
        dataModel shouldBe CategoryType.LUNCH
    }

    @Test
    fun return_dinner_when_categoryToDomainModel_given_dinnerDataModel() {
        // Given
        val model = DbCategoryType.DINNER

        // When
        val dataModel = model.toDomainModel()

        // Then
        dataModel shouldBe CategoryType.DINNER
    }

    @Test
    fun return_snack_when_categoryToDomainModel_given_snackDataModel() {
        // Given
        val model = DbCategoryType.SNACK

        // When
        val dataModel = model.toDomainModel()

        // Then
        dataModel shouldBe CategoryType.SNACK
    }

    @Test
    fun return_main_when_categoryToDomainModel_given_mainDataModel() {
        // Given
        val model = DbCategoryType.MAIN

        // When
        val dataModel = model.toDomainModel()

        // Then
        dataModel shouldBe CategoryType.MAIN
    }

    @Test
    fun return_side_when_categoryToDomainModel_given_sideDataModel() {
        // Given
        val model = DbCategoryType.SIDE

        // When
        val dataModel = model.toDomainModel()

        // Then
        dataModel shouldBe CategoryType.SIDE
    }

    @Test
    fun return_dessert_when_categoryToDomainModel_given_dessertDataModel() {
        // Given
        val model = DbCategoryType.DESSERT

        // When
        val dataModel = model.toDomainModel()

        // Then
        dataModel shouldBe CategoryType.DESSERT
    }

    @Test
    fun return_drink_when_categoryToDomainModel_given_drinkDataModel() {
        // Given
        val model = DbCategoryType.DRINK

        // When
        val dataModel = model.toDomainModel()

        // Then
        dataModel shouldBe CategoryType.DRINK
    }

    @Test
    fun return_pound_when_quantityToDomainModel_given_poundDataModel() {
        // Given
        val dataModel = DbQuantityType.POUND

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.POUND
    }

    @Test
    fun return_ounce_when_quantityToDomainModel_given_ounceDataModel() {
        // Given
        val dataModel = DbQuantityType.OUNCE

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.OUNCE
    }

    @Test
    fun return_gram_when_quantityToDomainModel_given_gramDataModel() {
        // Given
        val dataModel = DbQuantityType.GRAM

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.GRAM
    }

    @Test
    fun return_kilogram_when_quantityToDomainModel_given_kilogramDataModel() {
        // Given
        val dataModel = DbQuantityType.KILOGRAM

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.KILOGRAM
    }

    @Test
    fun return_teaspoon_when_quantityToDomainModel_given_teaspoonDataModel() {
        // Given
        val dataModel = DbQuantityType.TEASPOON

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.TEASPOON
    }

    @Test
    fun return_tablespoon_when_quantityToDomainModel_given_tablespoonDataModel() {
        // Given
        val dataModel = DbQuantityType.TABLESPOON

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.TABLESPOON
    }

    @Test
    fun return_FluidOunce_when_quantityToDomainModel_given_FluidOunceDataModel() {
        // Given
        val dataModel = DbQuantityType.FLUID_OUNCE

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.FLUID_OUNCE
    }

    @Test
    fun return_gill_when_quantityToDomainModel_given_gillDataModel() {
        // Given
        val dataModel = DbQuantityType.GILL

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.GILL
    }

    @Test
    fun return_cup_when_quantityToDomainModel_given_cupDataModel() {
        // Given
        val dataModel = DbQuantityType.CUP

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.CUP
    }

    @Test
    fun return_pint_when_quantityToDomainModel_given_pintDataModel() {
        // Given
        val dataModel = DbQuantityType.PINT

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.PINT
    }

    @Test
    fun return_quart_when_quantityToDomainModel_given_quartDataModel() {
        // Given
        val dataModel = DbQuantityType.QUART

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.QUART
    }

    @Test
    fun return_gallon_when_quantityToDomainModel_given_gallonDataModel() {
        // Given
        val dataModel = DbQuantityType.GALLON

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.GALLON
    }

    @Test
    fun return_liter_when_quantityToDomainModel_given_literDataModel() {
        // Given
        val dataModel = DbQuantityType.LITER

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.LITER
    }

    @Test
    fun return_deciliter_when_quantityToDomainModel_given_deciliterDataModel() {
        // Given
        val dataModel = DbQuantityType.DECILITER

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.DECILITER
    }

    @Test
    fun return_centiliter_when_quantityToDomainModel_given_centiliterDataModel() {
        // Given
        val dataModel = DbQuantityType.CENTILITER

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.CENTILITER
    }

    @Test
    fun return_milliliter_when_quantityToDomainModel_given_milliliterDataModel() {
        // Given
        val dataModel = DbQuantityType.MILLILITER

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe QuantityType.MILLILITER
    }

    @Test
    fun return_null_when_quantityToDomainModel_given_nullDataModel() {
        // Given
        val model: QuantityType? = null

        // When
        val dataModel = model.toDataModel()

        // Then
        dataModel shouldBe null
    }

    @Test
    fun return_celsius_when_temperatureToDomainModel_given_celsiusDataModel() {
        // Given
        val dataModel = DbTemperatureType.CELSIUS

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe Temperature.CELSIUS
    }

    @Test
    fun return_fahrenheit_when_temperatureToDomainModel_given_fahrenheitDataModel() {
        // Given
        val dataModel = DbTemperatureType.FAHRENHEIT

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe Temperature.FAHRENHEIT
    }

    @Test
    fun return_null_when_temperatureToDomainModel_given_nullDataModel() {
        // Given
        val dataModel: DbTemperatureType? = null

        // When
        val model = dataModel.toDomainModel()

        // Then
        model shouldBe null
    }
}

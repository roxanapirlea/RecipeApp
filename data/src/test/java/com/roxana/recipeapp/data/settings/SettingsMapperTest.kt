package com.roxana.recipeapp.data.settings

import com.roxana.recipeapp.data.Settings
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature
import io.kotest.matchers.shouldBe
import org.junit.Test

class SettingsMapperTest {

    @Test
    fun returnSettingsTemperatureCelsius_when_temperatureCelsius() {
        // Given
        val temperature = Temperature.CELSIUS

        // When
        val sTemperature = temperature.toProto()

        // Then
        sTemperature shouldBe Settings.Temperature.CELSIUS
    }

    @Test
    fun returnSettingsTemperatureFahrenheit_when_temperatureFahrenheit() {
        // Given
        val temperature = Temperature.FAHRENHEIT

        // When
        val sTemperature = temperature.toProto()

        // Then
        sTemperature shouldBe Settings.Temperature.FAHRENHEIT
    }

    @Test
    fun returnTemperatureCelsius_when_settingsTemperatureCelsius() {
        // Given
        val sTemperature = Settings.Temperature.CELSIUS

        // When
        val temperature = sTemperature.toDomainModel()

        // Then
        temperature shouldBe Temperature.CELSIUS
    }

    @Test
    fun returnTemperatureFahrenheit_when_settingsTemperatureFahrenheit() {
        // Given
        val sTemperature = Settings.Temperature.FAHRENHEIT

        // When
        val temperature = sTemperature.toDomainModel()

        // Then
        temperature shouldBe Temperature.FAHRENHEIT
    }

    @Test
    fun returnTemperatureCelsius_when_settingsTemperatureUnrecognized() {
        // Given
        val sTemperature = Settings.Temperature.UNRECOGNIZED

        // When
        val temperature = sTemperature.toDomainModel()

        // Then
        temperature shouldBe Temperature.CELSIUS
    }

    @Test
    fun returnTemperatureCelsius_when_settingsTemperatureNull() {
        // Given
        val sTemperature: Settings.Temperature? = null

        // When
        val temperature = sTemperature.toDomainModel()

        // Then
        temperature shouldBe Temperature.CELSIUS
    }

    @Test
    fun returnSettingsUnitPound_when_unitPound() {
        // Given
        val unit = QuantityType.POUND

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.POUND
    }

    @Test
    fun returnSettingsUnitOunce_when_unitOunce() {
        // Given
        val unit = QuantityType.OUNCE

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.OUNCE
    }

    @Test
    fun returnSettingsUnitGram_when_unitGram() {
        // Given
        val unit = QuantityType.GRAM

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.GRAM
    }

    @Test
    fun returnSettingsUnitKilogram_when_unitKilogram() {
        // Given
        val unit = QuantityType.KILOGRAM

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.KILOGRAM
    }

    @Test
    fun returnSettingsUnitTeaspoon_when_unitTeaspoon() {
        // Given
        val unit = QuantityType.TEASPOON

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.TEASPOON
    }

    @Test
    fun returnSettingsUnitTablespoon_when_unitTablespoon() {
        // Given
        val unit = QuantityType.TABLESPOON

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.TABLESPOON
    }

    @Test
    fun returnSettingsUnitFluidOunce_when_unitFluidOunce() {
        // Given
        val unit = QuantityType.FLUID_OUNCE

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.FLUID_OUNCE
    }

    @Test
    fun returnSettingsUnitGill_when_unitGill() {
        // Given
        val unit = QuantityType.GILL

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.GILL
    }

    @Test
    fun returnSettingsUnitCup_when_unitCup() {
        // Given
        val unit = QuantityType.CUP

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.CUP
    }

    @Test
    fun returnSettingsUnitPint_when_unitPint() {
        // Given
        val unit = QuantityType.PINT

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.PINT
    }
    @Test
    fun returnSettingsUnitQuart_when_unitQuart() {
        // Given
        val unit = QuantityType.QUART

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.QUART
    }
    @Test
    fun returnSettingsUnitGallon_when_unitGallon() {
        // Given
        val unit = QuantityType.GALLON

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.GALLON
    }

    @Test
    fun returnSettingsUnitLiter_when_unitLiter() {
        // Given
        val unit = QuantityType.LITER

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.LITER
    }
    @Test
    fun returnSettingsUnitDeciliter_when_unitDeciliter() {
        // Given
        val unit = QuantityType.DECILITER

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.DECILITER
    }

    @Test
    fun returnSettingsUnitCentiliter_when_unitCentiliter() {
        // Given
        val unit = QuantityType.CENTILITER

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.CENTILITER
    }
    @Test
    fun returnSettingsUnitMilliliter_when_unitMilliliter() {
        // Given
        val unit = QuantityType.MILLILITER

        // When
        val sUnit = unit.toProto()

        // Then
        sUnit shouldBe Settings.MeasureUnit.MILLILITER
    }

    @Test
    fun returnUnitPound_when_settingsUnitPound() {
        // Given
        val sUnit = Settings.MeasureUnit.POUND

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.POUND
    }
    @Test
    fun returnUnitOunce_when_settingsUnitOunce() {
        // Given
        val sUnit = Settings.MeasureUnit.OUNCE

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.OUNCE
    }

    @Test
    fun returnUnitGram_when_settingsUnitGram() {
        // Given
        val sUnit = Settings.MeasureUnit.GRAM

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.GRAM
    }

    @Test
    fun returnUnitKilogram_when_settingsUnitKilogram() {
        // Given
        val sUnit = Settings.MeasureUnit.KILOGRAM

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.KILOGRAM
    }
    @Test
    fun returnUnitTeaspoon_when_settingsUnitTeaspoon() {
        // Given
        val sUnit = Settings.MeasureUnit.TEASPOON

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.TEASPOON
    }
    @Test
    fun returnUnitTablespoon_when_settingsUnitTablespoon() {
        // Given
        val sUnit = Settings.MeasureUnit.TABLESPOON

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.TABLESPOON
    }
    @Test
    fun returnUnitFluidOunce_when_settingsUnitFluidOunce() {
        // Given
        val sUnit = Settings.MeasureUnit.FLUID_OUNCE

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.FLUID_OUNCE
    }

    @Test
    fun returnUnitGill_when_settingsUnitGill() {
        // Given
        val sUnit = Settings.MeasureUnit.GILL

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.GILL
    }

    @Test
    fun returnUnitCup_when_settingsUnitCup() {
        // Given
        val sUnit = Settings.MeasureUnit.CUP

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.CUP
    }

    @Test
    fun returnUnitPint_when_settingsUnitPint() {
        // Given
        val sUnit = Settings.MeasureUnit.PINT

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.PINT
    }

    @Test
    fun returnUnitQuart_when_settingsUnitQuart() {
        // Given
        val sUnit = Settings.MeasureUnit.QUART

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.QUART
    }

    @Test
    fun returnUnitGallon_when_settingsUnitGallon() {
        // Given
        val sUnit = Settings.MeasureUnit.GALLON

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.GALLON
    }

    @Test
    fun returnUnitLiter_when_settingsUnitLiter() {
        // Given
        val sUnit = Settings.MeasureUnit.LITER

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.LITER
    }

    @Test
    fun returnUnitDeciliter_when_settingsUnitDeciliter() {
        // Given
        val sUnit = Settings.MeasureUnit.DECILITER

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.DECILITER
    }

    @Test
    fun returnUnitCentiliter_when_settingsUnitCentiliter() {
        // Given
        val sUnit = Settings.MeasureUnit.CENTILITER

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.CENTILITER
    }

    @Test
    fun returnUnitMilliliter_when_settingsUnitMilliliter() {
        // Given
        val sUnit = Settings.MeasureUnit.MILLILITER

        // When
        val unit = sUnit.toDomainModel()

        // Then
        unit shouldBe QuantityType.MILLILITER
    }
}

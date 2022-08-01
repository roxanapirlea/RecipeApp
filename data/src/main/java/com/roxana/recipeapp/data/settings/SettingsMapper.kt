package com.roxana.recipeapp.data.settings

import com.roxana.recipeapp.data.Settings
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature

fun Settings.Temperature?.toDomainModel(): Temperature = when (this) {
    Settings.Temperature.FAHRENHEIT -> Temperature.FAHRENHEIT
    Settings.Temperature.CELSIUS -> Temperature.CELSIUS
    Settings.Temperature.UNRECOGNIZED -> Temperature.CELSIUS
    null -> Temperature.CELSIUS
}

fun Settings.MeasureUnit?.toDomainModel(): QuantityType? = when (this) {
    Settings.MeasureUnit.POUND -> QuantityType.POUND
    Settings.MeasureUnit.OUNCE -> QuantityType.OUNCE
    Settings.MeasureUnit.GRAM -> QuantityType.GRAM
    Settings.MeasureUnit.KILOGRAM -> QuantityType.KILOGRAM
    Settings.MeasureUnit.TEASPOON -> QuantityType.TEASPOON
    Settings.MeasureUnit.TABLESPOON -> QuantityType.TABLESPOON
    Settings.MeasureUnit.FLUID_OUNCE -> QuantityType.FLUID_OUNCE
    Settings.MeasureUnit.GILL -> QuantityType.GILL
    Settings.MeasureUnit.CUP -> QuantityType.CUP
    Settings.MeasureUnit.PINT -> QuantityType.PINT
    Settings.MeasureUnit.QUART -> QuantityType.QUART
    Settings.MeasureUnit.GALLON -> QuantityType.GALLON
    Settings.MeasureUnit.LITER -> QuantityType.LITER
    Settings.MeasureUnit.DECILITER -> QuantityType.DECILITER
    Settings.MeasureUnit.CENTILITER -> QuantityType.CENTILITER
    Settings.MeasureUnit.MILLILITER -> QuantityType.MILLILITER
    Settings.MeasureUnit.UNRECOGNIZED -> null
    null -> null
}

fun Temperature.toProto(): Settings.Temperature = when (this) {
    Temperature.FAHRENHEIT -> Settings.Temperature.FAHRENHEIT
    Temperature.CELSIUS -> Settings.Temperature.CELSIUS
}

fun QuantityType.toProto(): Settings.MeasureUnit = when (this) {
    QuantityType.POUND -> Settings.MeasureUnit.POUND
    QuantityType.OUNCE -> Settings.MeasureUnit.OUNCE
    QuantityType.GRAM -> Settings.MeasureUnit.GRAM
    QuantityType.KILOGRAM -> Settings.MeasureUnit.KILOGRAM
    QuantityType.TEASPOON -> Settings.MeasureUnit.TEASPOON
    QuantityType.TABLESPOON -> Settings.MeasureUnit.TABLESPOON
    QuantityType.FLUID_OUNCE -> Settings.MeasureUnit.FLUID_OUNCE
    QuantityType.GILL -> Settings.MeasureUnit.GILL
    QuantityType.CUP -> Settings.MeasureUnit.CUP
    QuantityType.PINT -> Settings.MeasureUnit.PINT
    QuantityType.QUART -> Settings.MeasureUnit.QUART
    QuantityType.GALLON -> Settings.MeasureUnit.GALLON
    QuantityType.LITER -> Settings.MeasureUnit.LITER
    QuantityType.DECILITER -> Settings.MeasureUnit.DECILITER
    QuantityType.CENTILITER -> Settings.MeasureUnit.CENTILITER
    QuantityType.MILLILITER -> Settings.MeasureUnit.MILLILITER
}

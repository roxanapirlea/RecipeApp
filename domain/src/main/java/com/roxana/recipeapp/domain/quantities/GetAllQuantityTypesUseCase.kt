package com.roxana.recipeapp.domain.quantities

import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.QuantityType
import javax.inject.Inject

class GetAllQuantityTypesUseCase @Inject constructor() : BaseSuspendableUseCase<
    Any?,
    List<QuantityType>
    >() {

    override suspend fun execute(input: Any?): List<QuantityType> {
        return listOf(
            QuantityType.GRAM,
            QuantityType.KILOGRAM,
            QuantityType.LITER,
            QuantityType.DECILITER,
            QuantityType.CENTILITER,
            QuantityType.MILLILITER,
            QuantityType.CUP,
            QuantityType.TEASPOON,
            QuantityType.TABLESPOON,
            QuantityType.POUND,
            QuantityType.OUNCE,
            QuantityType.FLUID_OUNCE,
            QuantityType.GILL,
            QuantityType.PINT,
            QuantityType.QUART,
            QuantityType.GALLON
        )
    }
}

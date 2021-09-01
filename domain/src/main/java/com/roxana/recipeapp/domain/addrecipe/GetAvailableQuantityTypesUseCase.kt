package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.QuantityType
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import javax.inject.Inject

class GetAvailableQuantityTypesUseCase @Inject constructor() : BaseSuspendableUseCase<
    Any?,
    List<QuantityType>
    >() {

    override suspend fun execute(input: Any?): List<QuantityType> {
        return listOf(
            QuantityType.GRAM,
            QuantityType.KILOGRAM,
            QuantityType.TEASPOON,
            QuantityType.TABLESPOON,
            QuantityType.CUP,
            QuantityType.LITER,
            QuantityType.DECILITER,
            QuantityType.CENTILITER
        )
    }
}

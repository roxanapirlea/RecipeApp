package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.QuantityType
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

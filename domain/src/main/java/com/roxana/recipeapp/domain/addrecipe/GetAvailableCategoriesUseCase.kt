package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.CategoryType
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import javax.inject.Inject

class GetAvailableCategoriesUseCase @Inject constructor() : BaseSuspendableUseCase<
    Any?,
    List<CategoryType>
    >() {

    override suspend fun execute(input: Any?): List<CategoryType> {
        return listOf(
            CategoryType.BREAKFAST,
            CategoryType.LUNCH,
            CategoryType.DINNER,
            CategoryType.SNACK,
            CategoryType.MAIN,
            CategoryType.SIDE,
            CategoryType.DESSERT,
            CategoryType.DRINK
        )
    }
}

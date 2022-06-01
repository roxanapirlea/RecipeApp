package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.CategoryType
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

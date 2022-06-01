package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.CreationComment
import javax.inject.Inject

class SetCommentsUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<List<CreationComment>, Unit>() {
    override suspend fun execute(input: List<CreationComment>) {
        creationRepository.setComments(input)
    }
}

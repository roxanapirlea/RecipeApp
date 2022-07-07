package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import javax.inject.Inject

class SetPhotoUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<String, Unit>() {
    override suspend fun execute(input: String) {
        creationRepository.setPhotoPath(input)
    }
}

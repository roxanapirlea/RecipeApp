package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.PhotoRepository
import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import javax.inject.Inject

class ClearPhotoUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository,
    private val photoRepository: PhotoRepository
) : BaseSuspendableUseCase<String, Unit>() {
    override suspend fun execute(input: String) {
        photoRepository.deleteFile(input)
        creationRepository.setPhotoPath(null)
    }
}
package com.roxana.recipeapp.domain.comment

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.base.CommonDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val dispatchers: CommonDispatchers
) : BaseSuspendableUseCase<AddCommentUseCase.Input, Unit>() {

    override suspend fun execute(input: Input) {
        withContext(dispatchers.io) {
            recipeRepository.addComment(input.recipeId, input.comment)
        }
    }

    data class Input(val recipeId: Int, val comment: String)
}

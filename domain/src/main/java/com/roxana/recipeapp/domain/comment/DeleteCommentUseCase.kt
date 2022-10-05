package com.roxana.recipeapp.domain.comment

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.base.CommonDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val dispatchers: CommonDispatchers
) : BaseSuspendableUseCase<DeleteCommentUseCase.Input, Unit>() {
    override suspend fun execute(input: Input) {
        withContext(dispatchers.io) {
            recipeRepository.deleteComment(input.recipeId, input.commentId)
        }
    }

    data class Input(val recipeId: Int, val commentId: Int)
}

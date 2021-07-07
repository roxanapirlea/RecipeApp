package com.roxana.recipeapp.domain.base

abstract class BaseSuspendableUseCase<Input, Output> {
    suspend operator fun invoke(input: Input): Result<Output> {
        return try {
            val output = execute(input)
            Result.success(output)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    abstract suspend fun execute(input: Input): Output
}

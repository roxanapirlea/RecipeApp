package com.roxana.recipeapp.domain

interface PhotoRepository {
    suspend fun copyTempFileToPermFile(tempPath: String): String?
    suspend fun deleteFile(path: String)
}

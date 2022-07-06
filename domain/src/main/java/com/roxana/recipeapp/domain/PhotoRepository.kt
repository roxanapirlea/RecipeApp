package com.roxana.recipeapp.domain

interface PhotoRepository {
    suspend fun copyTempFileToPermFile(tempPath: String, permFileName: String): String?
    suspend fun deleteFile(path: String)
}
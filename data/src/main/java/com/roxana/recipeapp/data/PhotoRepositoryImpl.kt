package com.roxana.recipeapp.data

import android.content.Context
import com.roxana.recipeapp.domain.PhotoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

private const val PHOTOS_PATH = "photo"

class PhotoRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : PhotoRepository {

    private val absoluteDirPath = "${context.filesDir}/$PHOTOS_PATH"

    override suspend fun copyTempFileToPermFile(tempPath: String, permFileName: String): String? =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val tempFile = File(tempPath)
                val newFile = createFile(absoluteDirPath, permFileName) ?: return@runCatching null
                tempFile.copyTo(newFile, true)
                tempFile.delete()
                newFile.absolutePath
            }.getOrNull()
        }

    private suspend fun createFile(path: String, name: String): File? =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val dir = File(path)
                if (!dir.exists())
                    dir.mkdirs()
                File(dir, name)
            }.getOrNull()
        }

    override suspend fun deleteFile(path: String) {
        withContext(Dispatchers.IO) {
            val file = File(path)
            file.delete()
        }
    }
}











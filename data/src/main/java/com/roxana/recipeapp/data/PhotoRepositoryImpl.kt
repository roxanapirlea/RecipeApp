package com.roxana.recipeapp.data

import android.content.Context
import com.roxana.recipeapp.domain.PhotoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import javax.inject.Inject

private const val PHOTOS_PATH = "photo"
private const val PHOTO_PREFIX = "photo_"

class PhotoRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : PhotoRepository {

    private val absoluteDirPath = "${context.filesDir}/$PHOTOS_PATH"

    override suspend fun copyTempFileToPermFile(tempPath: String): String? =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val tempFile = File(tempPath)
                val newFile = createFile(absoluteDirPath, getFileName()) ?: return@runCatching null
                tempFile.copyTo(newFile, true)
                tempFile.delete()
                newFile.absolutePath
            }.getOrNull()
        }

    private fun getFileName(): String = "$PHOTO_PREFIX${Instant.now().epochSecond}"

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











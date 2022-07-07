package com.roxana.recipeapp.scan

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val JPG_EXTENSION = "jpg"
private const val TEMP_FILE_NAME = "temp_image"

fun buildPreviewUseCase() = Preview.Builder().build()

fun buildCaptureUseCase() = ImageCapture.Builder()
    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
    .build()

suspend fun ImageCapture.takePicture(executor: Executor): File? {
    val photoFile = createTempFile() ?: return null

    return suspendCoroutine { continuation ->
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        takePicture(
            outputOptions, executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    continuation.resume(photoFile)
                }
                override fun onError(ex: ImageCaptureException) {
                    continuation.resumeWithException(ex)
                }
            }
        )
    }
}

private suspend fun createTempFile(): File? =
    withContext(Dispatchers.IO) {
        kotlin.runCatching {
            File.createTempFile(TEMP_FILE_NAME, JPG_EXTENSION)
        }.getOrNull()
    }

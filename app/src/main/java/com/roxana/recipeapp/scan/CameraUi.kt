package com.roxana.recipeapp.scan

import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.roxana.recipeapp.R
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    onImageFile: (File) -> Unit = {},
    onConfigError: (Throwable) -> Unit,
    onImageError: (Throwable) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewUseCase by remember { mutableStateOf<UseCase>(buildPreviewUseCase()) }
    val imageCaptureUseCase by remember { mutableStateOf(buildCaptureUseCase()) }

    CameraCaptureView(
        modifier = modifier,
        imageCaptureUseCase,
        { previewUseCase = it },
        onImageFile,
        onImageError
    )

    LaunchedEffect(previewUseCase) {
        configureCameraProvider(
            context = context,
            lifecycleOwner = lifecycleOwner,
            previewUseCase = previewUseCase,
            imageCaptureUseCase = imageCaptureUseCase,
            onError = onConfigError
        )
    }
}

@Composable
fun CameraCaptureView(
    modifier: Modifier = Modifier,
    imageCapture: ImageCapture,
    onPreviewUseCase: (UseCase) -> Unit,
    onImageFile: (File) -> Unit,
    onImageError: (Throwable) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(modifier = modifier) {
        CameraCapturePreview(
            modifier = Modifier.fillMaxSize(),
            onCameraUseCase = onPreviewUseCase
        )
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                coroutineScope.launch {
                    try {
                        onImageFile(imageCapture.takePicture(context.executor)!!)
                    } catch (e: Throwable) {
                        onImageError(e)
                    }
                }
            }
        ) {
            Icon(
                painterResource(R.drawable.ic_photo),
                contentDescription = stringResource(R.string.all_take_photo)
            )
        }
    }
}

@Composable
fun CameraCapturePreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    onCameraUseCase: (UseCase) -> Unit = { }
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            onCameraUseCase(
                Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
            )
            previewView
        }
    )
}

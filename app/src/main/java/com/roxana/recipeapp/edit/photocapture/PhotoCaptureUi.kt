package com.roxana.recipeapp.edit.photocapture

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.roxana.recipeapp.R
import com.roxana.recipeapp.common.utilities.rememberFlowWithLifecycle
import com.roxana.recipeapp.permission.ui.CameraPermissionDenied
import com.roxana.recipeapp.permission.ui.CameraPermissionExplanation
import com.roxana.recipeapp.scan.CameraView
import com.roxana.recipeapp.ui.basecomponents.AppBarBack

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PhotoCaptureDestination(
    photoCaptureViewModel: PhotoCaptureViewModel,
    onBack: () -> Unit = {}
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val state by rememberFlowWithLifecycle(photoCaptureViewModel.state)
        .collectAsState(PhotoCaptureViewState())

    val snackbarHostState = remember { SnackbarHostState() }
    val localContext = LocalContext.current.applicationContext

    state.error?.let {
        LaunchedEffect(state.error) {
            val message = when (it) {
                Error.CONFIG -> localContext.getString(R.string.all_camera_config_error)
                Error.CAPTURE -> localContext.getString(R.string.all_camera_capture_error)
            }
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            onBack()
            photoCaptureViewModel.onErrorDismissed()
        }
    }
    if (state.shouldNavigateBack) {
        LaunchedEffect(state.shouldNavigateBack) {
            onBack()
            photoCaptureViewModel.onNavDone()
        }
    }

    Scaffold(
        topBar = { AppBarBack(title = stringResource(R.string.all_take_photo), onIconClick = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                cameraPermissionState.status.isGranted ->
                    PhotoCaptureView(
                        photoPath = state.photoPath,
                        onImageTaken = photoCaptureViewModel::onPhotoTaken,
                        onAcceptPhoto = photoCaptureViewModel::onPhotoAdded,
                        onTakeAnotherPhoto = photoCaptureViewModel::onRetakePhoto,
                        onConfigError = photoCaptureViewModel::onConfigError,
                        onImageError = photoCaptureViewModel::onImageError,
                    )
                cameraPermissionState.status.shouldShowRationale ->
                    CameraPermissionExplanation(
                        text = stringResource(R.string.edit_recipe_add_photo_permission),
                        onRequest = { cameraPermissionState.launchPermissionRequest() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                else ->
                    CameraPermissionDenied(
                        onRequest = { cameraPermissionState.launchPermissionRequest() },
                        modifier = Modifier.align(Alignment.Center)
                    )
            }
        }
    }
}

@Composable
fun PhotoCaptureView(
    photoPath: String?,
    modifier: Modifier = Modifier,
    onImageTaken: (String) -> Unit = {},
    onAcceptPhoto: () -> Unit = {},
    onTakeAnotherPhoto: () -> Unit = {},
    onConfigError: () -> Unit = {},
    onImageError: () -> Unit = {},
) {
    if (photoPath != null) {
        CapturedImage(
            photoPath = photoPath,
            modifier = modifier,
            onTakeAnotherPhoto = onTakeAnotherPhoto,
            onAcceptPhoto = onAcceptPhoto
        )
    } else {
        CameraView(
            modifier = modifier,
            onImageFile = { file -> onImageTaken(file.toUri().path!!) },
            onConfigError = {
                onConfigError()
            },
            onImageError = {
                onImageError()
            }
        )
    }
}

@Composable
fun CapturedImage(
    photoPath: String,
    modifier: Modifier = Modifier,
    onTakeAnotherPhoto: () -> Unit = {},
    onAcceptPhoto: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            painter = rememberAsyncImagePainter(Uri.parse(photoPath)),
            contentDescription = stringResource(R.string.all_captured_image)
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(onClick = onTakeAnotherPhoto) {
                Text(stringResource(R.string.edit_recipe_photo_retake))
            }
            FilledTonalButton(onClick = onAcceptPhoto) {
                Text(stringResource(R.string.edit_recipe_photo_save))
            }
        }
    }
}

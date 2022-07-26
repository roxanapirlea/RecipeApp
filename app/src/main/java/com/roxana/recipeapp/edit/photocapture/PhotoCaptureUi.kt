package com.roxana.recipeapp.edit.photocapture

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.roxana.recipeapp.ui.AppBar
import com.roxana.recipeapp.ui.button.SecondaryButton

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoCaptureDestination(
    photoCaptureViewModel: PhotoCaptureViewModel,
    onBack: () -> Unit = {}
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val state by rememberFlowWithLifecycle(photoCaptureViewModel.state)
        .collectAsState(PhotoCaptureViewState())

    val scaffoldState = rememberScaffoldState()
    val localContext = LocalContext.current.applicationContext

    if (state.isConfigError) {
        LaunchedEffect(state.isConfigError) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = localContext.getString(R.string.all_camera_config_error),
                duration = SnackbarDuration.Short
            )
            onBack()
            photoCaptureViewModel.onConfigErrorDismissed()
        }
    }
    if (state.shouldNavigateBack) {
        LaunchedEffect(state.shouldNavigateBack) {
            onBack()
            photoCaptureViewModel.onNavDone()
        }
    }

    Scaffold(
        topBar = { AppBar(title = stringResource(R.string.home_title), onIconClick = onBack) },
        scaffoldState = scaffoldState
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
                        onConfigError = photoCaptureViewModel::onConfigError
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
            onConfigError = { onConfigError() }
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
            SecondaryButton(onClick = onTakeAnotherPhoto) {
                Text(stringResource(R.string.edit_recipe_photo_retake))
            }
            SecondaryButton(onClick = onAcceptPhoto) {
                Text(stringResource(R.string.edit_recipe_photo_save))
            }
        }
    }
}

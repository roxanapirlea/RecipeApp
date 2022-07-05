package com.roxana.recipeapp.edit.photocapture

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.roxana.recipeapp.R
import com.roxana.recipeapp.permission.ui.CameraPermissionDenied
import com.roxana.recipeapp.permission.ui.CameraPermissionExplanation
import com.roxana.recipeapp.ui.AppBar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoCaptureDestination(
    onBack: () -> Unit = {}
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val scaffoldState = rememberScaffoldState()

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
                    TODO()
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

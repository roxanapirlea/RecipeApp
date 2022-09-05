package com.roxana.recipeapp.edit.choosephoto.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.choosephoto.EditRecipeChoosePhotoViewState
import com.roxana.recipeapp.ui.RecipeImage
import com.roxana.recipeapp.ui.basecomponents.Detail
import com.roxana.recipeapp.ui.basecomponents.Label

@Composable
fun EditRecipeChoosePhotoView(
    state: EditRecipeChoosePhotoViewState,
    onCapturePhoto: () -> Unit = {},
    onRecapturePhoto: () -> Unit = {},
    onClearPhoto: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(3f))
        Label(text = stringResource(R.string.edit_recipe_photo_label))
        Spacer(modifier = Modifier.weight(0.5f))
        if (state.photoPath != null) {
            Box(modifier = Modifier.fillMaxWidth()) {
                RecipeImage(path = state.photoPath, Modifier.align(Alignment.Center))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                FilledTonalButton(
                    onClick = onClearPhoto,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(stringResource(R.string.all_delete))
                }
                FilledTonalButton(onClick = onRecapturePhoto) {
                    Text(stringResource(R.string.edit_recipe_photo_retake))
                }
            }
        } else {
            Detail(text = stringResource(R.string.edit_recipe_no_photo))
            Spacer(modifier = Modifier.weight(1f))
            FilledTonalButton(onClick = onCapturePhoto) {
                Text(stringResource(R.string.all_take_photo))
            }
        }
        Spacer(modifier = Modifier.weight(6f))
    }
}

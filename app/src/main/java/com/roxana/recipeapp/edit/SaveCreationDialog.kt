package com.roxana.recipeapp.edit

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.roxana.recipeapp.R

@Composable
fun SaveCreationDialog(
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                onDismiss()
            },
            title = {
                Text(
                    stringResource(R.string.edit_recipe_dialog_title),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    stringResource(R.string.edit_recipe_dialog_message),
                    textAlign = TextAlign.Center
                )
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onDelete()
                    }
                ) { Text(stringResource(R.string.edit_recipe_dialog_reset_info)) }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onSave()
                    }
                ) { Text(stringResource(R.string.edit_recipe_dialog_keep_recipe)) }
            },
        )
    }
}

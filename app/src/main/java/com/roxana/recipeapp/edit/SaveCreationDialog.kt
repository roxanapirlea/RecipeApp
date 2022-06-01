package com.roxana.recipeapp.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.FlatSecondaryButton

@Composable
fun SaveCreationDialog(
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDiscard: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                onDiscard()
            },
            title = {
                Text(stringResource(R.string.edit_recipe_dialog_title))
            },
            text = {
                Column() {
                    Text(stringResource(R.string.edit_recipe_dialog_message))
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    FlatSecondaryButton(
                        onClick = {
                            openDialog.value = false
                            onDelete()
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(stringResource(R.string.edit_recipe_dialog_reset_info))
                    }
                    FlatSecondaryButton(
                        onClick = {
                            openDialog.value = false
                            onSave()
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(stringResource(R.string.edit_recipe_dialog_keep_recipe))
                    }
                }
            }
        )
    }
}

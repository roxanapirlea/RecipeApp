package com.roxana.recipeapp.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.FlatSecondaryButton

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
            buttons = {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    FlatSecondaryButton(
                        onClick = {
                            openDialog.value = false
                            onDelete()
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Text(
                            stringResource(R.string.edit_recipe_dialog_reset_info),
                            textAlign = TextAlign.Center
                        )
                    }
                    FlatSecondaryButton(
                        onClick = {
                            openDialog.value = false
                            onSave()
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Text(
                            stringResource(R.string.edit_recipe_dialog_keep_recipe),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }
}

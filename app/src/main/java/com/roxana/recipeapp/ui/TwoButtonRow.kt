package com.roxana.recipeapp.ui

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun TwoButtonRow(
    textStartButton: String,
    textEndButton: String,
    modifier: Modifier = Modifier,
    onClickStartButton: () -> Unit = {},
    onClickEndButton: () -> Unit = {}
) {
    Surface(
        elevation = 4.dp,
        color = MaterialTheme.colors.background,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(vertical = 8.dp)
        ) {
            FlatSecondaryButton(
                onClick = onClickStartButton,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(textStartButton, textAlign = TextAlign.Center)
            }
            FlatSecondaryButton(
                onClick = onClickEndButton,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(textEndButton, textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview
@Composable
fun TwoButtonRowPreview() {
    RecipeTheme {
        TwoButtonRow("Cancel", "Ok")
    }
}

@Preview
@Composable
fun TwoButtonRowLeftLongTextPreview() {
    RecipeTheme {
        TwoButtonRow("Cancelling and continuing", "Ok")
    }
}

@Preview
@Composable
fun TwoButtonRowRightLongTextPreview() {
    RecipeTheme {
        TwoButtonRow("Cancel", "Save and continuing")
    }
}

@file:OptIn(ExperimentalMaterial3Api::class)

package com.roxana.recipeapp.ui.basecomponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilterChipTertiary(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilterChip(
        onClick = onClick,
        selected = selected,
        enabled = enabled,
        modifier = modifier,
        label = label,
        colors = tertiaryFlatChipsColors(),
        leadingIcon = {
            if (selected) Icon(Icons.Rounded.Check, contentDescription = null)
        },
    )
}

@Composable
fun ElevatedFilterChipTertiary(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ElevatedFilterChip(
        onClick = onClick,
        selected = selected,
        enabled = enabled,
        modifier = modifier,
        label = label,
        colors = tertiaryElevatedChipsColors(),
        leadingIcon = {
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .size(24.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
            ) {
                if (selected)
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null
                    )
            }
        },
    )
}

@Composable
private fun tertiaryFlatChipsColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
    selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
    selectedLeadingIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
)

@Composable
private fun tertiaryElevatedChipsColors() = FilterChipDefaults.elevatedFilterChipColors(
    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
    selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
    selectedLeadingIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
)

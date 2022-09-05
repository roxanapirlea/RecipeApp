package com.roxana.recipeapp.ui.basecomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Preview
@Composable
private fun CatalogView() {
    RecipeTheme {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { AppBars() }
            item { Buttons() }
            item { Cards() }
            item { Checkables() }
            item { Chips() }
            item { Fabs() }
            item { IconButtons() }
            item { DropDownMenu() }
            item { Switches() }
            item { Texts() }
            item { ToggableButtons() }
        }
    }
}

@Composable
private fun AppBars() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AppBarBack("Title")
        AppBarClose("Title")
        AppBar("Title")
    }
}

@Composable
private fun Buttons() {
    Column {
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {}) {
                Text(text = "Filled")
            }
            Button(onClick = {}, enabled = false) {
            Text(text = "Disabled")
        }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledIconTextButton(
                onClick = {},
                text = { Text(text = "Filled") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
            FilledIconTextButton(
                onClick = {},
                enabled = false,
                text = { Text(text = "Disabled") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {}) {
                Text(text = "Outlined")
            }
            OutlinedButton(onClick = {}, enabled = false) {
            Text(text = "Disabled")
        }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedIconTextButton(
                onClick = {},
                text = { Text(text = "Outlined") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
            OutlinedIconTextButton(
                onClick = {},
                enabled = false,
                text = { Text(text = "Disabled") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = {}) {
                Text(text = "Text button")
            }
            TextButton(onClick = {}, enabled = false) {
            Text(text = "Disabled")
        }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButtonWithIcon(
                onClick = {},
                text = { Text(text = "Text button") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
            TextButtonWithIcon(
                onClick = {},
                enabled = false,
                text = { Text(text = "Disabled") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ElevatedButton(onClick = {}) {
                Text(text = "Elevated")
            }
            ElevatedButton(onClick = {}, enabled = false) {
            Text(text = "Disabled")
        }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ElevatedIconTextButton(
                onClick = {},
                text = { Text(text = "Elevated") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
            ElevatedIconTextButton(
                onClick = {},
                enabled = false,
                text = { Text(text = "Disabled") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(onClick = {}) {
                Text(text = "Tonal")
            }
            FilledTonalButton(onClick = {}, enabled = false) {
            Text(text = "Disabled")
        }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalIconTextButton(
                onClick = {},
                text = { Text(text = "Tonal") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
            FilledTonalIconTextButton(
                onClick = {},
                enabled = false,
                text = { Text(text = "Disabled") },
                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
        }
    }
}

@Composable
private fun Cards() {
    Column(Modifier.padding(16.dp)) {
        Text(text = "Cards")
        CardEndImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Title", modifier = Modifier)
        }
        CardEndImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Title", modifier = Modifier)
        }
        Text(text = "Elevated Cards")
        ElevatedCardEndImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Title", modifier = Modifier)
        }
        ElevatedCardEndImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Title", modifier = Modifier)
        }
        Text(text = "Outlined Cards")
        OutlineCardEndImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Title", modifier = Modifier)
        }
        OutlineCardEndImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Title", modifier = Modifier)
        }
    }
}

@Composable
private fun Checkables() {
    Column {
        CheckableText(
            checked = true,
            onCheckedChange = {}, text = { Text("Selected Enabled") }
        )
        CheckableText(
            checked = false,
            onCheckedChange = {}, text = { Text("Unselected Enabled") }
        )
        CheckableText(
            checked = true,
            enabled = false,
            onCheckedChange = {},
            text = { Text("Selected Disabled") }
        )
        CheckableText(
            checked = false,
            enabled = false,
            onCheckedChange = {},
            text = { Text("Unselected Disabled") }
        )
    }
}

@Composable
private fun Chips() {
    Column {
        FilterChipTertiary(
            onClick = {},
            selected = true,
            enabled = true,
            label = { Text("Filter Selected Enabled") }
        )
        FilterChipTertiary(
            onClick = {},
            selected = false,
            enabled = true,
            label = { Text("Filter Unselected Enabled") }
        )
        FilterChipTertiary(
            onClick = {},
            selected = true,
            enabled = false,
            label = { Text("Filter Selected Disabled") }
        )
        FilterChipTertiary(
            onClick = {},
            selected = false,
            enabled = false,
            label = { Text("Filter Unselected Disabled") }
        )
        ElevatedFilterChipTertiary(
            onClick = {},
            selected = true,
            enabled = true,
            label = { Text("Elevated Filter Selected Enabled") }
        )
        ElevatedFilterChipTertiary(
            onClick = {},
            selected = false,
            enabled = true,
            label = { Text("Elevated Filter Unselected Enabled") }
        )
        ElevatedFilterChipTertiary(
            onClick = {},
            selected = true,
            enabled = false,
            label = { Text("Elevated Filter Selected Disabled") }
        )
        ElevatedFilterChipTertiary(
            onClick = {},
            selected = false,
            enabled = false,
            label = { Text("Elevated Filter Unselected Disabled") }
        )
    }
}

@Composable
private fun Fabs() {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SmallFloatingActionButton(onClick = {}) {
            Icon(Icons.Rounded.Add, contentDescription = null)
        }
        FloatingActionButton(onClick = {}) {
            Icon(Icons.Rounded.Add, contentDescription = null)
        }
        LargeFloatingActionButton(onClick = {}) {
            Icon(Icons.Rounded.Add, contentDescription = null)
        }
        ExtendedTextIconFab(
            onClick = {},
            text = { Text(text = "Add") },
            leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
        )
    }
}

@Composable
private fun IconButtons() {
    Column {
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledIconButton(onClick = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
            FilledIconButton(onClick = {}, enabled = false) {
            Icon(Icons.Rounded.Call, contentDescription = null)
        }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedIconButton(onClick = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
            OutlinedIconButton(onClick = {}, enabled = false) {
            Icon(Icons.Rounded.Call, contentDescription = null)
        }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalIconButton(onClick = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
            FilledTonalIconButton(onClick = {}, enabled = false) {
            Icon(Icons.Rounded.Call, contentDescription = null)
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownMenu() {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Label") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun Switches() {
    Column {
        SwitchText(checked = true, onCheckedChange = {}, text = "Selected Enabled")
        SwitchText(checked = false, onCheckedChange = {}, text = "Unselected Enabled")
        SwitchText(
            checked = true,
            enabled = false,
            onCheckedChange = {},
            text = "Selected Disabled"
        )
        SwitchText(
            checked = false,
            enabled = false,
            onCheckedChange = {},
            text = "Unselected Disabled"
        )
    }
}

@Composable
private fun Texts() {
    Column {
        Label(text = "Label")
        Explanation(text = "Explanation")
    }
}

@Composable
private fun ToggableButtons() {
    Column {
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledIconToggleButton(checked = true, onCheckedChange = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
            FilledIconToggleButton(checked = false, onCheckedChange = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
            FilledIconToggleButton(checked = false, onCheckedChange = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedIconToggleButton(checked = true, onCheckedChange = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
            OutlinedIconToggleButton(checked = false, onCheckedChange = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
            OutlinedIconToggleButton(checked = false, onCheckedChange = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalIconToggleButton(checked = true, onCheckedChange = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
            FilledTonalIconToggleButton(checked = false, onCheckedChange = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
            FilledTonalIconToggleButton(checked = false, onCheckedChange = {}) {
                Icon(Icons.Rounded.Call, contentDescription = null)
            }
        }
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToggleTextButton("C", checked = true, onCheckedChange = {})
            ToggleTextButton("F", checked = false, onCheckedChange = {})
        }
    }
}

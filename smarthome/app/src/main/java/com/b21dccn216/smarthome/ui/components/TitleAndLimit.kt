package com.b21dccn216.smarthome.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TitleAndLimit(
    modifier: Modifier,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text("Chart",
            modifier = Modifier.weight(1f))

        DropDownPicker(
            onOptionSelected = {onOptionSelected(it)},
            options = listOf("10", "20", "50", "100", "1000"),
            selectedOption = selectedOption,
            modifier = Modifier.width(160.dp)
        )
    }
}
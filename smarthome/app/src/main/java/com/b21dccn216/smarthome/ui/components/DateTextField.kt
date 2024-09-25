package com.b21dccn216.smarthome.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(
    value: String,
    onDateSelected: (String) -> Unit, // Callback function to pass the selected date
    onDeselected: ()->Unit,
    modifier: Modifier
) {
    var tmp = value
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: tmp
    tmp = ""

    // Trigger the callback with the selected date
    LaunchedEffect(selectedDate) {
        if (selectedDate.isNotEmpty()) {
            onDateSelected(selectedDate)
        }
    }
    Row(
        modifier = modifier.clickable {
            showDatePicker = !showDatePicker
        },
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Date",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Visible
            ) },
            readOnly = true,
            shape = RoundedCornerShape(15),
            trailingIcon = {
                if(selectedDate.isNotEmpty()){
                    IconButton(onClick = {
                        onDeselected()
                        datePickerState.selectedDateMillis = null
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Unselect date"
                        )
                    }
                }else{
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                }

            },
            modifier = Modifier.clickable {
                showDatePicker = !showDatePicker
                Log.e("clickable", "true");
            }
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                    Button(onClick = { showDatePicker = false }) {
                        Text("Close")
                    }
                }
            }
        }
        }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

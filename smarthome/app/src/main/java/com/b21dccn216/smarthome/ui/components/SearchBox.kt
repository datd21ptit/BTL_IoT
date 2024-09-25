package com.b21dccn216.smarthome.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.b21dccn216.smarthome.model.uistate.TableUiState

@Composable
fun SearchBox(
    titleColumn: List<String>,
    uiState: TableUiState,
    onValueChange: (String, Int) -> Unit,
    onClickClose: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        titleColumn.filterIndexed{index,_ -> index < 2 }.forEachIndexed{ index, tit ->
            OutlinedTextField(
                label = {
                    Text(tit,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier.padding(0.dp)
                    ) },
                value = uiState.row[index],
                onValueChange = { it ->
                    onValueChange(it, index)
                },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    if(uiState.row[index] != ""){
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onClickClose(index)
                                onValueChange("", index)
                            }
                        )
                    }
                },
                shape = RoundedCornerShape(15),
                modifier = Modifier.weight(1f)
            )
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        titleColumn.filterIndexed{index,_ -> index >=2 }.forEachIndexed{ ind, tit ->
            val index = ind + 2
            OutlinedTextField(
                label = {
                    Text(tit,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier.padding(0.dp)
                    ) },
                value = uiState.row[index],
                onValueChange = { it ->
                    onValueChange(it, index)
                },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    if(uiState.row[index] != ""){
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onClickClose(index)
                                onValueChange("", index)
                            }
                        )
                    }
                },
                shape = RoundedCornerShape(15),
                modifier = Modifier.weight(1f)
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}


@Composable
fun DateAndLimit(
    selectedDate: String,
    selectedOption: String,
    onOptionSelected: (String)->Unit,
    onDateSelected: (String) -> Unit,
    onDeSelected : () ->Unit,
    time: String,
    onChangeTime: (String) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
        ,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ){
        SearchBoxTime(
            modifier = Modifier.weight(1f),
            time = time,
            onValueChange = {onChangeTime(it)},
        )
        DatePickerDocked(
            onDeselected = {onDeSelected()},
            onDateSelected = onDateSelected,
            value = selectedDate,
            modifier = Modifier.weight(1f),

        )
        DropDownPicker(
            options = listOf("12", "20", "50", "100"),
            selectedOption = selectedOption,
            onOptionSelected = {newValue ->
                onOptionSelected(newValue)
           },
            modifier = Modifier.weight(1f)
        )

    }
}



@Composable
fun SearchBoxTime(
    modifier: Modifier,
    time: String,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
            OutlinedTextField(
                label = {
                    Text( text ="Time",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Visible
                    ) },
                value = time,
                onValueChange = { it ->
                    onValueChange(it)
                },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    if(time != ""){
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onValueChange("")
                            }
                        )
                    }
                },
                shape = RoundedCornerShape(15),
                modifier = Modifier.weight(1f)
            )
    }
}

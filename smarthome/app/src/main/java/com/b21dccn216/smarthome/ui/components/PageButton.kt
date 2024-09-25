package com.b21dccn216.smarthome.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.b21dccn216.smarthome.network.dto.TableDTO


@Composable
fun PageButtons(
    modifier: Modifier,
    tableData: TableDTO,
    onPageSelected: (Int) -> Unit, //viewmodel.addFilter(uiState.copy(page = index.toString()))
){
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        (1..tableData.totalPages).forEach{ index ->
            item {
                PageButton(
                    onPageSelected = {onPageSelected(index)},
                    isPageDisplay = tableData.page == index,
                    page = index
                )
            }
        }
    }
}


@Composable
fun PageButton(
    onPageSelected: () -> Unit,
    isPageDisplay: Boolean,
    page: Int
){
    Button(
        onClick = { onPageSelected() },
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(10))
            .size(height = 40.dp, width = 50.dp),
        colors = ButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.Black),
        contentPadding = PaddingValues(),
        border = BorderStroke(
            width = 2.dp,
            color = if(isPageDisplay) Color.Black else Color.Transparent
        )
    ) {
        Text(text = page.toString())
    }
}

package com.b21dccn216.smarthome.ui.screen

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.b21dccn216.smarthome.R
import com.b21dccn216.smarthome.SmartHomeViewmodel
import com.b21dccn216.smarthome.model.SortOrder
import com.b21dccn216.smarthome.network.dto.TableDTO
import com.b21dccn216.smarthome.ui.components.DateAndLimit
import com.b21dccn216.smarthome.ui.components.PageButtons
import com.b21dccn216.smarthome.ui.components.SearchBox

//const val column1Weight = .4f
//const val column2Weight = .7f
//const val column3Weight = .3f

@Composable
fun TableScreen(
    viewmodel: SmartHomeViewmodel,
    titleColumn: List<String>,
    innerPadding: PaddingValues,
    tableData: TableDTO,
    countTitle: String,
) {
    val uiState by viewmodel.uiStateTable.collectAsState()
    Column(
        modifier = Modifier
            .padding(top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 8.dp, end = 8.dp
            ),
//        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val selectedDate by remember{ mutableStateOf(uiState.dateTime) }
//      Search box
        Column(modifier = Modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(15))
            .padding(2.dp)
            .background(color = Color.White, shape = RoundedCornerShape(15))
            .padding(12.dp)
        ){
            SearchBox(titleColumn = titleColumn, uiState = uiState,
                onClickClose = {
                },
                onValueChange = { it, index ->
                    val l = uiState.row.toMutableList()
                    l[index] = it
                    viewmodel.addFilter(uiState.copy(row = l))
                })
            DateAndLimit(
                selectedDate = selectedDate,
                selectedOption = uiState.limit,
                onOptionSelected = {it ->
                    viewmodel.addFilter(uiState.copy(limit = it))
                },
                onDateSelected = {it ->
                    viewmodel.addFilter(uiState.copy(dateTime = it))
                },
                onDeSelected = { viewmodel.addFilter(uiState.copy(dateTime = ""))},
                time = uiState.searchTime,
                onChangeTime = {viewmodel.addFilter(uiState.copy(searchTime = it))}
            )
        }
        if(tableData.count != null){
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(15))
                .padding(2.dp)
                .background(color = Color.White, shape = RoundedCornerShape(15))
                .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "${countTitle}: ${tableData.count}")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        val scrollState = rememberScrollState()
//        Table
        Column{
            Column (modifier = Modifier
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(5))
                .padding(2.dp)
                .background(color = Color.White, shape = RoundedCornerShape(5))
                .padding(8.dp)
                .horizontalScroll(scrollState)
                .weight(1f)
            ) {
                TitleRow(titleColumn,
                    onClickCell = { index ->
                        Log.e("viewmodel", "CLICKED")
                        viewmodel.changeOrder(index)
                    },
                    listSort = uiState.sort,
                    sortTime = uiState.sortTime)
                LazyColumn{
                    items(tableData.data){ TableRow(it = it)}
                }
            }
            PageButtons(
                tableData = tableData,
                onPageSelected = {viewmodel.addFilter(uiState.copy(page = it.toString()))},
                modifier = Modifier
            )
        }
    }
}



@Composable
fun TitleRow(
    titleColumn: List<String>,
    listSort: List<SortOrder>,
    sortTime: SortOrder,
    onClickCell: (Int) -> Unit
){
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(text = "ID",  width = 100.dp)
        titleColumn.forEachIndexed{ index, title ->
            ClickableTableCell(text = title, width = 100.dp, onClickCell = { onClickCell(index) }, sortOrder = listSort[index])
        }
        ClickableTableCell(text = "Time", width = 150.dp, onClickCell = { onClickCell(-1) }, sortOrder = sortTime)
    }
}

@Composable
fun TableRow(
    it: List<String>
){
    Row(Modifier) {
        it.forEachIndexed{ index, text ->
            if(index == 0 ){
                TableCell(text = text,  width = 100.dp)
            }else if(index == it.lastIndex){
                TableCell(text = text,  width = 150.dp)
            }else{
                TableCell(text = text, width = 100.dp)
            }
        }
    }
}

@Composable
fun RowScope.ClickableTableCell(
    modifier: Modifier = Modifier,
    text: String,
    width: Dp,
    onClickCell: ()->Unit,
    sortOrder: SortOrder
){
    Row(
        modifier
            .width(width)
            .padding(vertical = 4.dp)
            .clickable { onClickCell() }
        ,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Start
        )
        when(sortOrder){
            SortOrder.ASC -> {
                Icon(
                    painter = painterResource(id = R.drawable.sortd),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            SortOrder.DESC -> {
                Icon(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            SortOrder.NO_SORT -> {
            }
        }

    }
}

@Composable
fun RowScope.TableCell(
    modifier: Modifier = Modifier,
    text: String,
    width: Dp
) {
    Text(
        text = text,
        modifier
            .width(width)
            .padding(vertical = 4.dp)
        ,
        maxLines = 1,
        overflow = TextOverflow.Visible,
        textAlign = TextAlign.Start
    )
}

package com.b21dccn216.smarthome.model.uistate

import com.b21dccn216.smarthome.model.SortOrder
import com.b21dccn216.smarthome.network.dto.TableDTO


data class TableUiState(
    val limit: String = "20",
    val dateTime: String = "",
    val searchTime: String = "",
    val page: String = "1",
    val sortTime: SortOrder = SortOrder.DESC,

    val sort: List<SortOrder> = listOf(
        SortOrder.NO_SORT,
        SortOrder.NO_SORT,
        SortOrder.NO_SORT,
        SortOrder.NO_SORT
    ), //
    val row: List<String> = listOf("", "", "", ""),

    val tableSensorData: TableDTO = TableDTO(
        data = listOf(listOf("id", "row1", "row2", "row3","row4", "time")),
        page = 1,
        totalRows = 100,
        totalPages = 10,
    ),

    val tableActionData: TableDTO = TableDTO(
        data = listOf(listOf("id", "row1", "row2",  "time")),
        page = 1,
        totalRows = 100,
        totalPages = 10
    ),
)




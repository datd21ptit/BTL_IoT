package com.b21dccn216.smarthome.model

import com.b21dccn216.smarthome.network.dto.TableDTO


data class TableUiState(
    val id: String = "",
    val limit: String = "20",
    val time: String = "",
    val page: String = "1",
    val sort: List<SortOrder> = listOf(SortOrder.NO_SORT, SortOrder.NO_SORT, SortOrder.NO_SORT, SortOrder.NO_SORT), //
    val sortTime: SortOrder = SortOrder.DESC,
    val row: List<String> = listOf("", "", "", ""),
    val count: Int? = null,
    val timeSearch: String = "",
//
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




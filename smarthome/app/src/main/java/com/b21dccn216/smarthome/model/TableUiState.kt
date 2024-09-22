package com.b21dccn216.smarthome.model

data class TableUiState(
    val id: String = "",
    val limit: String = "20",
    val time: String = "",
    val page: String = "1",
    val sort: List<SortOrder> = listOf(SortOrder.NO_SORT, SortOrder.NO_SORT, SortOrder.NO_SORT), //
    val sortTime: SortOrder = SortOrder.NO_SORT,
    val row: List<String> = listOf("", "", ""),
//
    val tableSensorData:TableResponse = TableResponse(
        data = listOf(listOf("id", "row1", "row2", "row3", "time")),
        page = 1,
        totalRows = 100,
        totalPages = 10
    ),
    val tableActionData:TableResponse= TableResponse(
        data = listOf(listOf("id", "row1", "row2",  "time")),
        page = 1,
        totalRows = 100,
        totalPages = 10
    ),
)



data class TableResponse(
    val page: Int,
    val totalPages: Int,
    val totalRows: Int,
    val data: List<List<String>>
)
package com.b21dccn216.smarthome.network.dto


data class TableDTO(
    val page: Int,
    val totalPages: Int,
    val totalRows: Int,
    val data: List<List<String>>,
    val count: Int? = null,
)
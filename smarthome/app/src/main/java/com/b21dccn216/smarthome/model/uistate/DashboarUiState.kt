package com.b21dccn216.smarthome.model.uistate

import com.b21dccn216.smarthome.network.dto.DashboardDTO


data class DashboarUiState(
    val limitD: String = "50",
    val data: DashboardDTO = DashboardDTO(),
)





package com.b21dccn216.smarthome.network

import com.b21dccn216.smarthome.model.SortOrder
import com.b21dccn216.smarthome.network.dto.TableDTO
import com.b21dccn216.smarthome.model.TableUiState
import com.b21dccn216.smarthome.network.dto.DashboardDTO
import retrofit2.Response

class SmartHomeRepository(
    private val apiService: ApiService
) {
    suspend fun getDashboardData(limit: Int): DashboardDTO = apiService.getDashboardData(limit)

    suspend fun sendAction(device: String, state: String): Response<Void>{
        return apiService.sendAction(device, state)
    }

    suspend fun getSensorTableData(uiState: TableUiState): TableDTO
    {
        val sortOrderList = mutableListOf("{\"column\": \"time\", \"order\": \"${uiState.sortTime.value}\"}")
        uiState.sort.forEachIndexed{ index, sortOrder ->
            if(sortOrder != SortOrder.NO_SORT){
                val col = when(index){
                    0 ->  "temp"
                    1 ->  "humid"
                    2 ->  "light"
                    3 ->  "wind"
                    else -> ""
                }
                if(col.isNotEmpty()) sortOrderList.add("{\"column\": \"${col}\", \"order\": \"${sortOrder.value}\"}")
            }
        }
        return apiService.getSensorTable(
            page = uiState.page,
            limit = uiState.limit,
            temp = uiState.row[0],
            humid = uiState.row[1],
            light = uiState.row[2],
            wind = uiState.row[3],
            time = uiState.time,
            sort = sortOrderList,
            timeSearch = uiState.timeSearch
        )
    }

    suspend fun getActionTableData(uiState: TableUiState): TableDTO
    {
        val sortOrderList = mutableListOf<String>().apply {
            if(uiState.sortTime != SortOrder.NO_SORT){
                add("{\"column\": \"time\", \"order\": \"${uiState.sortTime.value}\"}")
            }
        }

        uiState.sort.forEachIndexed{index, sortOrder ->
            if(sortOrder != SortOrder.NO_SORT){
                val col = when(index){
                    0 ->  "device"
                    1 ->  "state"
                    else -> ""
                }
                if(col.isNotEmpty()) sortOrderList.add("{\"column\": \"${col}\", \"order\": \"${sortOrder.value}\"}")
            }
        }
        return apiService.getActionTable(
            page = uiState.page,
            limit = uiState.limit,
            device = uiState.row[0],
            state = uiState.row[1],
            time = uiState.time,
            sort = sortOrderList,
            timeSearch = uiState.timeSearch
        )
    }
}
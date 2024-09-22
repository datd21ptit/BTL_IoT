package com.b21dccn216.smarthome.network

import com.b21dccn216.smarthome.model.DashboarUiState
import com.b21dccn216.smarthome.model.SortOrder
import com.b21dccn216.smarthome.model.TableResponse
import com.b21dccn216.smarthome.model.TableUiState
import retrofit2.Response

class SmartHomeRepository(
    private val apiService: ApiService
) {
    suspend fun getDashboardUiData(limit: Int): DashboarUiState = apiService.getDashboardData(limit)

    suspend fun sendAction(device: String, state: String): Response<Void>{
        return apiService.sendAction(device, state)
    }

    suspend fun getSensorDataTable(
        uiState: TableUiState
    ): TableResponse {
//        "{\"column\": \"${it.first}\", \"order\": \"${it.second}\"}"
        val ls = uiState.sort
        val listSort = if(uiState.sortTime == SortOrder.NO_SORT) mutableListOf<String>() else mutableListOf("{\"column\": \"time\", \"order\": \"${uiState.sortTime.value}\"}")
        ls.forEachIndexed{index, it ->
            if(it != SortOrder.NO_SORT){
                var col = ""
                when(index){
                    0 -> col = "temp"
                    1 -> col = "humid"
                    2 -> col = "light"
                }
                if(col != "") listSort.add("{\"column\": \"${col}\", \"order\": \"${it.value}\"}")
            }
        }
        return apiService.getSensorTable(
            page = uiState.page,
            limit = uiState.limit,
            temp = uiState.row[0],
            humid = uiState.row[1],
            light = uiState.row[2],
            time = uiState.time,
            sort = listSort
        )
    }

    suspend fun getActionDataTable(
        uiState: TableUiState
    ): TableResponse {

        val listSort = if(uiState.sortTime == SortOrder.NO_SORT)mutableListOf<String>() else mutableListOf("{\"column\": \"time\", \"order\": \"${uiState.sortTime.value}\"}")
        val ls = uiState.sort
        ls.forEachIndexed{index, it ->
            if(it != SortOrder.NO_SORT){
                var col = ""
                when(index){
                    0 -> col = "device"
                    1 -> col = "state"
                }
                if(col != "") listSort.add("{\"column\": \"${col}\", \"order\": \"${it.value}\"}")
            }
        }
        return apiService.getActionTable(
            page = uiState.page,
            limit = uiState.limit,
            device = uiState.row[0],
            state = uiState.row[1],
            time = uiState.time,
            sort = listSort
        )
    }
}
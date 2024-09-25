package com.b21dccn216.smarthome.network

import com.b21dccn216.smarthome.model.DashboarUiState
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

    suspend fun getSensorTableData(
        uiState: TableUiState
    ): TableDTO {
        val ls = uiState.sort
        val listSort = mutableListOf("{\"column\": \"time\", \"order\": \"${uiState.sortTime.value}\"}")
        ls.forEachIndexed{index, it ->
            if(it != SortOrder.NO_SORT){
                var col = ""
                when(index){
                    0 -> col = "temp"
                    1 -> col = "humid"
                    2 -> col = "light"
                    3 -> col = "wind"
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
            sort = listSort,
            wind = uiState.row[3],
            timeSearch = uiState.timeSearch
        )
    }

    suspend fun getActionDataTable(
        uiState: TableUiState
    ): TableDTO {

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
            sort = listSort,
            timeSearch = uiState.timeSearch
        )
    }
}
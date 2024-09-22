package com.b21dccn216.smarthome


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.b21dccn216.smarthome.model.Destinations.DASHBOARD
import com.b21dccn216.smarthome.model.Destinations.SENSOR_DATA_TABLE
import com.b21dccn216.smarthome.network.SmartHomeRepository
import com.b21dccn216.smarthome.model.AppState.LOADED
import com.b21dccn216.smarthome.model.AppState.LOADING
import com.b21dccn216.smarthome.model.DashboarUiState
import com.b21dccn216.smarthome.model.Destinations.ACTION_DATA_TABLE
import com.b21dccn216.smarthome.model.SortOrder
import com.b21dccn216.smarthome.model.TableUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SmartHomeViewmodel(
    private val repository: SmartHomeRepository
) : ViewModel(){

    private var _uiStateDashboard = MutableStateFlow(DashboarUiState())
    val uiStateDashboard: StateFlow<DashboarUiState> = _uiStateDashboard.asStateFlow()

    private val _appState = MutableStateFlow(LOADING)
    val appState = _appState.asStateFlow()

    private val _uiStateTable = MutableStateFlow(TableUiState())
    val uiStateTable: StateFlow<TableUiState> = _uiStateTable.asStateFlow()

    private var _currentScreen = MutableStateFlow(DASHBOARD)
    val currentScreen = _currentScreen.asStateFlow()


    init {
        viewModelScope.launch {
            _currentScreen.collectLatest{ current ->
                while(true) {
                    when (current) {
                        DASHBOARD -> {
                            getDashboardData()
                        }
                        SENSOR_DATA_TABLE -> {
                            getTableData()
                        }
                        ACTION_DATA_TABLE -> {
                            getTableDataAction()
                        }
                    }
                    delay(1500)
                }
            }
        }
    }

    private suspend fun getTableData(){
        try{
            val result = repository.getSensorDataTable(_uiStateTable.value)
            _uiStateTable.update { value ->
                value.copy(
                    tableSensorData = result
                )
            }
            _appState.value = LOADED
            Log.d("viewmodel", "get sensor table")
        }catch (e: Exception){
            Log.e("viewmodel", e.toString())
        }
    }

    private suspend fun getTableDataAction(){
        try{
            val result = repository.getActionDataTable(_uiStateTable.value)
            _uiStateTable.update { value ->
                value.copy(
                    tableActionData = result
                )
            }
            _appState.value = LOADED
            Log.d("viewmodel", "get action table")
        }catch (e: Exception){
            Log.e("viewmodel", e.toString())
        }
    }

    private suspend fun getDashboardData(){
        try {
            val listResult = repository.getDashboardUiData(100)
            _uiStateDashboard.update {
                listResult
            }
            Log.d("viewmodel", listResult.toString())
//            getChartData()
            _appState.value = LOADED
        }catch (e: Exception){
            Log.e("viewmodel", e.toString())
        }
    }



    fun clickAction(device: String, state: String){
        viewModelScope.launch {
            try {
                val response = repository.sendAction(device, state)
                if(response.isSuccessful && response.code() == 200){
                    _uiStateDashboard.update {
                        when(device){
                            "led" -> it.copy(led = state).also { Log.e("viewmodel", "led") }
                            "fan" -> it.copy(fan = state).also { Log.e("viewmodel", "fam") }
                            "relay" -> it.copy(relay = state).also { Log.e("viewmodel", "relay") }
                            else -> {it}
                        }
                    }
                    Log.d("viewmodel", state)
                    Log.d("viewmodel", response.toString())
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun navigateTo(screen: Pair<Int, String>){
        _currentScreen.value = screen
        _uiStateTable.update { value ->
            value.copy(
                sort = listOf(SortOrder.NO_SORT, SortOrder.NO_SORT, SortOrder.NO_SORT),
                sortTime = SortOrder.NO_SORT,
                page = "1",
                row = listOf("", "", ""),
                time = "",
            )
        }
    }

    fun addFilter(state: TableUiState){
        _uiStateTable.update { value ->
            value.copy(
                page = state.page,
                row = state.row,
                time = state.time,
                limit = state.limit
            )
        }
    }

    fun ChangeOrder(index: Int){
        if (index == -1){
            _uiStateTable.update { it ->
                it.copy(sortTime = toggleSort(it.sortTime))
            }
        }else{
            val sort = _uiStateTable.value.sort.toMutableList()
            sort[index] = toggleSort(sort[index])
            _uiStateTable.update { it ->
                it.copy(sort = sort)
            }
        }
    }

    fun toggleSort(currentSortOrder: SortOrder): SortOrder {
        return when (currentSortOrder) {
            SortOrder.NO_SORT -> SortOrder.ASC
            SortOrder.ASC -> SortOrder.DESC
            SortOrder.DESC -> SortOrder.NO_SORT
        }
    }


}



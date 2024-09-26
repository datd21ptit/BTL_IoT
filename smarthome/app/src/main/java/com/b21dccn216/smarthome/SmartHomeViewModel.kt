package com.b21dccn216.smarthome


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.b21dccn216.smarthome.model.Destinations.DASHBOARD
import com.b21dccn216.smarthome.model.Destinations.SENSOR_DATA_TABLE
import com.b21dccn216.smarthome.network.SmartHomeRepository
import com.b21dccn216.smarthome.model.uistate.AppState.LOADED
import com.b21dccn216.smarthome.model.uistate.AppState.LOADING
import com.b21dccn216.smarthome.model.uistate.DashboarUiState
import com.b21dccn216.smarthome.model.Destinations.ACTION_DATA_TABLE
import com.b21dccn216.smarthome.model.SortOrder
import com.b21dccn216.smarthome.model.uistate.TableUiState
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
    private suspend fun getDashboardData(){
        try {
            val listResult = repository.getDashboardData(_uiStateDashboard.value.limitD.toInt())
            _uiStateDashboard.update {
                it.copy(data = listResult)
            }
            setAppStateLoaded()
            Log.d("viewmodel", listResult.toString())
        }catch (e: Exception){
            Log.e("viewmodel", e.toString())
        }
    }

    private suspend fun getTableData(){
        try{
            val result = repository.getSensorTableData(_uiStateTable.value)
            _uiStateTable.update { value ->
                value.copy(
                    tableSensorData = result,
                )
            }
            setAppStateLoaded()
            Log.d("viewmodel", "get sensor table")
        }catch (e: Exception){
            Log.e("viewmodel", e.toString())
        }
    }

    private suspend fun getTableDataAction(){
        try{
            val result = repository.getActionTableData(_uiStateTable.value)
            _uiStateTable.update { value ->
                value.copy(
                    tableActionData = result,
                )
            }
            setAppStateLoaded()
            Log.d("viewmodel", "get action table")
        }catch (e: Exception){
            Log.e("viewmodel", e.toString())
        }
    }

    fun changeLimit(limit: String){
        _uiStateDashboard.update { it->
            it.copy(limitD = limit)
        }
    }

    fun clickAction(device: String, state: String){
        viewModelScope.launch {
            try {
                val response = repository.sendAction(device, state)
                if(response.isSuccessful && response.code() == 200){
                    _uiStateDashboard.update {
                        when(device){
                            "led" -> it.copy(data = it.data.copy(led = state))
                            "fan" -> it.copy(data = it.data.copy(fan = state))
                            "relay" -> it.copy(data = it.data.copy(relay = state))
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
            TableUiState().copy(
                tableSensorData = value.tableSensorData,
                tableActionData = value.tableActionData,
            )
        }
    }

    fun addFilter(state: TableUiState){
        _uiStateTable.update { value ->
            value.copy(
                page = state.page,
                row = state.row,
                dateTime = state.dateTime,
                limit = state.limit,
                searchTime = state.searchTime
            )
        }
    }

    fun changeOrder(index: Int){
        _uiStateTable.update { currentState ->
            if (index == -1) {
                val newSortOrder = if(currentState.sortTime == SortOrder.DESC) SortOrder.ASC else SortOrder.DESC
                currentState.copy(sortTime = newSortOrder)
            } else {
                val newSort = currentState.sort.toMutableList()
                newSort[index] = toggleSort(newSort[index])
                currentState.copy(sort = newSort)
            }
        }
    }

    private fun toggleSort(currentSortOrder: SortOrder): SortOrder {
        return when (currentSortOrder) {
            SortOrder.NO_SORT -> SortOrder.ASC
            SortOrder.ASC -> SortOrder.DESC
            SortOrder.DESC -> SortOrder.NO_SORT
        }
    }

    private fun setAppStateLoaded() {
        _appState.value = LOADED
    }


}



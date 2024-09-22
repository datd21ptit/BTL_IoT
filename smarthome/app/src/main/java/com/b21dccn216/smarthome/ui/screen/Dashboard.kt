package com.b21dccn216.smarthome.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.b21dccn216.smarthome.SmartHomeViewmodel
import com.b21dccn216.smarthome.R
import com.b21dccn216.smarthome.model.SensorType
import com.b21dccn216.smarthome.ui.components.ActionBox
import com.b21dccn216.smarthome.ui.components.LineChartComponent
import com.b21dccn216.smarthome.ui.components.SensorInformationBox


@Composable
fun DashboardScreen(
    viewmodel: SmartHomeViewmodel,
    innerPadding: PaddingValues
){
    val uiState by viewmodel.uiStateDashboard.collectAsState()
    val cell = 3
    LazyVerticalGrid(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = innerPadding.calculateBottomPadding()),
        columns = GridCells.Fixed(cell),
        contentPadding = PaddingValues(top = innerPadding.calculateTopPadding(), bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SensorInformationBox( sensorType = SensorType.Temperature, value = uiState.listTemp.last().toString() ) }
        item { SensorInformationBox( sensorType = SensorType.Humidity, value = uiState.listHumid.last().toString() ) }
        item { SensorInformationBox( sensorType = SensorType.Light, value = uiState.listLight.last().toString() ) }

        item {
            ActionBox(icon = R.drawable.lightbulb, deviceName = "Bulb",
                isOn = uiState.led == "on",
                onClick = {
                    if(uiState.led == "on"){
                        viewmodel.clickAction(device = "led", "off")
                    }else{
                        viewmodel.clickAction(device = "led", "on")
                    }
                }
            )
        }
        item {
            ActionBox(icon = R.drawable.fan, deviceName = "Fan",
                isOn = uiState.fan == "on",
                onClick = {
                    if(uiState.fan == "on"){
                        viewmodel.clickAction(device = "fan", "off")
                    }else{
                        viewmodel.clickAction(device = "fan", "on")
                    }
                }
            )
        }
        item {
            ActionBox(icon = R.drawable.fan, deviceName = "Relay",
                isOn = uiState.relay == "on",
                onClick = {
                    if(uiState.relay == "on"){
                        viewmodel.clickAction(device = "relay", "off")
                    }else{
                        viewmodel.clickAction(device = "relay", "on")
                    }
                }) }

        item( span = { GridItemSpan(cell) }) {
            LineChartComponent(
                name = "Light",
                chartData = uiState.listLight,
                step = 200,
            ) }
        item( span = { GridItemSpan(cell) }) {
            LineChartComponent(
                name = "Humidity",
                chartData = uiState.listHumid,
                step = 10,
                colorChart = Color(0xFF179BAE),
            )
        }
        item( span = { GridItemSpan(cell) }) {
            LineChartComponent(
                name = "Temparature",
                step = 10,
                chartData = uiState.listTemp,
            ) }


    }
}


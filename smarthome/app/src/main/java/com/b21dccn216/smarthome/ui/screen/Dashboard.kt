package com.b21dccn216.smarthome.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.b21dccn216.smarthome.SmartHomeViewmodel
import com.b21dccn216.smarthome.R
import com.b21dccn216.smarthome.model.SensorType
import com.b21dccn216.smarthome.ui.components.ActionBox
import com.b21dccn216.smarthome.ui.components.LineChartComponent
import com.b21dccn216.smarthome.ui.components.SensorBox
import com.b21dccn216.smarthome.ui.components.TitleAndLimit


@Composable
fun DashboardScreen(
    viewmodel: SmartHomeViewmodel,
    innerPadding: PaddingValues
){
    val uiState by viewmodel.uiStateDashboard.collectAsState()
    val cell = 4
    LazyVerticalGrid(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = innerPadding.calculateBottomPadding()),
        columns = GridCells.Fixed(cell),
        contentPadding = PaddingValues(top = innerPadding.calculateTopPadding(), bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

            item {
                SensorBox(
                    sensorType = SensorType.Temperature,
                    value = uiState.data.listTemp.last().toString()
                )
            }
            item {
                SensorBox(
                    sensorType = SensorType.Humidity,
                    value = uiState.data.listHumid.last().toString()
                )
            }
            item {
                SensorBox(
                    sensorType = SensorType.Light,
                    value = uiState.data.listLight.last().toString()
                )
            }
            item {
                SensorBox(
                    sensorType = SensorType.Wind,
                    value = uiState.data.listWind.last().toString()
                )

            }

        item(span  = { GridItemSpan(cell) }) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                    ActionBox(icon = R.drawable.lightbulb, deviceName = "Bulb",
                        isOn = uiState.data.led == "on",
                        onClick = {
                            if(uiState.data.led == "on"){
                                viewmodel.clickAction(device = "led", "off")
                            }else{
                                viewmodel.clickAction(device = "led", "on")
                            }
                        }
                    )

                    ActionBox(icon = R.drawable.fan, deviceName = "Fan",
                        isOn = uiState.data.fan == "on",
                        onClick = {
                            if(uiState.data.fan == "on"){
                                viewmodel.clickAction(device = "fan", "off")
                            }else{
                                viewmodel.clickAction(device = "fan", "on")
                            }
                        }
                    )

                    ActionBox(icon = R.drawable.fan, deviceName = "Relay",
                        isOn = uiState.data.relay == "on",
                        onClick = {
                            if(uiState.data.relay == "on"){
                                viewmodel.clickAction(device = "relay", "off")
                            }else{
                                viewmodel.clickAction(device = "relay", "on")
                            }
                        })
            }
        }


        item( span = { GridItemSpan(cell) }) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                TitleAndLimit(modifier = Modifier
                    .height(60.dp)
                    .padding(horizontal = 16.dp),
                    selectedOption = uiState.limitD,
                    onOptionSelected = {viewmodel.changeLimit(it)})
                LineChartComponent(
                    name = "Light",
                    chartData = uiState.data.listLight,
                    step = 200,
                    absMaxYPoint = 1800,
                    tempMin = 0
                )
                LineChartComponent(
                    name = "Humidity",
                    chartData = uiState.data.listHumid,
                    step = 10,
                    colorChart = Color(0xFF179BAE),
                    absMaxYPoint = 90,
                    tempMin = 0
                )
                LineChartComponent(
                    name = "Temparature",
                    step = 10,
                    chartData = uiState.data.listTemp,
                    absMaxYPoint = 40,
                    tempMin = 0
                )
                LineChartComponent(
                    name = "Wind",
                    step = 10,
                    chartData = uiState.data.listWind,
                    absMaxYPoint = 90,
                    tempMin = 0
                )
            }
         }
    }
}


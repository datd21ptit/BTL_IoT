package com.b21dccn216.smarthome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.b21dccn216.smarthome.network.ApiService
import com.b21dccn216.smarthome.network.SmartHomeRepository
import com.b21dccn216.smarthome.network.retrofit
import com.b21dccn216.smarthome.ui.theme.SmartHomeTheme



@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHomeTheme {
                SmarthomeNavigation(
                )
            }
        }
    }
}

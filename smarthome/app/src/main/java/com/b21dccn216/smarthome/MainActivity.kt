package com.b21dccn216.smarthome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.b21dccn216.smarthome.ui.screen.SmartHomeNavigation
import com.b21dccn216.smarthome.ui.theme.SmartHomeTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHomeTheme {
                SmartHomeNavigation()
            }
        }
    }
}

package com.b21dccn216.smarthome.model

import androidx.compose.ui.graphics.Color
import com.b21dccn216.smarthome.R

sealed class SensorType(
    val icon: Int,              //Drawable icon
    val unit: Int,              //Drawable icon unit
    val color: Color,           //Color of components
) {

    // value = 100 - (value - minValue) / (maxValue - minValue) * 100
    abstract fun caculateGradient(value: Int): Int

    data object Temperature : SensorType(
        icon = R.drawable.thermometer,
        unit = R.drawable.celsius,
        color = Color(0xFFFF8343)
    ){
        override fun caculateGradient(value: Int): Int {
            return (100 - 2 * value)
        }
    }
    data object Wind : SensorType(
        icon = R.drawable.fan,
        unit = R.drawable.celsius,
        color = Color(0xFFFF8343)
    ){
        override fun caculateGradient(value: Int): Int {
            return (100 - 2 * value)
        }
    }
    data object Humidity : SensorType(
        icon = R.drawable.humidity,
        unit = R.drawable.percent,
        color = Color(0xFF179BAE)
    ) {
        override fun caculateGradient(value: Int): Int {
            return (100 - value)
        }
    }

    data object Light : SensorType(
        icon = R.drawable.brightness,
        unit = R.drawable.lux,
        color = Color(0xFFFF8343)
    ) {
        override fun caculateGradient(value: Int): Int {
            return (100 - (value/8))
        }
    }
}
package com.b21dccn216.smarthome.network.dto

data class DashboardDTO(
    val led: String = "off",
    val fan: String = "off",
    val relay: String = "off",
    val listTemp: MutableList<Int> = mutableListOf(),
    val listHumid: MutableList<Int> = mutableListOf(),
    val listLight: MutableList<Int> = mutableListOf(),
    val listWind: MutableList<Int> = mutableListOf(),
)
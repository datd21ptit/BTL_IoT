package com.b21dccn216.smarthome.model

import android.content.Context
import com.b21dccn216.smarthome.network.ApiService
import com.b21dccn216.smarthome.network.SmartHomeRepository
import com.b21dccn216.smarthome.network.retrofit

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val appRepository: SmartHomeRepository
}

/**
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [AppContainer]
     */
    override val appRepository: SmartHomeRepository by lazy {
        SmartHomeRepository(retrofit.create(ApiService::class.java))
    }
}
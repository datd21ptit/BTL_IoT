package com.b21dccn216.smarthome

import android.app.Application
import com.b21dccn216.smarthome.model.AppContainer
import com.b21dccn216.smarthome.model.AppDataContainer

class SmartHomeApplication: Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
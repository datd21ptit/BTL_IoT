package com.b21dccn216.smarthome

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SmartHomeViewmodel(smartHomeApplication().container.appRepository)
        }
    }
}

fun CreationExtras.smartHomeApplication(): SmartHomeApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as SmartHomeApplication)
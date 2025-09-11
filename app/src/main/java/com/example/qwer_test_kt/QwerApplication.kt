package com.example.qwer_test_kt

import android.app.Application
import android.content.IntentFilter
import com.example.qwer_test_kt.gomin.wiget.BatteryChangeReceiver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class qwerApplication : Application() {
    private var batteryChangeReceiver: BatteryChangeReceiver? = null
    override fun onCreate() {
        super.onCreate()
        batteryChangeReceiver = BatteryChangeReceiver()
        val filter = IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryChangeReceiver, filter)
    }
    override fun onTerminate() {
        batteryChangeReceiver?.let { unregisterReceiver(it) }
        batteryChangeReceiver = null
        super.onTerminate()
    }
}
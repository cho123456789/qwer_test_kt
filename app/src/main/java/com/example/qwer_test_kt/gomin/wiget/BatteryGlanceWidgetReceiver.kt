package com.example.qwer_test_kt.gomin.wiget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class BatteryGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget : GlanceAppWidget = BatteryGlanceWidgetProvider()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if(intent.action == Intent.ACTION_BATTERY_CHANGED){
            MainScope().launch {
                val appWidgetManager = GlanceAppWidgetManager(context)
                val glanceIds =
                    appWidgetManager.getGlanceIds(BatteryGlanceWidgetProvider::class.java)

                if (glanceIds.isNotEmpty()) {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    val percentage = ((level * 100) / scale.toFloat()).roundToInt()
                    val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

                    glanceIds.forEach { glanceId ->
                        updateAppWidgetState(context, glanceId) { prefs ->
                            prefs[BATTERY_STATUS] = status
                            prefs[BATTERY_PECENTAGE] = percentage
                        }
                        glanceAppWidget.update(context, glanceId)
                    }
                }
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val batteryIntent = context.registerReceiver(null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        batteryIntent?.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val percentage = ((level * 100) / scale.toFloat()).roundToInt()
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

            MainScope().launch {
                val glanceAppWidgetManager = GlanceAppWidgetManager(context)
                val glanceIds = glanceAppWidgetManager.getGlanceIds(BatteryGlanceWidgetProvider::class.java)

                glanceIds.forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { prefs ->
                        prefs[BATTERY_LEVEL] = percentage
                        prefs[BATTERY_STATUS] = status
                    }
                    glanceAppWidget.update(context, glanceId)
                }
            }
        }
    }

    override fun onEnabled(context: Context?) {
        if(context != null) {
            super.onEnabled(context)
            startBatteryMonitoringService(context)
        }
    }

    override fun onDisabled(context: Context?) {
        if(context != null) {
            super.onDisabled(context)
            stopBatteryMonitoringService(context)
        }
    }

    private fun startBatteryMonitoringService(context: Context) {
        val serviceIntent = Intent(context, BatteryUpdateService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    private fun stopBatteryMonitoringService(context: Context) {
        val serviceIntent = Intent(context, BatteryUpdateService::class.java)
        context.stopService(serviceIntent)
    }
}

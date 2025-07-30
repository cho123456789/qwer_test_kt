package com.example.qwer_test_kt

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.RemoteViews

class BatteryWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateBatteryWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateBatteryWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val batteryStatus =
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val percentage = if (level >= 0 && scale > 0) (level * 100) / scale else -1

        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging =
            status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        val views = RemoteViews(context.packageName, R.layout.battery_widget)
        views.setTextViewText(R.id.battery_level, "$percentage%")

        views.setProgressBar(R.id.battery_progress, 100, percentage, false)


        // 아이콘이 있다면 꼭 visibility 처리 추가!
        // views.setViewVisibility(R.id.charging_icon, if (isCharging) View.VISIBLE else View.GONE)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
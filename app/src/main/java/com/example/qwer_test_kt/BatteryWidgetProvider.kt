package com.example.qwer_test_kt

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.View
import android.widget.RemoteViews
import kotlin.math.roundToInt

class BatteryWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        // 위젯 초기화용 기본 갱신 코드
        context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))?.let {
            updateBatteryWidget(context, appWidgetManager, it)
        }
    }

    companion object {
        fun updateBatteryWidget(context: Context, appWidgetManager: AppWidgetManager, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val percentage = (level * 100) / scale.toFloat()
            val percentageDisplay = percentage.roundToInt()  // 표시용 정수 변환

            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val views = RemoteViews(context.packageName, R.layout.battery_widget)
            when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    views.setViewVisibility(R.id.battery_progress_charging, View.VISIBLE)
                    views.setViewVisibility(R.id.battery_progress_normal, View.GONE)
                    views.setProgressBar(
                        R.id.battery_progress_charging,
                        100,
                        percentageDisplay,
                        false
                    )

                }

                BatteryManager.BATTERY_STATUS_DISCHARGING -> {
                    views.setViewVisibility(R.id.battery_progress_charging, View.GONE)
                    views.setViewVisibility(R.id.battery_progress_normal, View.VISIBLE)
                    views.setProgressBar(
                        R.id.battery_progress_normal,
                        100,
                        percentageDisplay,
                        false
                    )
                }

                BatteryManager.BATTERY_STATUS_FULL -> {
                    views.setViewVisibility(R.id.battery_progress_charging, View.VISIBLE)
                    views.setViewVisibility(R.id.battery_progress_normal, View.GONE)
                    views.setProgressBar(
                        R.id.battery_progress_charging,
                        100,
                        percentageDisplay,
                        false
                    )
                }
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> {}
                else -> {}
            }

            views.setTextViewText(R.id.battery_level, "$percentageDisplay%")

            val thisWidget = ComponentName(context, BatteryWidgetProvider::class.java)
            appWidgetManager.updateAppWidget(thisWidget, views)
        }
    }
}
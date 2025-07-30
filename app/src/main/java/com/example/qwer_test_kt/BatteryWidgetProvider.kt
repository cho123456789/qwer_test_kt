package com.example.qwer_test_kt

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.RemoteViews

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

            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val statusText = when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> "충전 중"
                BatteryManager.BATTERY_STATUS_DISCHARGING -> "사용 중"
                BatteryManager.BATTERY_STATUS_FULL -> "완료"
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "충전 안 함"
                else -> "알 수 없음"
            }

            val views = RemoteViews(context.packageName, R.layout.battery_widget)
            views.setTextViewText(R.id.battery_level, "$percentage% ($statusText)")
            views.setProgressBar(R.id.battery_progress, 100, percentage.toInt(), false)

            val thisWidget = ComponentName(context, BatteryWidgetProvider::class.java)
            appWidgetManager.updateAppWidget(thisWidget, views)
        }
    }
}
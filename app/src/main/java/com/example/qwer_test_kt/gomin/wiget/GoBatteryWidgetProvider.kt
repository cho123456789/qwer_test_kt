package com.example.qwer_test_kt.gomin.wiget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.presentation.GominJungdokViewModel
import com.example.qwer_test_kt.presentation.downloadBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class GoBatteryWidgetProvider : AppWidgetProvider() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val sharedPrefs = context.getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
        val wallpaperUrl = sharedPrefs.getString("widgetWallpaperUrl", null)
        val widgetType = sharedPrefs.getString("widgetType", null)

        val views = RemoteViews(context.packageName, R.layout.battery_go_widget)

        startBatteryMonitoringService(context)


        if (widgetType != null && wallpaperUrl != null) {
            coroutineScope.launch {
                val bitmap = downloadBitmap(context, wallpaperUrl)
                if (bitmap != null) {
                    views.setImageViewBitmap(
                        R.id.widget_background,
                        bitmap
                    )
                }

                // 배터리 정보 업데이트는 UI 업데이트 후 진행
                val intent =
                    context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                intent?.let {
                    updateBatteryInfo(context, views, it)
                }

                val thisWidget = ComponentName(context, GoBatteryWidgetProvider::class.java)
                appWidgetManager.updateAppWidget(thisWidget, views)
            }
        } else {
            // URL이 없는 경우, 배터리 정보만 업데이트
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            intent?.let {
                updateBatteryInfo(context, views, it)
            }
            val thisWidget = ComponentName(context, GoBatteryWidgetProvider::class.java)
            appWidgetManager.updateAppWidget(thisWidget, views)
        }


        context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))?.let {
            updateBatteryWidget(context, appWidgetManager, it)
        }
    }
    override fun onEnabled(context: Context) {
        // 위젯의 첫 번째 인스턴스가 추가될 때 호출됩니다.
        super.onEnabled(context)
        startBatteryMonitoringService(context)
    }
    override fun onDisabled(context: Context) {
        // 위젯의 마지막 인스턴스가 제거될 때 호출됩니다.
        super.onDisabled(context)
        // 서비스 종료
        val serviceIntent = Intent(context, GoBatteryMonitorService::class.java)
        context.stopService(serviceIntent)
    }
    private fun startBatteryMonitoringService(context: Context) {
        val serviceIntent = Intent(context, GoBatteryMonitorService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    private fun updateBatteryInfo(context: Context, views: RemoteViews, intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val percentage = (level * 100) / scale.toFloat()
        val percentageDisplay = percentage.roundToInt()

        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

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
    }

    companion object {
        fun updateBatteryWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            intent: Intent
        ) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val percentage = (level * 100) / scale.toFloat()
            val percentageDisplay = percentage.roundToInt()  // 표시용 정수 변환

            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val views = RemoteViews(context.packageName, R.layout.battery_go_widget)
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

            val thisWidget = ComponentName(context, GoBatteryWidgetProvider::class.java)
            appWidgetManager.updateAppWidget(thisWidget, views)
        }
    }
}
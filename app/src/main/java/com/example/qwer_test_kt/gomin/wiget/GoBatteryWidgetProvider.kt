package com.example.qwer_test_kt.gomin.wiget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.gomin.view.downloadBitmap
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

        if (widgetType != null && wallpaperUrl != null) {
            coroutineScope.launch {
                Log.d("GoWatchWidget", "Downloading image from: $wallpaperUrl") // 로그 추가
                val bitmap = downloadBitmap(context, wallpaperUrl)
                if (bitmap != null) {
                    views.setImageViewBitmap(
                        R.id.widget_background,
                        bitmap
                    )
                    Log.d("GoWatchWidget", "Image downloaded successfully!") // 성공 로그
                } else {
                    Log.e("GoWatchWidget", "Failed to download bitmap from $wallpaperUrl") // 실패 로그
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
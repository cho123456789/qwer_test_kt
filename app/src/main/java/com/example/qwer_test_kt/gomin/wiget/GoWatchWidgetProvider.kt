package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.widget.RemoteViews
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.gomin.view.downloadBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GoWatchWidgetProvider : AppWidgetProvider() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_UPDATE_TIME) {
            updateAllWidgets(context)
            scheduleNextUpdate(context)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateAllWidgets(context)
        scheduleNextUpdate(context)
    }

    @SuppressLint("RemoteViewLayout")
    private fun updateAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, GoWatchWidgetProvider::class.java)
        val widgetIds = appWidgetManager.getAppWidgetIds(componentName)

        val sharedPrefs = context.getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
        val wallpaperUrl = sharedPrefs.getString("widgetWallpaperUrl", null)
        val widgetType = sharedPrefs.getString("widgetType", null)

        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        var currentIndex = prefs.getInt("current_image_index", 0)


        for (id in widgetIds) {
            val now = Calendar.getInstance()
            val views = RemoteViews(context.packageName, R.layout.go_watch_widget)

            val dateStrWithYear = SimpleDateFormat("yyyy년 M월 d일 EEEE", Locale.KOREAN).format(now.time)
            views.setTextViewText(R.id.widget_date, dateStrWithYear)

            val amPm = if (now.get(Calendar.AM_PM) == Calendar.AM) "오전" else "오후"
            val hour = now.get(Calendar.HOUR)
            val displayHour = if (hour == 0) 12 else hour
            val minute = now.get(Calendar.MINUTE)

            val timeStr = String.format("%s %d:%02d", amPm, displayHour, minute)

            val spannable = SpannableString(timeStr)
            spannable.setSpan(
                RelativeSizeSpan(0.50f),
                0,
                amPm.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            views.setTextViewText(R.id.widget_time, spannable)

            if (widgetType != null && wallpaperUrl != null) {
                coroutineScope.launch {
                    Log.d("GoWatchWidget", "Downloading image from: $wallpaperUrl") // 로그 추가
                    val bitmap = downloadBitmap(context, wallpaperUrl)
                    if (bitmap != null) {
                        views.setImageViewBitmap(
                            R.id.go_widget_image,
                            bitmap
                        )
                        Log.d("GoWatchWidget", "Image downloaded successfully!") // 성공 로그
                    } else {
                        Log.e(
                            "GoWatchWidget",
                            "Failed to download bitmap from $wallpaperUrl"
                        ) // 실패 로그
                    }
                    appWidgetManager.updateAppWidget(id, views)
                }
            } else {
                appWidgetManager.updateAppWidget(id, views)
            }
            currentIndex = (currentIndex + 1) % 36
            prefs.edit().putInt("current_image_index", currentIndex).apply()
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNextUpdate(context: Context) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 1)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val triggerAtMillis = calendar.timeInMillis

        val intent = Intent(context, GoWatchWidgetProvider::class.java).apply {
            action = ACTION_UPDATE_TIME
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    companion object {
        const val ACTION_UPDATE_TIME = "com.example.qwer_test_kt.UPDATE_TIME"
    }
}
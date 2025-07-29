package com.example.qwer_test_kt

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
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SiyeonWidgetProvider : AppWidgetProvider() {

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
        val componentName = ComponentName(context, SiyeonWidgetProvider::class.java)
        val widgetIds = appWidgetManager.getAppWidgetIds(componentName)
        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        var currentIndex = prefs.getInt("current_image_index", 0)

        for (id in widgetIds) {
            val now = Calendar.getInstance()
            //val imageName = "s${currentIndex + 1}"  // s1 ~ s36
            //val imageRes = context.resources.getIdentifier(imageName, "drawable", context.packageName)
            val views = RemoteViews(context.packageName, R.layout.siyeon_widget)
            val dateStr = SimpleDateFormat("M월 d일 EEEE", Locale.KOREAN).format(now.time)
            views.setTextViewText(R.id.widget_date, dateStr)

            val amPm = if (now.get(Calendar.AM_PM) == Calendar.AM) "오전" else "오후"
            val hour = now.get(Calendar.HOUR)
            val displayHour = if (hour == 0) 12 else hour
            val minute = now.get(Calendar.MINUTE)

            val timeStr = String.format("%s %d:%02d", amPm, displayHour, minute)

            val imageRes = R.drawable.siyeon2
            views.setImageViewResource(R.id.widget_image, imageRes)

            //if (imageRes != 0) {
            //     views.setImageViewResource(R.id.widget_image, imageRes)
            // }

            val spannable = SpannableString(timeStr)
            spannable.setSpan(
                RelativeSizeSpan(0.50f),
                0,
                amPm.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            views.setTextViewText(R.id.widget_time, spannable)

            // views.setTextViewText(R.id.widget_weather, "맑음")
            appWidgetManager.updateAppWidget(id, views)

            currentIndex = (currentIndex + 1) % 36
            prefs.edit().putInt("current_image_index", currentIndex).apply()
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNextUpdate(context: Context) {
        val intent = Intent(context, SiyeonWidgetProvider::class.java).apply {
            action = ACTION_UPDATE_TIME
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAtMillis = System.currentTimeMillis() + 60_000L // 1분

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
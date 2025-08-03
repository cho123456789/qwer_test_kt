package com.example.qwer_test_kt

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import java.time.LocalDate

class CalendarWidgetProvider : AppWidgetProvider() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, ScheduleWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(context.packageName, R.layout.calendar_widget)
            views.setRemoteAdapter(R.id.schedule_list, intent)

            val prefs = context.getSharedPreferences("qwer_schedule_prefs", Context.MODE_PRIVATE)
            val year = prefs.getInt("year", LocalDate.now().year)
            val month = prefs.getInt("month", LocalDate.now().monthValue)
            views.setTextViewText(R.id.text_month, "${year}년 ${month}월")

            // Prev Button
            val prevIntent = Intent(context, CalendarWidgetProvider::class.java).apply {
                action = "ACTION_PREV_MONTH"
            }
            val prevPendingIntent = PendingIntent.getBroadcast(
                context, 0, prevIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.button_prev, prevPendingIntent)

            // Next Button
            val nextIntent = Intent(context, CalendarWidgetProvider::class.java).apply {
                action = "ACTION_NEXT_MONTH"
            }
            val nextPendingIntent = PendingIntent.getBroadcast(
                context, 1, nextIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.button_next, nextPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val prefs = context.getSharedPreferences("qwer_schedule_prefs", Context.MODE_PRIVATE)
        var year = prefs.getInt("year", LocalDate.now().year)
        var month = prefs.getInt("month", LocalDate.now().monthValue)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisWidget = ComponentName(context, CalendarWidgetProvider::class.java)

        when (intent.action) {
            "ACTION_PREV_MONTH" -> {
                val newDate = LocalDate.of(year, month, 1).minusMonths(1)
                prefs.edit().putInt("year", newDate.year).putInt("month", newDate.monthValue)
                    .apply()
                // 변경 후 위젯 갱신
                val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.schedule_list)
                onUpdate(context, appWidgetManager, appWidgetIds)
            }

            "ACTION_NEXT_MONTH" -> {
                val newDate = LocalDate.of(year, month, 1).plusMonths(1)
                prefs.edit().putInt("year", newDate.year).putInt("month", newDate.monthValue)
                    .apply()
                // 변경 후 위젯 갱신
                val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.schedule_list)
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
    }
}
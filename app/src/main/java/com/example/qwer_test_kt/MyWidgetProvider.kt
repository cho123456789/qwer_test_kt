package com.example.qwer_test_kt

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class MyWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.main_widget)

            views.setTextViewText(R.id.line1, "첫 줄")
            views.setTextViewText(R.id.line2, "두 번째 줄")
            views.setTextViewText(R.id.line3, "세 번째 줄")

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
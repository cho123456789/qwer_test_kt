package com.example.qwer_test_kt

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class MyWidgetProvider : AppWidgetProvider() {

    companion object{
        var imageToggle = false;
        const val ACTION_CLICK = "com.example.widget.CLICK"
    }

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.main_widget)

            val intent = Intent(context, MyWidgetProvider::class.java).apply {
                action = ACTION_CLICK
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_image, pendingIntent)

            views.setTextViewText(R.id.line1, "첫 줄")
            views.setTextViewText(R.id.line2, "두 번째 줄")
            views.setTextViewText(R.id.line3, "세 번째 줄")

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if(intent.action == ACTION_CLICK){
            imageToggle = !imageToggle
            val views = RemoteViews(context.packageName , R.layout.main_widget)
            val newImage = if(imageToggle) R.drawable.qwer4 else R.drawable.qwer5
            views.setImageViewResource(R.id.widget_image, newImage)

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, MyWidgetProvider::class.java)
            appWidgetManager.updateAppWidget(thisWidget, views)
        }
    }
}

// 클릭하면 바뀌는 위젯 4개 - qwer
// 메인 위젯

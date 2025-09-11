package com.example.qwer_test_kt.gomin.wiget

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class GoWatchWidgetRecevier(override val glanceAppWidget: GlanceAppWidget) :GlanceAppWidgetReceiver() {
    companion object {
        const val ACTION_UPDATE_TIME = "com.example.qwer_test_kt.UPDATE_TIME"
    }
    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == ACTION_UPDATE_TIME) {
//            updateAllWidgets(context)
//            scheduleNextUpdate(context)
//        }
    }
}
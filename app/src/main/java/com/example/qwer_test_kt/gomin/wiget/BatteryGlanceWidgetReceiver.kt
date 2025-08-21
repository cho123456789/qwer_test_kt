package com.example.qwer_test_kt.gomin.wiget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class BatteryGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget : GlanceAppWidget
    get() = BatteryGlanceWidgetProvider()
}
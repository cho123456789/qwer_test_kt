package com.example.qwer_test_kt.gomin.wiget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyGlanceAppWidgetUpdater @Inject constructor(
    @ApplicationContext private val context: Context,
    private val widget: GlanceAppWidget
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun updateWidget() {
        coroutineScope.launch {
            widget.updateAll(context)
        }
    }
}
package com.example.qwer_test_kt.gomin.wiget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.compose.ui.input.key.Key.Companion.D
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.PreferencesGlanceStateDefinition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class GoWatchWidgetReceiver : GlanceAppWidgetReceiver() {

    // Your main widget class
    override val glanceAppWidget: GlanceAppWidget = GoWatchWidgetProvider()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        // Get the wallpaper URL from your data source
        val sharedPrefs = context.getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
        val wallpaperUrl = sharedPrefs.getString("widgetWallpaperUrl", "") ?: ""

        if (wallpaperUrl.isEmpty()) {
            Log.e("GoWatchWidgetReceiver", "Wallpaper URL is empty. Skipping update.")
            return
        }

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            // Get all existing widget instances
            val glanceIds = GlanceAppWidgetManager(context)
                .getGlanceIds(GoWatchWidgetProvider::class.java)

            // Update each widget's state individually
            glanceIds.forEach { glanceId ->
                try {
                    // Download the bitmap in a background thread
                    val bitmap = withContext(Dispatchers.IO) {
                        downloadBitmap(context, wallpaperUrl)
                    } ?: throw IllegalStateException("Failed to download bitmap.")

                    // Save the bitmap to a temporary file
                    val file = withContext(Dispatchers.IO) {
                        saveBitmapToTempFile(context, bitmap)
                    }

                    // Update the state for the specific widget instance
                    updateAppWidgetState(
                        context = context,
                        definition = PreferencesGlanceStateDefinition,
                        glanceId = glanceId
                    ) { prefs ->
                        prefs.toMutablePreferences().apply {
                            this[ImageUrlKey] = file.absolutePath
                        }
                    }
                    GoWatchWidgetProvider().update(context, glanceId)

                } catch (e: Exception) {
                    Log.e("GoWatchWidgetReceiver", "Failed to update widget wallpaper.", e)
                }
            }
        }
    }
}
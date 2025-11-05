package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PhotoWidgetProvider : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // 현재 위젯 상태(Preferences)를 가져옴
            val prefs = currentState<Preferences>()
            val wallpaperPath = prefs[ImageUrlKey]

            var widgetBitmap: Bitmap? = null
            if (!wallpaperPath.isNullOrEmpty()) {
                widgetBitmap = try {
                    BitmapFactory.decodeFile(wallpaperPath)
                } catch (e: Exception) {
                    Log.e("GoWatchWidgetProvider", "Error loading bitmap from file: ${e.message}")
                    null
                }
            }
            WidgetLayout(wallpaperBitmap = widgetBitmap)
        }
    }
}

@SuppressLint("RestrictedApi", "DefaultLocale")
@Composable
private fun WidgetLayout(wallpaperBitmap: Bitmap?) {
    Box(
        modifier = GlanceModifier.fillMaxSize()
    ) {
        wallpaperBitmap?.let {
            Image(
                provider = ImageProvider(it),
                contentDescription = "widget_background",
                modifier = GlanceModifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
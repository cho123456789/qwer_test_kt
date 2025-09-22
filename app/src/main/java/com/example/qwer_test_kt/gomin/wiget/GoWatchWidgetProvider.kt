package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

class GoWatchWidgetProvider : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // 현재 위젯 상태(Preferences)를 가져옴
            val prefs = currentState<androidx.datastore.preferences.core.Preferences>()
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
            // UI를 구성하는 컴포저블 함수 호출
            WidgetLayout(wallpaperBitmap = widgetBitmap)
        }
    }
}

@SuppressLint("RestrictedApi", "DefaultLocale")
@Composable
fun WidgetLayout(wallpaperBitmap: Bitmap?) {
    val now = Calendar.getInstance()
    val dateStr = SimpleDateFormat("M월 d일 EEEE", Locale.KOREAN).format(now.time)
    val amPm = if (now.get(Calendar.AM_PM) == Calendar.AM) "오전" else "오후"
    val hour = if (now.get(Calendar.HOUR) == 0) 12 else now.get(Calendar.HOUR)
    val minute = now.get(Calendar.MINUTE)

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

        Column(
            modifier = GlanceModifier.fillMaxWidth().padding(top = 16.dp), // 👈 수정된 부분
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        ) {
            Text(
                text = dateStr,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(color = androidx.compose.ui.graphics.Color.White),
                    fontSize = 12.sp
                ),
            )
            Text(
                text = "$amPm ${String.format("%02d", hour)}:${String.format("%02d", minute)}",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(
                        color = androidx.compose.ui.graphics.Color.White
                    ),
                    fontSize = 25.sp
                )
            )
        }
    }
}
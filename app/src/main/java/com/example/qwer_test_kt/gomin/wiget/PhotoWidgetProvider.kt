package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.qwer_test_kt.MainActivity

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
                    null
                }
            }
            WidgetLayout(context, widgetBitmap)
        }
    }
}

@SuppressLint("RestrictedApi", "DefaultLocale")
@Composable
private fun WidgetLayout(context: Context, wallpaperBitmap: Bitmap?) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable(
                onClick = actionStartActivity<MainActivity>()
            )
    ) {
        if (wallpaperBitmap != null) {
            Image(
                provider = ImageProvider(wallpaperBitmap),
                contentDescription = "widget_background",
                modifier = GlanceModifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // 사진이 없을 때 기본 UI 표시
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📷",
                        style = TextStyle(
                            fontSize = 48.sp,
                            color = ColorProvider(Color.Gray)
                        )
                    )
                    Text(
                        text = "앱에서 사진을\n선택해주세요",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = ColorProvider(Color.Gray),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}
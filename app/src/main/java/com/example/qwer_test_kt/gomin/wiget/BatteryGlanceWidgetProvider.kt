package com.example.qwer_test_kt.gomin.wiget

import android.content.Context
import android.graphics.Bitmap
import android.os.BatteryManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.qwer_test_kt.WidgetEntryPoint
import com.example.qwer_test_kt.domin.model.BatteryInfo
import com.example.qwer_test_kt.domin.model.WidgetData
import com.example.qwer_test_kt.presentation.downloadBitmap
import dagger.hilt.android.EntryPointAccessors
import kotlin.math.roundToInt

class BatteryGlanceWidgetProvider : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context,
            WidgetEntryPoint::class.java
        )
        val viewmodel = hiltEntryPoint.getBatteryWidgetViewModel()
        val uiState = viewmodel.uiState.value

        val widgetBitmap = uiState.widgetData.wallpaperUrl?.let {
            downloadBitmap(context, it)
        }

        provideContent {
            BatteryGlanceWidget(
                batteryInfo = uiState.batteryInfo,
                widgetInfo = uiState.widgetData,
                wallpaperBitmap = widgetBitmap
            )
        }
    }
}

@Composable
fun BatteryGlanceWidget(
    batteryInfo: BatteryInfo,
    widgetInfo: WidgetData,
    wallpaperBitmap: Bitmap?
) {
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
            modifier = GlanceModifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (batteryInfo.status) {
                BatteryManager.BATTERY_STATUS_CHARGING, BatteryManager.BATTERY_STATUS_FULL -> {
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${batteryInfo.percentage.roundToInt()}%",
                            style = TextStyle(
                                color = androidx.glance.color.ColorProvider(
                                    day = Color.White,
                                    night = Color.White
                                ),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${batteryInfo.percentage.roundToInt()}%",
                            style = TextStyle(
                                color = androidx.glance.color.ColorProvider(
                                    day = Color.White,
                                    night = Color.White
                                ),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
            }
        }
    }
}
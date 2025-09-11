package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.BatteryManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
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
import com.example.qwer_test_kt.gomin.BatteryWidgetStateDefinition
import com.example.qwer_test_kt.presentation.downloadBitmap
import dagger.hilt.android.EntryPointAccessors

val BATTERY_LEVEL = androidx.datastore.preferences.core.intPreferencesKey("battery_level")
val BATTERY_STATUS = androidx.datastore.preferences.core.intPreferencesKey("battery_status")
val BATTERY_PECENTAGE = androidx.datastore.preferences.core.intPreferencesKey("battery_pecentage")
class BatteryGlanceWidgetProvider : GlanceAppWidget() {

    override val stateDefinition = BatteryWidgetStateDefinition

    @SuppressLint("StateFlowValueCalledInComposition")
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
            val prefs = currentState<Preferences>()
            val batteryLevel = prefs[BATTERY_LEVEL] ?: 0
            val batteryStatus = prefs[BATTERY_STATUS] ?: -1
            val percentage = prefs[BATTERY_PECENTAGE] ?: 0

            Log.d("Test", "provideGlance: $batteryLevel, $batteryStatus")

            BatteryGlanceWidget(
                batteryInfo = BatteryInfo(
                    percentage = batteryLevel,
                    status = batteryStatus
                ),
                widgetInfo = WidgetData(),
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
            Text(
                text = "${batteryInfo.percentage}%",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color.White,
                        night = Color.White
                    ),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            when (batteryInfo.status) {
                BatteryManager.BATTERY_STATUS_CHARGING,
                BatteryManager.BATTERY_STATUS_FULL -> {
                    Text(
                        text = "충전중",
                        style = TextStyle(
                            color = ColorProvider(day = Color.White, night = Color.White),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                BatteryManager.BATTERY_STATUS_DISCHARGING -> {
                    // 방전중일 때
                    Text(
                        text = "사용중",
                        style = TextStyle(
                            color = ColorProvider(day = Color.White, night = Color.White),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                else -> {
                    Text(
                        text = "대기중",
                        style = TextStyle(
                            color = ColorProvider(day = Color.White, night = Color.White),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
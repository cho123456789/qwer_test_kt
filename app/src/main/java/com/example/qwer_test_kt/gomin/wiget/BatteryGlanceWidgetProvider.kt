package com.example.qwer_test_kt.gomin.wiget

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.BatteryManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.qwer_test_kt.gomin.cafe24
import com.example.qwer_test_kt.gomin.onePop
import com.example.qwer_test_kt.presentation.downloadBitmap
import kotlin.math.roundToInt

class BatteryGlanceWidgetProvider : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val batteryInfo = getBatteryInfo(context) // 배터리 정보
        val widgetInfo = loadWidgetData(context)  // 위젯 데이터

        provideContent {
            BatteryGlanceWidget(
                batteryInfo = batteryInfo,
                widgetInfo = widgetInfo
            )
        }
    }
}

@Composable
fun BatteryGlanceWidget(batteryInfo: BatteryInfo, widgetInfo: WidgetData) {
    Box(
        modifier = GlanceModifier.fillMaxSize()
    ) {
        widgetInfo.wallpaperBitmap?.let {
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
                        // LinearProgressIndicator 부분이 제거되었습니다.
                    }
                }
            }
        }
    }
}

fun getBatteryInfo(context: Context): BatteryInfo {
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    val level = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    val chargingStatus = getChargingStatus(context)

    return BatteryInfo(level.toFloat(), chargingStatus)
}

fun getChargingStatus(context: Context): Int {
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
        context.registerReceiver(null, ifilter)
    }

    return batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
}

suspend fun loadWidgetData(context: Context): WidgetData {
    val sharedPrefs = context.getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
    val wallpaperUrl = sharedPrefs.getString("widgetWallpaperUrl", null)
    val widgetType = sharedPrefs.getString("widgetType", null)

    val bitmap = wallpaperUrl?.let {
        downloadBitmap(context, it)
    }
    return WidgetData(
        wallpaperUrl = wallpaperUrl,
        widgetType = widgetType,
        wallpaperBitmap = bitmap
    )
}

data class BatteryInfo(
    val percentage: Float,
    val status: Int
)

data class WidgetData(
    val wallpaperUrl: String? = null,
    val widgetType: String? = null,
    val wallpaperBitmap: Bitmap? = null
)
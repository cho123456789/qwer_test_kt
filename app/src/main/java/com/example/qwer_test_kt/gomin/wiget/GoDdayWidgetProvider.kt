package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.qwer_test_kt.gomin.util.WidgetPreferencesManager
import com.example.qwer_test_kt.gomin.util.WidgetKeys
import com.example.qwer_test_kt.WidgetRegistrationActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class GoDdayWidgetProvider : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = currentState<Preferences>()
            val wallpaperPath = prefs[WidgetKeys.DdayImageUrlKey]

            // GlanceId로부터 appWidgetId 가져오기
            val appWidgetId = try {
                GlanceAppWidgetManager(context).getAppWidgetId(id)
            } catch (e: Exception) {
                -1
            }

            val widgetPrefs = WidgetPreferencesManager.getInstance(context)

            // 위젯 ID별 데이터 가져오기, 없으면 공유 데이터 사용 (fallback)
            val positionString = if (appWidgetId != -1) {
                widgetPrefs.getWidgetPosition(appWidgetId)
            } else {
                widgetPrefs.getWidgetPosition()
            }

            val textColorHex = if (appWidgetId != -1) {
                widgetPrefs.getTextColor(appWidgetId)
            } else {
                widgetPrefs.getTextColor()
            }

            val ddayTitle = if (appWidgetId != -1) {
                widgetPrefs.getDdayTitle(appWidgetId)
            } else {
                widgetPrefs.getDdayTitle()
            }

            val ddayDate = if (appWidgetId != -1) {
                widgetPrefs.getDdayDate(appWidgetId)
            } else {
                widgetPrefs.getDdayDate()
            }

            var widgetBitmap: Bitmap? = null
            if (!wallpaperPath.isNullOrEmpty()) {
                widgetBitmap = try {
                    BitmapFactory.decodeFile(wallpaperPath)
                } catch (e: Exception) {
                    null
                }
            }

            DdayWidgetLayout(
                context = context,
                wallpaperBitmap = widgetBitmap,
                positionString = positionString,
                textColorHex = textColorHex,
                ddayTitle = ddayTitle,
                ddayDate = ddayDate
            )
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun DdayWidgetLayout(
    context: Context,
    wallpaperBitmap: Bitmap?,
    positionString: String,
    textColorHex: String,
    ddayTitle: String?,
    ddayDate: Long
) {
    // 디데이 계산
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val targetCalendar = Calendar.getInstance().apply {
        timeInMillis = ddayDate
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val diffInMillis = targetCalendar.timeInMillis - today.timeInMillis
    val daysRemaining = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

    val ddayText = when {
        daysRemaining > 0 -> "D-$daysRemaining"
        daysRemaining == 0 -> "D-Day"
        else -> "D+${-daysRemaining}"
    }
    val displayText = ddayTitle?.takeIf { it.isNotBlank() } ?: ddayText

    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN)
    val dateText = dateFormat.format(targetCalendar.time)

    // Hex 문자열을 Color로 변환
    val textColor = try {
        val hexString = textColorHex.removePrefix("#")
        val colorLong = if (hexString.length == 6) {
            ("FF" + hexString).toLong(16)
        } else {
            hexString.toLong(16)
        }
        val color = androidx.compose.ui.graphics.Color(colorLong)
        color
    } catch (e: Exception) {
        androidx.compose.ui.graphics.Color.White
    }

    // 위치 정보 파싱 (x,y,scale 형식)
    val (posX, posY, textScale) = try {
        val parts = positionString.split(",")
        when (parts.size) {
            3 -> Triple(
                parts[0].toFloat(),
                parts[1].toFloat(),
                parts[2].toFloat()
            )

            2 -> Triple(
                parts[0].toFloat(),
                parts[1].toFloat(),
                1.0f
            )

            else -> Triple(0.5f, 0.5f, 1.0f)
        }
    } catch (e: Exception) {
        Triple(0.5f, 0.5f, 1.0f)
    }

    // Y 위치에 따른 정렬 결정
    val verticalAlignment = when {
        posY < 0.33f -> Alignment.Vertical.Top
        posY > 0.67f -> Alignment.Vertical.Bottom
        else -> Alignment.Vertical.CenterVertically
    }

    // X 위치에 따른 정렬 결정
    val horizontalAlignment = when {
        posX < 0.33f -> Alignment.Horizontal.Start
        posX > 0.67f -> Alignment.Horizontal.End
        else -> Alignment.Horizontal.CenterHorizontally
    }
        wallpaperBitmap?.let {
            Image(
                provider = ImageProvider(it),
                contentDescription = "widget_background",
                modifier = GlanceModifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = GlanceModifier.padding(16.dp),
            horizontalAlignment = horizontalAlignment,
        ) {
            // 타이틀 표시 (있는 경우)
            if (false) {
                if (ddayTitle != null) {
                    Text(
                        text = ddayTitle,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(color = textColor),
                            fontSize = (20 * textScale).sp
                        ),
                    )
                }
            }

            // 디데이 텍스트
            Text(
                text = displayText,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(color = textColor),
                    fontSize = (60 * textScale).sp
                )
            )

            // 날짜 텍스트
            Text(
                text = dateText,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    color = ColorProvider(color = textColor),
                    fontSize = (18 * textScale).sp
                ),
            )
        }
}
// PreferencesKey 정의
// Removed ImageUrlKey declaration

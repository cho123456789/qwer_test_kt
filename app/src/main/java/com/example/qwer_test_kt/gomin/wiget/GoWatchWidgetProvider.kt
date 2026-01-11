package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.action.clickable
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.example.qwer_test_kt.gomin.util.WidgetPreferencesManager
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GoWatchWidgetProvider : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // 현재 위젯 상태(Preferences)를 가져옴
            val prefs = currentState<Preferences>()
            val wallpaperPath = prefs[ImageUrlKey]
            val isLoading = prefs[IsLoadingKey] ?: false

            // GlanceId로부터 appWidgetId 가져오기
            val appWidgetId = try {
                androidx.glance.appwidget.GlanceAppWidgetManager(context).getAppWidgetId(id)
            } catch (e: Exception) {
                -1
            }

            // WidgetPreferencesManager에서 위치 정보 읽기
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

            var widgetBitmap: Bitmap? = null
            if (!wallpaperPath.isNullOrEmpty()) {
                widgetBitmap = try {
                    BitmapFactory.decodeFile(wallpaperPath)
                } catch (e: Exception) {
                    null
                }
            }
            // UI를 구성하는 컴포저블 함수 호출
            WidgetLayout(
                wallpaperBitmap = widgetBitmap,
                positionString = positionString,
                textColorHex = textColorHex,
                isLoading = isLoading
            )
        }
    }
}

// 새로고침 액션 콜백
class RefreshTimeAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: androidx.glance.action.ActionParameters
    ) {
        // 로딩 상태 표시
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[IsLoadingKey] = true
        }
        GoWatchWidgetProvider().update(context, glanceId)

        // 잠시 대기 (로딩 표시)
        delay(500)

        // 로딩 상태 해제 및 시간 업데이트
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[IsLoadingKey] = false
        }
        GoWatchWidgetProvider().update(context, glanceId)
    }
}

@SuppressLint("RestrictedApi", "DefaultLocale")
@Composable
private fun WidgetLayout(
    wallpaperBitmap: Bitmap?,
    positionString: String,
    textColorHex: String,
    isLoading: Boolean
) {
    val now = Calendar.getInstance()
    val dateStr = SimpleDateFormat("M월 d일 EEEE", Locale.KOREAN).format(now.time)
    val amPm = if (now.get(Calendar.AM_PM) == Calendar.AM) "오전" else "오후"
    val hour = if (now.get(Calendar.HOUR) == 0) 12 else now.get(Calendar.HOUR)
    val minute = now.get(Calendar.MINUTE)

    // 시간대에 따른 아이콘 결정 (6시~18시: ☀️, 18시~6시: 🌙)
    val currentHour = now.get(Calendar.HOUR_OF_DAY)
    val weatherIcon = if (currentHour in 6..17) "☀️" else "🌙"

    // Hex 문자열을 Color로 변환
    val textColor = try {
        val hexString = textColorHex.removePrefix("#")
        val colorLong = if (hexString.length == 6) {
            // RGB 형식인 경우 알파값 FF 추가
            ("FF" + hexString).toLong(16)
        } else {
            // ARGB 형식인 경우 그대로 사용
            hexString.toLong(16)
        }
        val color = androidx.compose.ui.graphics.Color(colorLong)
        color
    } catch (e: Exception) {
        androidx.compose.ui.graphics.Color.White // 기본값
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
                1.0f // 기본 크기
            )

            else -> Triple(0.5f, 0.5f, 1.0f) // 기본값: 중앙, 기본 크기
        }
    } catch (e: Exception) {
        Triple(0.5f, 0.5f, 1.0f) // 기본값: 중앙, 기본 크기
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

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable(actionRunCallback<RefreshTimeAction>()),
        contentAlignment = Alignment(horizontalAlignment, verticalAlignment)
    ) {
        wallpaperBitmap?.let {
            Image(
                provider = ImageProvider(it),
                contentDescription = "widget_background",
                modifier = GlanceModifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (isLoading) {
            // 로딩 인디케이터
            Column(
                modifier = GlanceModifier.padding(16.dp),
                horizontalAlignment = horizontalAlignment,
            ) {
                Text(
                    text = "⟳",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(color = textColor),
                        fontSize = (40 * textScale).sp
                    ),
                )
                Text(
                    text = "새로고침 중...",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(color = textColor),
                        fontSize = (12 * textScale).sp
                    ),
                )
            }
        } else {
            // 일반 시계 표시 - 새로운 레이아웃
            Column(
                modifier = GlanceModifier.padding(16.dp),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            ) {
                // 오전/오후 + 시간 (한 줄에 표시)
                Row(
                    verticalAlignment = Alignment.Vertical.Bottom,
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Text(
                        text = amPm,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(color = textColor),
                            fontSize = (14 * textScale).sp
                        ),
                    )
                    Text(
                        text = " ",
                        style = TextStyle(
                            fontSize = (6 * textScale).sp
                        )
                    )
                    Text(
                        text = "${hour}:${String.format("%02d", minute)}",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(color = textColor),
                            fontSize = (35 * textScale).sp
                        )
                    )
                }

                // 날짜 + 날씨 아이콘 (한 줄에 표시)
                Row(
                    verticalAlignment = Alignment.Vertical.CenterVertically,
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Text(
                        text = dateStr,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(color = textColor),
                            fontSize = (14 * textScale).sp
                        ),
                    )
                    Text(
                        text = " ",
                        style = TextStyle(
                            fontSize = (4 * textScale).sp
                        )
                    )
                    Text(
                        text = weatherIcon,
                        style = TextStyle(
                            fontSize = (20 * textScale).sp
                        )
                    )
                }
            }
        }
    }
}

// PreferencesKey 정의
private val IsLoadingKey = androidx.datastore.preferences.core.booleanPreferencesKey("is_loading")
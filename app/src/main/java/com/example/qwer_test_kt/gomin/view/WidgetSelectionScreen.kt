package com.example.qwer_test_kt.gomin.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.qwer_test_kt.gomin.wiget.screen.barry
import com.example.qwer_test_kt.gomin.wiget.GoWatchWidgetReceiver
import com.example.qwer_test_kt.gomin.util.WidgetPreferencesManager

// BroadcastReceiver to handle widget pin success
class WidgetPinSuccessReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "위젯이 바탕화면에 생성되었습니다", Toast.LENGTH_SHORT).show()

        // 위젯 타입 가져오기
        val widgetType = intent.getStringExtra("widgetType") ?: ""

        // 위젯 타입에 따라 업데이트 - delay 없이 바로 전송
        when (widgetType) {
            "clock" -> {
                val updateIntent = Intent(context, GoWatchWidgetReceiver::class.java).apply {
                    action = "com.example.qwer_test_kt.UPDATE_IMAGE"
                }
                context.sendBroadcast(updateIntent)
            }

            "dday" -> {
                val updateIntent = Intent(
                    context,
                    com.example.qwer_test_kt.gomin.wiget.GoDdayWidgetReceiver::class.java
                ).apply {
                    action = "com.example.qwer_test_kt.UPDATE_DDAY"
                }
                context.sendBroadcast(updateIntent)
            }

            "photo" -> {
                val updateIntent = Intent(
                    context,
                    com.example.qwer_test_kt.gomin.wiget.PhotoWidgetReceiver::class.java
                ).apply {
                    action = "com.example.qwer_test_kt.UPDATE_PHOTO"
                }
                context.sendBroadcast(updateIntent)
            }
        }
    }
}

@Composable
fun WidgetButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) Color(0xFF4B3B55) else Color(0xFF4B3B55).copy(alpha = 0.65f)
    val radioColor = Color(0xFF1565C0) // 겨울 파란색

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) Color(0xFFFFE4EE) else Color.White,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = ButtonDefaults.elevation(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 라디오 버튼 원
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) Color(0xFF9B5270) else Color(0xFF9B5270).copy(alpha = 0.45f),
                            shape = CircleShape
                        )
                        .padding(4.dp)
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(width = 4.dp, color = radioColor, shape = CircleShape)
                                .padding(4.dp) // 안쪽 padding 추가
                        ) {
                            // 내부를 투명하게 만듭니다.
                            Box(modifier = Modifier.fillMaxSize())
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 위젯 이름
                Text(
                    text = text,
                    fontFamily = barry,
                    color = contentColor
                )
            }
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun requestPinWidget(
    context: Context,
    providerComponent: ComponentName,
    wallpaperUrl: String,
    widgetType: String,
    position: String
) {
    val appWidgetManager = AppWidgetManager.getInstance(context)

    // 먼저 위젯 고정이 지원되는지 확인
    if (!appWidgetManager.isRequestPinAppWidgetSupported) {
        Toast.makeText(context, "이 런처는 위젯 고정을 지원하지 않습니다.", Toast.LENGTH_LONG).show()
        return
    }

    // WidgetPreferencesManager를 사용하여 위젯 데이터 저장
    val widgetPrefs = WidgetPreferencesManager.getInstance(context)
    widgetPrefs.saveWidgetData(wallpaperUrl, widgetType, position)

    // PendingIntent 생성 - 사용자가 "Add to Home Screen" 버튼을 눌렀을 때 호출됨
    val successIntent = Intent(context, WidgetPinSuccessReceiver::class.java).apply {
        putExtra("widgetType", widgetType)
    }

    val successCallback = PendingIntent.getBroadcast(
        context,
        0,
        successIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val success = appWidgetManager.requestPinAppWidget(providerComponent, null, successCallback)

    if (success) {
        // 사용자가 "Add to Home Screen"을 누르면 successCallback이 호출됩니다
    } else {
        Toast.makeText(context, "위젯 추가 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
    }
}

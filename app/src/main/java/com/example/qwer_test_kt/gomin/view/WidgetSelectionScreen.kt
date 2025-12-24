package com.example.qwer_test_kt.gomin.view

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper.getMainLooper
import android.util.Log
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

@Composable
fun WidgetButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor =
        if (isSelected) Color.Black.copy(alpha = 1.0f) else Color.Black.copy(alpha = 0.5f)
    val radioColor = Color(0xFF1565C0) // 겨울 파란색

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
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
                            color = Color.Black.copy(alpha = if (isSelected) 1.0f else 0.5f),
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
        Log.e("WidgetSelection", "런처가 위젯 고정을 지원하지 않습니다.")
        return
    }

    // WidgetPreferencesManager를 사용하여 위젯 데이터 저장
    val widgetPrefs = WidgetPreferencesManager.getInstance(context)
    widgetPrefs.saveWidgetData(wallpaperUrl, widgetType, position)

    Log.d(
        "WidgetSelection",
        "위젯 데이터 저장됨 - URL: $wallpaperUrl, Type: $widgetType, Position: $position"
    )

    val success = appWidgetManager.requestPinAppWidget(providerComponent, null, null)

    if (success) {
        //Toast.makeText(context, "위젯이 추가되었습니다!", Toast.LENGTH_SHORT).show()
        Log.d("WidgetSelection", "위젯 추가 성공")

        // 위젯 타입에 따라 즉시 업데이트
        when (widgetType) {
            "clock" -> {
                // 시계 위젯인 경우 즉시 업데이트
                Handler(getMainLooper()).postDelayed({
                    val updateIntent =
                        android.content.Intent(context, GoWatchWidgetReceiver::class.java).apply {
                            action = "com.example.qwer_test_kt.UPDATE_IMAGE"
                        }
                    context.sendBroadcast(updateIntent)
                    Log.d("WidgetSelection", "시계 위젯 업데이트 브로드캐스트 전송")
                }, 1000) // 1초 후 업데이트 (위젯 등록 완료 대기)
            }

            "dday" -> {
                // 디데이 위젯인 경우 즉시 업데이트
                Handler(getMainLooper()).postDelayed({
                    val updateIntent =
                        android.content.Intent(
                            context,
                            com.example.qwer_test_kt.gomin.wiget.GoDdayWidgetReceiver::class.java
                        ).apply {
                            action = "com.example.qwer_test_kt.UPDATE_DDAY"
                        }
                    context.sendBroadcast(updateIntent)
                    Log.d("WidgetSelection", "디데이 위젯 업데이트 브로드캐스트 전송")
                }, 1000) // 1초 후 업데이트 (위젯 등록 완료 대기)
            }
        }
    } else {
        Toast.makeText(context, "위젯 추가를 취소했거나 실패했습니다.", Toast.LENGTH_SHORT).show()
        Log.e("WidgetSelection", "위젯 추가 실패 - Component: ${providerComponent.className}")
    }
}
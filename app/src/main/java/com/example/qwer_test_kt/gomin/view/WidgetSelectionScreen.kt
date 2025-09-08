package com.example.qwer_test_kt.gomin.view

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.qwer_test_kt.gomin.onePop
import com.example.qwer_test_kt.gomin.wiget.BatteryGlanceWidgetReceiver
import kotlin.jvm.java

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WidgetSelectionDialog(
    onDismissRequest: () -> Unit,
    onWidgetSelected: () -> Unit,
    wallpaperUrl: String
) {
    val context = LocalContext.current
    var selectedWidgetProvider by remember { mutableStateOf<ComponentName?>(null) }
    val dialogBackgroundColor = Color(0xFFF8BBD0)

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = dialogBackgroundColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // 타이틀과 'X' 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "위젯 스타일 선택",
                        fontSize = 20.sp,
                        fontFamily = onePop,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "닫기",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 위젯 선택 버튼들 (라디오 버튼 스타일)
                WidgetButton(
                    text = "배터리 위젯",
                    isSelected = selectedWidgetProvider?.className == BatteryGlanceWidgetReceiver::class.java.name,
                    onClick = {
                        selectedWidgetProvider =
                            ComponentName(context, BatteryGlanceWidgetReceiver::class.java)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

//                WidgetButton(
//                    text = "시계 위젯",
//                    isSelected = selectedWidgetProvider?.className == SiyeonWidgetProvider::class.java.name,
//                    onClick = {
//                        selectedWidgetProvider =
//                            ComponentName(context, SiyeonWidgetProvider::class.java)
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                WidgetButton(
//                    text = "사진 위젯",
//                    isSelected = selectedWidgetProvider?.className == ChodanWidgetProvider::class.java.name,
//                    onClick = {
//                        selectedWidgetProvider =
//                            ComponentName(context, ChodanWidgetProvider::class.java)
//                    }
//                )

                Spacer(modifier = Modifier.height(24.dp))

                // 확인/취소 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.LightGray,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "취소", fontFamily = onePop)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            selectedWidgetProvider?.let {
                                // 어떤 위젯 타입인지 확인합니다.
                                val widgetType = when (it.className) {
                                    BatteryGlanceWidgetReceiver::class.java.name -> "battery"
                                  //  SiyeonWidgetProvider::class.java.name -> "clock"
                                  //  ChodanWidgetProvider::class.java.name -> "photo"
                                    else -> "unknown"
                                }
                                // 위젯 타입과 wallpaperUrl을 함께 전달합니다.
                                requestPinWidget(context, it, wallpaperUrl, widgetType)
                                onWidgetSelected()
                            }
                        },
                        enabled = selectedWidgetProvider != null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF3A1C71),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "확인", fontFamily = onePop)
                    }
                }
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
    val contentColor =
        if (isSelected) Color.Black.copy(alpha = 1.0f) else Color.Black.copy(alpha = 0.5f)
    val radioColor = Color(0xFF3A1C71) // '확인' 버튼과 동일한 분홍색

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
                    fontFamily = onePop,
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
    widgetType: String
) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val success = appWidgetManager.requestPinAppWidget(providerComponent, null, null)

    if (success) {
        val sharedPrefs = context.getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putString("widgetWallpaperUrl", wallpaperUrl)
            .putString("widgetType", widgetType)
            .apply()
        Toast.makeText(context, "위젯이 추가되었습니다!", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "위젯 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
    }
}
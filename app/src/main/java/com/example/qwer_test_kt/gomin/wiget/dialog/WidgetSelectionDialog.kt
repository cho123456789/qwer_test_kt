package com.example.qwer_test_kt.gomin.wiget.dialog

import android.content.ComponentName
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.qwer_test_kt.gomin.wiget.screen.barry
import com.example.qwer_test_kt.gomin.view.WidgetButton
import com.example.qwer_test_kt.gomin.view.requestPinWidget
import com.example.qwer_test_kt.gomin.wiget.GoDdayWidgetReceiver
import com.example.qwer_test_kt.gomin.wiget.GoWatchWidgetReceiver
import com.example.qwer_test_kt.gomin.wiget.PhotoWidgetReceiver

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WidgetSelectionDialog(
    onDismissRequest: () -> Unit,
    onWidgetSelected: () -> Unit,
    wallpaperUrl: String,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedWidgetProvider by remember { mutableStateOf<ComponentName?>(null) }
    val winterGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF0F8FF),  // 앨리스 블루 (밝은 하늘색)
            Color(0xFFE6F3FF),  // 연한 파란색
            Color(0xFFFFFFFF)   // 흰색
        )
    )
    var selectedWidgetName by remember { mutableStateOf<String?>(null) }
    var showClockPositionDialog by remember { mutableStateOf(false) }
    var showDdaySetupDialog by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color(0xFFF0F8FF)  // 앨리스 블루
        ) {
            Box(
                modifier = Modifier.background(winterGradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
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
                            fontFamily = barry,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = Color(0xFF1E3A8A)  // 진한 겨울 파란색
                        )
                        IconButton(
                            onClick = onDismissRequest,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "닫기",
                                tint = Color(0xFF1565C0)  // 겨울 파란색
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    WidgetButton(
                        text = "디데이 위젯",
                        isSelected = selectedWidgetName == "dday",
                        onClick = {
                            selectedWidgetName = "dday"
                            selectedWidgetProvider =
                                ComponentName(context, GoDdayWidgetReceiver::class.java)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    WidgetButton(
                        text = "시계 위젯",
                        isSelected = selectedWidgetName == "clock",
                        onClick = {
                            selectedWidgetName = "clock"
                            selectedWidgetProvider =
                                ComponentName(context, GoWatchWidgetReceiver::class.java)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    WidgetButton(
                        text = "사진 위젯",
                        isSelected = selectedWidgetName == "photo",
                        onClick = {
                            selectedWidgetName = "photo"
                            selectedWidgetProvider =
                                ComponentName(context, PhotoWidgetReceiver::class.java)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 확인/취소 버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFE3F2FD),  // 연한 겨울 파란색
                                contentColor = Color(0xFF1565C0)  // 진한 파란색
                            )
                        ) {
                            Text(text = "취소", fontFamily = barry)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                selectedWidgetProvider?.let {
                                    when (selectedWidgetName) {
                                        "dday" -> {
                                            // 디데이 위젯인 경우 설정 다이얼로그 표시
                                            showDdaySetupDialog = true
                                        }
                                        "clock" -> {
                                            // 시계 위젯인 경우 위치 선택 다이얼로그 표시
                                            showClockPositionDialog = true
                                        }

                                        else -> {
                                            // 다른 위젯은 바로 등록
                                            val widgetType = when (it.className) {
                                                PhotoWidgetReceiver::class.java.name -> "photo"
                                                else -> "unknown"
                                            }
                                            requestPinWidget(
                                                context,
                                                it,
                                                wallpaperUrl,
                                                widgetType,
                                                "center"
                                            )
                                            onWidgetSelected()
                                        }
                                    }
                                }
                            },
                            enabled = selectedWidgetProvider != null,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF1565C0),  // 겨울 파란색
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "확인", fontFamily = barry)
                        }
                    }
                }
            }
        }
    }

    // 디데이 설정 다이얼로그
    if (showDdaySetupDialog) {
        DdaySetupDialog(
            wallpaperUrl = wallpaperUrl,
            onDismiss = { showDdaySetupDialog = false },
            onSetupComplete = { position ->
                selectedWidgetProvider?.let {
                    requestPinWidget(context, it, wallpaperUrl, "dday", position)
                    showDdaySetupDialog = false
                    onWidgetSelected()
                }
            }
        )
    }

    // 시계 위치 선택 다이얼로그
    if (showClockPositionDialog) {
        ClockPositionDialog(
            wallpaperUrl = wallpaperUrl,
            onDismiss = { showClockPositionDialog = false },
            onPositionSelected = { position ->
                selectedWidgetProvider?.let {
                    requestPinWidget(context, it, wallpaperUrl, "clock", position)
                    showClockPositionDialog = false
                    onWidgetSelected()
                }
            }
        )
    }
}
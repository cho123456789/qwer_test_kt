package com.example.qwer_test_kt.gomin.wiget.dialog

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.qwer_test_kt.gomin.wiget.screen.barry
import com.example.qwer_test_kt.gomin.util.WidgetPreferencesManager
import com.example.qwer_test_kt.gomin.wiget.dialog.WidgetPositionPreview
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ClockPositionDialog(
    wallpaperUrl: String,
    onDismiss: () -> Unit,
    onPositionSelected: (String) -> Unit
) {
    val context = LocalContext.current
    
    // 텍스트 크기 스케일 (0.5배 ~ 2.0배)
    var textScale by remember { mutableStateOf(1.0f) }

    // 텍스트 색상
    var selectedColor by remember { mutableStateOf(Color.White) }

    // 위치 정보를 저장할 상태
    var currentOffsetX by remember { mutableStateOf(0f) }
    var currentOffsetY by remember { mutableStateOf(0f) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var imageOffsetX by remember { mutableStateOf(0f) }
    var imageOffsetY by remember { mutableStateOf(0f) }

    // 현재 시간 (GoWatchWidgetProvider와 동일한 형식)
    val now = remember { java.util.Calendar.getInstance() }
    val dateStr = remember {
        SimpleDateFormat("M월 d일 EEEE", Locale.KOREAN).format(now.time)
    }
    val amPm = remember {
        if (now.get(java.util.Calendar.AM_PM) == java.util.Calendar.AM) "오전" else "오후"
    }
    val hour = remember {
        if (now.get(java.util.Calendar.HOUR) == 0) 12 else now.get(java.util.Calendar.HOUR)
    }
    val minute = remember {
        now.get(java.util.Calendar.MINUTE)
    }
    val timeStr = remember {
        "${hour}:${String.format("%02d", minute)}"
    }

    // 시간대에 따른 아이콘 결정 (6시~18시: ☀️, 18시~6시: 🌙)
    val currentHour = remember { now.get(java.util.Calendar.HOUR_OF_DAY) }
    val weatherIcon = remember { if (currentHour in 6..17) "☀️" else "🌙" }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color(0xFFF0F8FF)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // 타이틀
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "시계 위치 조정",
                        fontSize = 20.sp,
                        fontFamily = barry,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = Color(0xFF1E3A8A)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "닫기",
                            tint = Color(0xFF1565C0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 안내 텍스트
                Text(
                    text = "시계를 드래그하여 원하는 위치로 이동하세요",
                    fontSize = 14.sp,
                    fontFamily = barry,
                    color = Color.Black.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 글자 크기 조정 슬라이더
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "글자 크기: ${String.format("%.1f", textScale)}x",
                        fontSize = 14.sp,
                        fontFamily = barry,
                        color = Color.Black.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Slider(
                        value = textScale,
                        onValueChange = { textScale = it },
                        valueRange = 0.5f..2.0f,
                        steps = 14,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF1565C0),
                            activeTrackColor = Color(0xFF1565C0),
                            inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 텍스트 색상 선택
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "텍스트 색상",
                        fontSize = 14.sp,
                        fontFamily = barry,
                        color = Color.Black.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "💡 배경이 밝으면 어두운 색, 어두우면 밝은 색을 선택하세요",
                        fontSize = 11.sp,
                        fontFamily = barry,
                        color = Color(0xFF1565C0),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val colors = listOf(
                            Color.White,           // 흰색
                            Color.Black,           // 검정
                            Color(0xFFFF3B30),    // 밝은 빨강
                            Color(0xFFFF69B4),    // 핫핑크
                            Color(0xFF00D4FF),    // 밝은 하늘색 (네온)
                            Color(0xFFFFD700),    // 골드
                            Color(0xFF00FF00),    // 네온 그린
                            Color(0xFFFF00FF)     // 네온 마젠타
                        )

                        colors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(35.dp)
                                    .background(color, CircleShape)
                                    .border(
                                        width = if (selectedColor == color) 3.dp else 1.dp,
                                        color = if (selectedColor == color) Color(0xFF1565C0) else Color.Gray,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = color }
                                    .padding(2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 배경 이미지 프리뷰 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    WidgetPositionPreview(
                        wallpaperUrl = wallpaperUrl,
                        textScale = textScale,
                        estimatedTextWidth = 200f,
                        estimatedTextHeight = 100f,
                        onPositionChanged = { offsetX, offsetY, imgSize, imgOffsetX, imgOffsetY ->
                            currentOffsetX = offsetX
                            currentOffsetY = offsetY
                            imageSize = imgSize
                            imageOffsetX = imgOffsetX
                            imageOffsetY = imgOffsetY
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 오전/오후 + 시간 (한 줄에 표시)
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = amPm,
                                    style = TextStyle(
                                        fontSize = (12 * textScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = selectedColor,
                                        fontFamily = barry,
                                        textAlign = TextAlign.Center,
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(0f, 0f),
                                            blurRadius = 8f
                                        )
                                    )
                                )
                                Spacer(modifier = Modifier.width((4 * textScale).dp))
                                Text(
                                    text = timeStr,
                                    style = TextStyle(
                                        fontSize = (35 * textScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = selectedColor,
                                        fontFamily = barry,
                                        textAlign = TextAlign.Center,
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(0f, 0f),
                                            blurRadius = 12f
                                        )
                                    )
                                )
                            }
                            // 날짜 + 날씨 아이콘 (한 줄에 표시)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = dateStr,
                                    style = TextStyle(
                                        fontSize = (14 * textScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = selectedColor,
                                        fontFamily = barry,
                                        textAlign = TextAlign.Center,
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(0f, 0f),
                                            blurRadius = 8f
                                        )
                                    )
                                )
                                Spacer(modifier = Modifier.width((4 * textScale).dp))
                                Text(
                                    text = weatherIcon,
                                    style = TextStyle(
                                        fontSize = (20 * textScale).sp,
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(0f, 0f),
                                            blurRadius = 8f
                                        )
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 확인/취소 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFE3F2FD),
                            contentColor = Color(0xFF1565C0)
                        )
                    ) {
                        Text(text = "취소", fontFamily = barry)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            // 위치를 이미지 기준 0~1 사이의 비율로 변환하여 저장
                            val positionX =
                                ((currentOffsetX - imageOffsetX) / imageSize.width.toFloat()).coerceIn(
                                    0f,
                                    1f
                                )
                            val positionY =
                                ((currentOffsetY - imageOffsetY) / imageSize.height.toFloat()).coerceIn(
                                    0f,
                                    1f
                                )

                            // "x,y,scale" 형식으로 문자열 생성
                            val position = "${positionX},${positionY},${textScale}"

                            // 선택한 색상을 Hex 형식으로 변환하여 저장 (RGB만)
                            val red = (selectedColor.red * 255).toInt()
                            val green = (selectedColor.green * 255).toInt()
                            val blue = (selectedColor.blue * 255).toInt()
                            val colorHex = String.format("#%02X%02X%02X", red, green, blue)

                            Log.d(
                                "ClockPositionDialog",
                                "선택한 색상 - R:$red, G:$green, B:$blue, Hex:$colorHex"
                            )

                            val widgetPrefs = WidgetPreferencesManager.getInstance(context)
                            widgetPrefs.setTextColor(colorHex)

                            onPositionSelected(position)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF1565C0),
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
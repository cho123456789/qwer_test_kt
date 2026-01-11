package com.example.qwer_test_kt.gomin.wiget.dialog

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun DdaySetupDialog(
    wallpaperUrl: String,
    onDismiss: () -> Unit,
    onSetupComplete: (String) -> Unit
) {
    val context = LocalContext.current

    // 디데이 설정
    var ddayTitle by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }

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

    // 디데이 계산
    val today = Calendar.getInstance()
    today.set(Calendar.HOUR_OF_DAY, 0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.SECOND, 0)
    today.set(Calendar.MILLISECOND, 0)

    val targetCalendar = selectedDate.clone() as Calendar
    targetCalendar.set(Calendar.HOUR_OF_DAY, 0)
    targetCalendar.set(Calendar.MINUTE, 0)
    targetCalendar.set(Calendar.SECOND, 0)
    targetCalendar.set(Calendar.MILLISECOND, 0)

    val diffInMillis = targetCalendar.timeInMillis - today.timeInMillis
    val daysRemaining = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

    val ddayText = when {
        daysRemaining > 0 -> "D-$daysRemaining"
        daysRemaining == 0 -> "D-Day"
        else -> "D+${-daysRemaining}"
    }

    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN)
    val dateText = dateFormat.format(selectedDate.time)

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
                .fillMaxHeight(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color(0xFFF0F8FF)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
                    .navigationBarsPadding()
            ) {
                // 타이틀
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "디데이 위젯 설정",
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

                // 디데이 제목 입력
                OutlinedTextField(
                    value = ddayTitle,
                    onValueChange = { ddayTitle = it },
                    label = { Text("디데이 제목 (선택)", fontFamily = barry) },
                    placeholder = { Text("예: 콘서트까지", fontFamily = barry) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1565C0),
                        unfocusedBorderColor = Color.Gray,
                        textColor = Color.Black,
                        cursorColor = Color(0xFF1565C0)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 날짜 선택 버튼
                Button(
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                selectedDate = Calendar.getInstance().apply {
                                    set(year, month, dayOfMonth)
                                }
                            },
                            selectedDate.get(Calendar.YEAR),
                            selectedDate.get(Calendar.MONTH),
                            selectedDate.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFE3F2FD),
                        contentColor = Color(0xFF1565C0)
                    )
                ) {
                    Text(
                        text = "목표 날짜: ${dateFormat.format(selectedDate.time)}",
                        fontFamily = barry
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 글자 크기 조정
                Column(modifier = Modifier.fillMaxWidth()) {
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
                Column(modifier = Modifier.fillMaxWidth()) {
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
                Text(
                    text = "디데이 위치를 드래그하여 조정하세요",
                    fontSize = 12.sp,
                    fontFamily = barry,
                    color = Color.Black.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    WidgetPositionPreview(
                        wallpaperUrl = wallpaperUrl,
                        textScale = textScale,
                        estimatedTextWidth = 300f,
                        estimatedTextHeight = 200f,
                        onPositionChanged = { offsetX, offsetY, imgSize, imgOffsetX, imgOffsetY ->
                            currentOffsetX = offsetX
                            currentOffsetY = offsetY
                            imageSize = imgSize
                            imageOffsetX = imgOffsetX
                            imageOffsetY = imgOffsetY
                        }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (ddayTitle.isNotEmpty()) {
                                Text(
                                    text = ddayTitle,
                                    style = TextStyle(
                                        fontSize = (14 * textScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = selectedColor,
                                        fontFamily = barry,
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(0f, 0f),
                                            blurRadius = 8f
                                        )
                                    )
                                )
                            }
                            Text(
                                text = ddayText,
                                style = TextStyle(
                                    fontSize = (40 * textScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = selectedColor,
                                    fontFamily = barry,
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(0f, 0f),
                                        blurRadius = 12f
                                    )
                                )
                            )
                            Text(
                                text = dateText,
                                style = TextStyle(
                                    fontSize = (12 * textScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = selectedColor,
                                    fontFamily = barry,
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
                            // 위치를 이미지 기준 0~1 사이의 비율로 변환
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
                            val position = "${positionX},${positionY},${textScale}"

                            // 색상을 Hex로 변환
                            val red = (selectedColor.red * 255).toInt()
                            val green = (selectedColor.green * 255).toInt()
                            val blue = (selectedColor.blue * 255).toInt()
                            val colorHex = String.format("#%02X%02X%02X", red, green, blue)

                            // 데이터 저장
                            val widgetPrefs = WidgetPreferencesManager.getInstance(context)
                            widgetPrefs.setWallpaperUrl(wallpaperUrl)
                            widgetPrefs.setDdayTitle(ddayTitle)
                            widgetPrefs.setDdayDate(selectedDate.timeInMillis)
                            widgetPrefs.setTextColor(colorHex)
                            widgetPrefs.setWidgetPosition(position)

                            onSetupComplete(position)
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

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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qwer_test_kt.gomin.onePop
import com.example.qwer_test_kt.gomin.wiget.GoBatteryWidgetProvider
import com.example.qwer_test_kt.gomin.wiget.GoWatchWidgetReceiver
import com.example.qwer_test_kt.gomin.wiget.PhotoWidgetReceiver
import com.example.qwer_test_kt.gomin.wiget.WidgetPreferencesManager
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

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
    val dialogBackgroundColor = Color(0xFFF8BBD0)
    var selectedWidgetName by remember { mutableStateOf<String?>(null) }
    var showClockPositionDialog by remember { mutableStateOf(false) }

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

                WidgetButton(
                    text = "배터리 위젯",
                    isSelected = selectedWidgetName == "battery",
                    onClick = {
                        selectedWidgetName = "battery"
                        selectedWidgetProvider =
                            ComponentName(context, GoBatteryWidgetProvider::class.java)
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
                                // 시계 위젯인 경우 위치 선택 다이얼로그 표시
                                if (selectedWidgetName == "clock") {
                                    showClockPositionDialog = true
                                } else {
                                    // 다른 위젯은 바로 등록
                                    val widgetType = when (it.className) {
                                        GoBatteryWidgetProvider::class.java.name -> "battery"
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

@Composable
fun ClockPositionDialog(
    wallpaperUrl: String,
    onDismiss: () -> Unit,
    onPositionSelected: (String) -> Unit
) {
    val context = LocalContext.current
    // 드래그 오프셋 (픽셀 단위)
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // 텍스트 크기 스케일 (0.5배 ~ 2.0배)
    var textScale by remember { mutableStateOf(1.0f) }

    // 텍스트 색상 (Hex 형식)
    var selectedColor by remember { mutableStateOf(Color.White) }

    // 컨테이너 크기 (전체 Box 크기)
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    // 이미지 실제 크기 (이미지가 표시되는 영역)
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var imageOffsetX by remember { mutableStateOf(0f) }
    var imageOffsetY by remember { mutableStateOf(0f) }

    // 텍스트 박스의 실제 크기 (측정값)
    var textBoxSize by remember { mutableStateOf(IntSize.Zero) }

    // 텍스트 크기 추정 (textScale에 따라 동적으로 계산) - 초기값으로만 사용
    val estimatedTextWidth = 200 * textScale
    val estimatedTextHeight = 100 * textScale

    // 실제 사용할 텍스트 크기 (측정되지 않았으면 추정값 사용)
    val actualTextWidth =
        if (textBoxSize.width > 0) textBoxSize.width.toFloat() else estimatedTextWidth
    val actualTextHeight =
        if (textBoxSize.height > 0) textBoxSize.height.toFloat() else estimatedTextHeight

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
        "$amPm ${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }

    // textScale 또는 이미지 크기 또는 텍스트 박스 크기 변경 시 offset 범위 재조정
    LaunchedEffect(textScale, imageSize, textBoxSize) {
        if (imageSize.width > 0 && imageSize.height > 0) {
            val minX = imageOffsetX + actualTextWidth / 2
            val maxX = imageOffsetX + imageSize.width - actualTextWidth / 2
            val minY = imageOffsetY + actualTextHeight / 2
            val maxY = imageOffsetY + imageSize.height - actualTextHeight / 2

            // 현재 위치가 새로운 범위를 벗어나면 조정
            offsetX = offsetX.coerceIn(minX, maxX.coerceAtLeast(minX))
            offsetY = offsetY.coerceIn(minY, maxY.coerceAtLeast(minY))
        }
    }

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
            backgroundColor = Color(0xFFF8BBD0)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                        fontFamily = onePop,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "닫기",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 안내 텍스트
                Text(
                    text = "시계를 드래그하여 원하는 위치로 이동하세요",
                    fontSize = 14.sp,
                    fontFamily = onePop,
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
                        fontFamily = onePop,
                        color = Color.Black.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Slider(
                        value = textScale,
                        onValueChange = { textScale = it },
                        valueRange = 0.5f..2.0f,
                        steps = 14,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF3A1C71),
                            activeTrackColor = Color(0xFF3A1C71),
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
                        fontFamily = onePop,
                        color = Color.Black.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 색상 프리셋 버튼들 - 첫 번째 줄
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val firstRowColors = listOf(
                            Color.White,
                            Color.Black,
                            Color.Red,
                            Color(0xFFFF69B4) // 핑크
                        )

                        firstRowColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(color, CircleShape)
                                    .border(
                                        width = if (selectedColor == color) 3.dp else 1.dp,
                                        color = if (selectedColor == color) Color(0xFF3A1C71) else Color.Gray,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = color }
                                    .padding(2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 색상 프리셋 버튼들 - 두 번째 줄
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val secondRowColors = listOf(
                            Color(0xFF4169E1), // 로열 블루
                            Color(0xFFFFD700), // 골드
                            Color(0xFF32CD32), // 라임 그린
                            Color(0xFF9370DB)  // 퍼플
                        )

                        secondRowColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(color, CircleShape)
                                    .border(
                                        width = if (selectedColor == color) 3.dp else 1.dp,
                                        color = if (selectedColor == color) Color(0xFF3A1C71) else Color.Gray,
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
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .onGloballyPositioned { coordinates ->
                            containerSize = coordinates.size
                        }
                ) {
                    // 배경 이미지
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(wallpaperUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Wallpaper",
                        modifier = Modifier
                            .fillMaxSize()
                            .onGloballyPositioned { coordinates ->
                                // 이미지의 실제 크기와 위치 저장
                                imageSize = coordinates.size

                                // 이미지가 중앙 정렬되므로, 컨테이너 중심 기준으로 offset 계산
                                imageOffsetX =
                                    ((containerSize.width - imageSize.width) / 2).toFloat()
                                imageOffsetY =
                                    ((containerSize.height - imageSize.height) / 2).toFloat()

                                // 초기 위치를 이미지 중앙으로 설정
                                if (offsetX == 0f && offsetY == 0f) {
                                    offsetX = imageOffsetX + (imageSize.width / 2).toFloat()
                                    offsetY = imageOffsetY + (imageSize.height / 2).toFloat()
                                }
                            }
                    )

                    // 드래그 가능한 시계 텍스트
                    if (imageSize.width > 0 && imageSize.height > 0) {
                        // 텍스트가 이미지 영역을 벗어나지 않도록 offset 재조정
                        val validOffsetX = offsetX.coerceIn(
                            imageOffsetX + actualTextWidth / 2,
                            (imageOffsetX + imageSize.width - actualTextWidth / 2).coerceAtLeast(
                                imageOffsetX + actualTextWidth / 2
                            )
                        )
                        val validOffsetY = offsetY.coerceIn(
                            imageOffsetY + actualTextHeight / 2,
                            (imageOffsetY + imageSize.height - actualTextHeight / 2).coerceAtLeast(
                                imageOffsetY + actualTextHeight / 2
                            )
                        )

                        Box(
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        x = (validOffsetX - actualTextWidth / 2).roundToInt()
                                            .coerceIn(
                                                imageOffsetX.toInt(),
                                                (imageOffsetX + imageSize.width - actualTextWidth).roundToInt()
                                                    .coerceAtLeast(imageOffsetX.toInt())
                                            ),
                                        y = (validOffsetY - actualTextHeight / 2).roundToInt()
                                            .coerceIn(
                                                imageOffsetY.toInt(),
                                                (imageOffsetY + imageSize.height - actualTextHeight).roundToInt()
                                                    .coerceAtLeast(imageOffsetY.toInt())
                                            )
                                    )
                                }
                                .background(
                                    Color.Black.copy(alpha = 0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .pointerInput(textScale, imageSize) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        val minX = imageOffsetX + actualTextWidth / 2
                                        val maxX =
                                            imageOffsetX + imageSize.width - actualTextWidth / 2
                                        val minY = imageOffsetY + actualTextHeight / 2
                                        val maxY =
                                            imageOffsetY + imageSize.height - actualTextHeight / 2

                                        offsetX = (offsetX + dragAmount.x).coerceIn(
                                            minX,
                                            maxX.coerceAtLeast(minX)
                                        )
                                        offsetY = (offsetY + dragAmount.y).coerceIn(
                                            minY,
                                            maxY.coerceAtLeast(minY)
                                        )
                                    }
                                }
                                .onGloballyPositioned { coordinates ->
                                    textBoxSize = coordinates.size
                                }
                        ) {
                            Column {
                                Text(
                                    text = dateStr,
                                    fontSize = (14 * textScale).sp,
                                    color = selectedColor,
                                    fontFamily = onePop
                                )
                                Text(
                                    text = timeStr,
                                    fontSize = (32 * textScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = selectedColor,
                                    fontFamily = onePop
                                )
                            }
                        }
                    }

                    // 안내 아이콘 (중앙 십자선)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⊕",
                            fontSize = 40.sp,
                            color = Color.Gray.copy(alpha = 0.3f)
                        )
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
                            backgroundColor = Color.LightGray,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "취소", fontFamily = onePop)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            // 위치를 이미지 기준 0~1 사이의 비율로 변환하여 저장
                            val positionX =
                                ((offsetX - imageOffsetX) / imageSize.width.toFloat()).coerceIn(
                                    0f,
                                    1f
                                )
                            val positionY =
                                ((offsetY - imageOffsetY) / imageSize.height.toFloat()).coerceIn(
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

        // 시계 위젯인 경우 즉시 업데이트
        if (widgetType == "clock") {
            // 브로드캐스트를 통해 위젯 강제 업데이트
            Handler(getMainLooper()).postDelayed({
                val updateIntent =
                    android.content.Intent(context, GoWatchWidgetReceiver::class.java).apply {
                        action = "com.example.qwer_test_kt.UPDATE_IMAGE"
                    }
                context.sendBroadcast(updateIntent)
                Log.d("WidgetSelection", "위젯 업데이트 브로드캐스트 전송")
            }, 1000) // 1초 후 업데이트 (위젯 등록 완료 대기)
        }
    } else {
        Toast.makeText(context, "위젯 추가를 취소했거나 실패했습니다.", Toast.LENGTH_SHORT).show()
        Log.e("WidgetSelection", "위젯 추가 실패 - Component: ${providerComponent.className}")
    }
}
package com.example.qwer_test_kt.gomin.wiget.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.roundToInt

/**
 * 위젯 배치를 위한 공통 프리뷰 컴포저블
 *
 * @param wallpaperUrl 배경 이미지 URL
 * @param textScale 텍스트 크기 배율 (0.5 ~ 2.0)
 * @param estimatedTextWidth 텍스트 박스의 예상 너비
 * @param estimatedTextHeight 텍스트 박스의 예상 높이
 * @param onPositionChanged 위치 정보 변경 콜백 (offsetX, offsetY, imageSize, imageOffsetX, imageOffsetY)
 * @param content 표시할 위젯 컨텐츠
 */
@Composable
fun WidgetPositionPreview(
    wallpaperUrl: String,
    textScale: Float,
    estimatedTextWidth: Float,
    estimatedTextHeight: Float,
    initialPositionX: Float = 0.5f,
    initialPositionY: Float = 0.5f,
    onPositionChanged: (offsetX: Float, offsetY: Float, imageSize: IntSize, imageOffsetX: Float, imageOffsetY: Float) -> Unit = { _, _, _, _, _ -> },
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // 드래그 오프셋 (픽셀 단위)
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // 컨테이너 크기 (전체 Box 크기)
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    // 이미지 실제 크기
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var imageOffsetX by remember { mutableStateOf(0f) }
    var imageOffsetY by remember { mutableStateOf(0f) }

    // 텍스트 박스의 실제 크기
    var textBoxSize by remember { mutableStateOf(IntSize.Zero) }

    // 실제 사용할 텍스트 크기
    val actualTextWidth =
        if (textBoxSize.width > 0) textBoxSize.width.toFloat() else estimatedTextWidth * textScale
    val actualTextHeight =
        if (textBoxSize.height > 0) textBoxSize.height.toFloat() else estimatedTextHeight * textScale

    // textScale 또는 이미지 크기 변경 시 offset 범위 재조정
    LaunchedEffect(textScale, imageSize, textBoxSize) {
        if (imageSize.width > 0 && imageSize.height > 0) {
            val minX = imageOffsetX + actualTextWidth / 2
            val maxX = imageOffsetX + imageSize.width - actualTextWidth / 2
            val minY = imageOffsetY + actualTextHeight / 2
            val maxY = imageOffsetY + imageSize.height - actualTextHeight / 2

            offsetX = offsetX.coerceIn(minX, maxX.coerceAtLeast(minX))
            offsetY = offsetY.coerceIn(minY, maxY.coerceAtLeast(minY))

            onPositionChanged(offsetX, offsetY, imageSize, imageOffsetX, imageOffsetY)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                    imageSize = coordinates.size
                    imageOffsetX = ((containerSize.width - imageSize.width) / 2).toFloat()
                    imageOffsetY = ((containerSize.height - imageSize.height) / 2).toFloat()

                    if (offsetX == 0f && offsetY == 0f) {
                        offsetX = imageOffsetX + imageSize.width * initialPositionX
                        offsetY = imageOffsetY + imageSize.height * initialPositionY
                        onPositionChanged(offsetX, offsetY, imageSize, imageOffsetX, imageOffsetY)
                    }
                }
        )

        // 드래그 가능한 위젯 컨텐츠
        if (imageSize.width > 0 && imageSize.height > 0) {
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
                            val maxX = imageOffsetX + imageSize.width - actualTextWidth / 2
                            val minY = imageOffsetY + actualTextHeight / 2
                            val maxY = imageOffsetY + imageSize.height - actualTextHeight / 2

                            offsetX = (offsetX + dragAmount.x).coerceIn(
                                minX,
                                maxX.coerceAtLeast(minX)
                            )
                            offsetY = (offsetY + dragAmount.y).coerceIn(
                                minY,
                                maxY.coerceAtLeast(minY)
                            )

                            onPositionChanged(
                                offsetX,
                                offsetY,
                                imageSize,
                                imageOffsetX,
                                imageOffsetY
                            )
                        }
                    }
                    .onGloballyPositioned { coordinates ->
                        textBoxSize = coordinates.size
                    }
            ) {
                content()
            }
        }

        // 안내 아이콘
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
}

/**
 * 위젯 위치를 이미지 기준 비율로 변환
 *
 * @param offsetX 픽셀 기준 X 오프셋
 * @param offsetY 픽셀 기준 Y 오프셋
 * @param imageOffsetX 이미지 시작 X 위치
 * @param imageOffsetY 이미지 시작 Y 위치
 * @param imageWidth 이미지 너비
 * @param imageHeight 이미지 높이
 * @param textScale 텍스트 스케일
 * @return "x,y,scale" 형식의 문자열
 */
fun calculatePositionRatio(
    offsetX: Float,
    offsetY: Float,
    imageOffsetX: Float,
    imageOffsetY: Float,
    imageWidth: Int,
    imageHeight: Int,
    textScale: Float
): String {
    val positionX = ((offsetX - imageOffsetX) / imageWidth.toFloat()).coerceIn(0f, 1f)
    val positionY = ((offsetY - imageOffsetY) / imageHeight.toFloat()).coerceIn(0f, 1f)
    return "$positionX,$positionY,$textScale"
}

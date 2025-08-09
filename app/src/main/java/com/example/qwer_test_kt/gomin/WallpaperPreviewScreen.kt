package com.example.qwer_test_kt.gomin

import android.app.WallpaperManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.qwer_test_kt.presentation.GominJungdokViewModel

@Composable
fun WallpaperPreviewScreen(wallpaperUrls: List<String>, onWallpaperSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "배경화면을 선택해주세요",
            fontSize = 20.sp,
            fontFamily = onePop,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
        // LazyVerticalGrid를 사용해 스크롤 가능한 2x2 격자 구현
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2개의 열로 고정
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally),
            contentPadding = PaddingValues(25.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), // 세로 간격 8.dp
            horizontalArrangement = Arrangement.spacedBy(8.dp), // 세로 간격 8.dp
        ) {
            items(wallpaperUrls) { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onWallpaperSelected(url) },
                    contentScale = ContentScale.Crop,
                    onLoading = {
                        println("Loading: $url")
                    },
                    onError = {
                        println("Error loading: $url")
                    }
                )
            }
        }
    }
}


@Composable
fun WallpaperDetailScreen(
    wallpaperUrl: String,
    onBackPressed: () -> Unit,
    viewModel: GominJungdokViewModel
) {

    BackHandler {
        onBackPressed()
    }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.userMessageShown() // 메시지 표시 후 상태 초기화
        }
    }

    // 로딩 중일 때 로딩 인디케이터 표시
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 상단 뒤로가기 버튼
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기"
                )
            }
        }

        // 선택된 이미지를 크게 표시
        AsyncImage(
            model = wallpaperUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        // '배경화면 설정' 버튼
        Button(
            onClick = {
               showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "배경화면으로 설정하기", fontFamily = onePop)
        }
    }

    if (showDialog) {
        var selectedOption by remember { mutableStateOf<Int?>(null) }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = null, // title을 null로 설정하여 기본 타이틀 영역을 비웁니다.
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Spacer를 사용해 텍스트를 가운데로 밀어내고, 버튼을 오른쪽 끝에 배치합니다.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "배경화면 설정",
                            fontFamily = onePop,
                            color = Color.Black,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(
                                onClick = { showDialog = false }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "닫기",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                    // 구분선
                    Divider(
                        color = Color.Gray, thickness = 1.dp
                    )

                    // 내용 영역
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Text(
                            text = "배경화면 (홈 화면)",
                            modifier = Modifier
                                .clickable { selectedOption = WallpaperManager.FLAG_SYSTEM }
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(), // 클릭 영역 확장
                            fontFamily = onePop,
                            color = if (selectedOption == WallpaperManager.FLAG_SYSTEM) Color.Blue else Color.Black
                        )
                        Text(
                            text = "잠금화면",
                            modifier = Modifier
                                .clickable { selectedOption = WallpaperManager.FLAG_LOCK }
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(), // 클릭 영역 확장
                            fontFamily = onePop,
                            color = if (selectedOption == WallpaperManager.FLAG_LOCK) Color.Blue else Color.Black
                        )
                        Text(
                            text = "배경화면 (홈 화면) / 잠금화면",
                            modifier = Modifier
                                .clickable {
                                    selectedOption =
                                        WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                                }
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(), // 클릭 영역 확장
                            fontFamily = onePop,
                            color = if (selectedOption == WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK) Color.Blue else Color.Black
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedOption?.let { flag ->
                            viewModel.setWallpaper(wallpaperUrl, flag)
                        }
                        showDialog = false
                    },
                    enabled = selectedOption != null
                ) {
                    Text(text = "확인", fontFamily = onePop)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = "취소", fontFamily = onePop)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}


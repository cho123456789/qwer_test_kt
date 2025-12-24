package com.example.qwer_test_kt.gomin.wiget.dialog

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qwer_test_kt.gomin.wiget.screen.barry
import com.example.qwer_test_kt.presentation.PhotoWidgetViewModel

@Composable
fun ImageDetailDialog(
    imageUrl: String,
    onDismiss: () -> Unit,
    context: Context,
    viewModel: PhotoWidgetViewModel,
    navController: NavHostController
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var showWidgetSelectionDialog by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Scaffold(
            backgroundColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // 상단 닫기 버튼
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(48.dp)
                            .background(
                                Color(0xFFF5F5F5),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = Color(0xFF1565C0),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 확대된 이미지 (중앙)
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "확대된 사진",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .padding(horizontal = 5.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 하단 버튼 영역
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 배경화면 설정 버튼
                    Button(
                        onClick = {
                            viewModel.setWallpaper(
                                context = context,
                                imageUrl = imageUrl,
                                onSuccess = { onDismiss() },
                                onError = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF1565C0),
                            contentColor = Color.White
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "배경화면",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = barry
                            )
                        }
                    }

                    // 위젯 설정 버튼
                    Button(
                        onClick = {
                            showWidgetSelectionDialog = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF42A5F5),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "위젯",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = barry
                        )
                    }
                }
            }
        }
    }

    // 위젯 선택 다이얼로그
    if (showWidgetSelectionDialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WidgetSelectionDialog(
                onDismissRequest = { showWidgetSelectionDialog = false },
                onWidgetSelected = {
                    showWidgetSelectionDialog = false
                    onDismiss()
                },
                wallpaperUrl = imageUrl,
                navController = navController
            )
        }
    }
}
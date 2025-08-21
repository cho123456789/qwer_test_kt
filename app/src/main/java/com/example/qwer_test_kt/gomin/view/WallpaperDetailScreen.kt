package com.example.qwer_test_kt.gomin.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.qwer_test_kt.gomin.onePop
import com.example.qwer_test_kt.presentation.GominJungdokViewModel
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WallpaperDetailScreen(
    wallpaperUrl: String,
    onBackPressed: () -> Unit,
    viewModel: GominJungdokViewModel,
) {
    val scrollState = rememberScrollState()

    BackHandler {
        onBackPressed()
    }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

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
    var showWidgetSelectionDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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


        AsyncImage(
            model = wallpaperUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        WallpaperAndWidgetRow(
            viewModel = viewModel, // Pass the instance
            context = context,
            wallpaperUrl = wallpaperUrl,
            onSetWidgetClicked = {
                showWidgetSelectionDialog = true
            } // Pass the lambda
        )
        if (showWidgetSelectionDialog) {
            WidgetSelectionDialog(
                onDismissRequest = {
                    showWidgetSelectionDialog = false
                },
                onWidgetSelected = {
                    showWidgetSelectionDialog = false
                },
                wallpaperUrl = wallpaperUrl // 매개변수를 괄호 안에 올바르게 위치시킵니다.
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WallpaperAndWidgetRow(
    viewModel: GominJungdokViewModel,
    context: Context,
    wallpaperUrl: String,
    onSetWidgetClicked: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround, // Arranges buttons with space between them
        verticalAlignment = Alignment.CenterVertically // Aligns buttons vertically in the center
    ) {
        // "배경화면으로 설정하기" (Set as Wallpaper) Button
        Button(
            onClick = {
                viewModel.setWallpaper(context, wallpaperUrl) {
                    context.startActivity(it)
                }
            },
            // The `weight` modifier is crucial for `Row` to make the buttons share space
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF80CBC4),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "배경화면 등록",
                fontFamily = onePop, // Make sure this font family is defined
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // "위젯으로 설정하기" (Set as Widget) Button
        Button(
            onClick = onSetWidgetClicked,
            modifier = Modifier
                .weight(1f) // Gives this button equal weight to the first one
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF80CBC4),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "위젯 등록",
                fontFamily = onePop,
                fontSize = 18.sp
            )
        }
        if (showDialog) {
            WidgetSelectionDialog(
                onDismissRequest = {
                    showDialog = false // Dismiss the dialog when user clicks outside
                },
                onWidgetSelected = {
                    showDialog = false // Dismiss the dialog after a widget is selected
                },
                wallpaperUrl = wallpaperUrl
            )
        }
    }
}
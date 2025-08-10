package com.example.qwer_test_kt.gomin.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.qwer_test_kt.presentation.GominJungdokViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val bitmap = downloadBitmap(context, wallpaperUrl)
                    bitmap?.let {
                        val wallpaperFile = saveBitmapToTempFile(context, it)
                        val contentUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            wallpaperFile
                        )
                        val intent = Intent(Intent.ACTION_SET_WALLPAPER).apply {
                            addCategory(Intent.CATEGORY_DEFAULT)
                            setDataAndType(contentUri, "image/*")
                            putExtra("mimeType", "image/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(intent, "다음으로 설정"))
                    } ?: run {
                        // 다운로드 실패 시 토스트 메시지
                        launch(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "이미지를 다운로드할 수 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "배경화면으로 설정하기")
        }
    }
}

private fun saveBitmapToTempFile(context: Context, bitmap: Bitmap): File {
    val tempFile = File(context.cacheDir, "wallpaper_temp.png")
    val fos = FileOutputStream(tempFile)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    fos.flush()
    fos.close()
    return tempFile
}

private suspend fun downloadBitmap(context: Context, url: String): Bitmap? {
    val request = ImageRequest.Builder(context)
        .data(url)
        .allowHardware(false)
        .build()
    val result = context.imageLoader.execute(request)
    return (result.drawable as? BitmapDrawable)?.bitmap
}
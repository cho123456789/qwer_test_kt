package com.example.qwer_test_kt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Card
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.qwer_test_kt.gomin.onePop
import com.example.qwer_test_kt.presentation.PhotoWidgetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

// CategoryInfo 데이터 클래스
data class CategoryInfo(val name: String, val imageRes: Int)

@Composable
fun PhotoWidgetScreen(
    navController: NavHostController,
    viewModel: PhotoWidgetViewModel = hiltViewModel()
) {
    val currentImage by viewModel.currentImage.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val profiles by viewModel.profiles.collectAsStateWithLifecycle()

    // 애니메이션을 위한 scale 값
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    // 이미지 확대 다이얼로그 표시 상태
    var showImageDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // 겨울 느낌 그라데이션 배경
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE3F2FD), // 연한 겨울 하늘색
            Color(0xFFBBDEFB), // 부드러운 파란색
            Color(0xFFE1F5FE)  // 아주 연한 하늘색
        )
    )

    // 카테고리 목록과 이미지 매핑
    val categories = listOf(
        CategoryInfo("디스코드", R.drawable.discord_title),
        CategoryInfo("고민중독", R.drawable.gomin_title),
        CategoryInfo("내이름맑음", R.drawable.my_name_title),
        CategoryInfo("눈물참기", R.drawable.dear_title)
    )

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 상단 제목
                Text(
                    text = "📷 사진 위젯",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = onePop,
                    color = Color(0xFF1565C0),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 중앙 이미지 카드 (클릭 가능) - 겨울 느낌 테두리
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // 외부 테두리 (로딩 중에는 하얀색, 완료 후 눈송이 효과)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFE3F2FD),
                                        Color(0xFFBBDEFB),
                                        Color(0xFFE1F5FE)
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(5.dp)
                    ) {
                        // 중간 테두리 (로딩 중에는 연한 회색, 완료 후 흰색 눈 효과)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.9f),
                                            Color(0xFFE3F2FD).copy(alpha = 0.8f),
                                            Color.White.copy(alpha = 0.9f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(22.dp)
                                )
                                .padding(3.dp)
                        ) {
                            // 내부 파란 테두리 (로딩 중에는 회색, 완료 후 파란 얼음 효과)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF64B5F6),
                                                Color(0xFF42A5F5),
                                                Color(0xFF64B5F6)
                                            )
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(2.dp)
                            ) {
                                // 실제 이미지 카드
                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .scale(scale.value)
                                        .clickable {
                                            if (currentImage != null) {
                                                showImageDialog = true
                                            }
                                        },
                                    shape = RoundedCornerShape(18.dp),
                                    elevation = 12.dp,
                                    backgroundColor = Color.White
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (currentImage != null) {
                                            val painter = rememberAsyncImagePainter(
                                                model = ImageRequest.Builder(context)
                                                    .data(currentImage)
                                                    .crossfade(true)
                                                    .build()
                                            )

                                            androidx.compose.foundation.Image(
                                                painter = painter,
                                                contentDescription = "Selected Photo",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(18.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color(0xFFF5F5F5))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 카테고리 버튼들 (앨범 이미지 포함)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 첫 번째 행: 디스코드, 고민중독
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CategoryButtonWithImage(
                            categoryInfo = categories[0],
                            isSelected = selectedCategory == categories[0].name,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.selectRandomImage(categories[0].name)
                                coroutineScope.launch {
                                    scale.animateTo(0.9f, animationSpec = tween(100))
                                    scale.animateTo(1.1f, animationSpec = tween(100))
                                    scale.animateTo(1f, animationSpec = tween(100))
                                }
                            }
                        )
                        CategoryButtonWithImage(
                            categoryInfo = categories[1],
                            isSelected = selectedCategory == categories[1].name,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.selectRandomImage(categories[1].name)
                                coroutineScope.launch {
                                    scale.animateTo(0.9f, animationSpec = tween(100))
                                    scale.animateTo(1.1f, animationSpec = tween(100))
                                    scale.animateTo(1f, animationSpec = tween(100))
                                }
                            }
                        )
                    }

                    // 두 번째 행: 내이름맑음, 눈물참기
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CategoryButtonWithImage(
                            categoryInfo = categories[2],
                            isSelected = selectedCategory == categories[2].name,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.selectRandomImage(categories[2].name)
                                coroutineScope.launch {
                                    scale.animateTo(0.9f, animationSpec = tween(100))
                                    scale.animateTo(1.1f, animationSpec = tween(100))
                                    scale.animateTo(1f, animationSpec = tween(100))
                                }
                            }
                        )
                        CategoryButtonWithImage(
                            categoryInfo = categories[3],
                            isSelected = selectedCategory == categories[3].name,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.selectRandomImage(categories[3].name)
                                coroutineScope.launch {
                                    scale.animateTo(0.9f, animationSpec = tween(100))
                                    scale.animateTo(1.1f, animationSpec = tween(100))
                                    scale.animateTo(1f, animationSpec = tween(100))
                                }
                            }
                        )
                    }
                }
            }
        }

        // 이미지 확대 다이얼로그
        if (showImageDialog && currentImage != null) {
            ImageDetailDialog(
                imageUrl = currentImage!!,
                onDismiss = { showImageDialog = false },
                context = context
            )
        }
    }
}

@Composable
fun ImageDetailDialog(
    imageUrl: String,
    onDismiss: () -> Unit,
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

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
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    // 이미지 다운로드 및 저장
                                    val contentUri = downloadAndSaveImage(context, imageUrl)

                                    // 배경화면 및 스타일 설정 화면으로 이동하는 Intent
                                    val intent = Intent(Intent.ACTION_SET_WALLPAPER).apply {
                                        setDataAndType(contentUri, "image/*")
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        putExtra("mimeType", "image/*")
                                    }

                                    withContext(Dispatchers.Main) {
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // ACTION_SET_WALLPAPER가 실패하면 대체 방법 사용
                                            val chooserIntent = Intent.createChooser(
                                                Intent(Intent.ACTION_ATTACH_DATA).apply {
                                                    addCategory(Intent.CATEGORY_DEFAULT)
                                                    setDataAndType(contentUri, "image/*")
                                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                    putExtra("mimeType", "image/*")
                                                },
                                                "배경화면 설정"
                                            )
                                            context.startActivity(chooserIntent)
                                        }
                                        onDismiss()
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "설정 실패: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } finally {
                                    isLoading = false
                                }
                            }
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
                                fontFamily = onePop
                            )
                        }
                    }

                    // 위젯 설정 버튼
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "위젯 설정 기능은 준비 중입니다! 🔧",
                                Toast.LENGTH_SHORT
                            ).show()
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
                            fontFamily = onePop
                        )
                    }
                }
            }
        }
    }
}

// 이미지 다운로드 및 파일 저장 함수
suspend fun downloadAndSaveImage(context: Context, imageUrl: String): Uri {
    return withContext(Dispatchers.IO) {
        try {
            // 이미지 다운로드
            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            // 파일 저장
            val imagesDir = File(context.cacheDir, "images")
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }

            val imageFile = File(imagesDir, "wallpaper_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // FileProvider를 통해 Uri 생성
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
        } catch (e: Exception) {
            throw e
        }
    }
}

@Composable
fun CategoryButtonWithImage(
    categoryInfo: CategoryInfo,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = if (isSelected) 12.dp else 6.dp,
        backgroundColor = Color.White
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isSelected) Color(0xFFE3F2FD) else Color.White,
                contentColor = Color(0xFF1565C0)
            ),
            contentPadding = PaddingValues(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                // 앨범 이미지
                androidx.compose.foundation.Image(
                    painter = painterResource(id = categoryInfo.imageRes),
                    contentDescription = categoryInfo.name,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .then(
                            if (isSelected) {
                                Modifier.border(
                                    3.dp,
                                    Color(0xFF1565C0),
                                    RoundedCornerShape(8.dp)
                                )
                            } else {
                                Modifier
                            }
                        ),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 카테고리 이름
                Text(
                    text = categoryInfo.name,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontFamily = onePop,
                    color = if (isSelected) Color(0xFF0D47A1) else Color(0xFF1565C0),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {

}
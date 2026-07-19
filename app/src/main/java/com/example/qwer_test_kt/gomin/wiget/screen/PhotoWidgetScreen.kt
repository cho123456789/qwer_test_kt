package com.example.qwer_test_kt.gomin.wiget.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.gomin.wiget.dialog.ImageDetailDialog
import com.example.qwer_test_kt.presentation.PhotoWidgetViewModel


data class CategoryInfo(
    val name: String,
    val queryName: String,
    val imageRes: Int,
    val releaseDate: String,
    val isNew: Boolean = false
)

@Composable
fun PhotoWidgetScreen(
    navController: NavHostController,
    viewModel: PhotoWidgetViewModel = hiltViewModel()
) {
    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            // Back button at the top left
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE3F2FD),
                                Color(0xFFBBDEFB),
                                Color(0xFFE1F5FE)
                            )
                        )
                    )
                    .padding(top = 40.dp, start = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        // Navigate back to Gominjungdok page
                        navController.navigate("gominjungdok") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE3F2FD), // 연한 겨울 하늘색
                            Color(0xFFBBDEFB), // 부드러운 파란색
                            Color(0xFFE1F5FE)  // 아주 연한 하늘색
                        )
                    )
                )
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            PhotoWidgetContent(navController, viewModel)
        }
    }
}


@Composable
fun PhotoWidgetContent(
    navController: NavHostController,
    viewModel: PhotoWidgetViewModel = hiltViewModel()
) {
    val currentImage by viewModel.currentImage.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    var showImageDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // 카테고리 목록과 이미지 매핑
    val categories = listOf(
        CategoryInfo("CEREMONY", "CEREMONY", R.drawable.cermonery, "2026.04.27", isNew = true),
        CategoryInfo("눈물참기", "눈물참기", R.drawable.dear_title, "2025.06.09"),
        CategoryInfo("내이름 맑음", "내이름맑음", R.drawable.my_name_title, "2024.09.23"),
        CategoryInfo("고민중독", "고민중독", R.drawable.gomin_title, "2024.04.01"),
        CategoryInfo("디스코드", "디스코드", R.drawable.discord_title, "2023.10.18")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 중앙 이미지 (클릭 가능) - 겨울 느낌 테두리
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            // 외부 테두리 (로딩 중에는 하얀색, 완료 후 눈송이 효과)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFE3F2FD),
                                Color(0xFFBBDEFB),
                                Color(0xFFE1F5FE)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(4.dp)
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
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(2.dp)
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
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(2.dp)
                    ) {
                        // 실제 이미지
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                        ) {
                            if (currentImage != null) {
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(currentImage)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Selected Photo",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop,
                                    loading = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFFE3F2FD)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                color = Color(0xFF1565C0),
                                                modifier = Modifier.size(50.dp),
                                                strokeWidth = 4.dp
                                            )
                                        }
                                    },
                                    error = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFFF5F5F5)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "이미지 로드 실패",
                                                fontSize = 14.sp,
                                                color = Color.Gray,
                                                fontFamily = barry
                                            )
                                        }
                                    }
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFFF5F5F5))
                                )
                            }

                            // Magnifying glass (zoom) button with widget registration text at the bottom right corner
                            if (currentImage != null) {
                                Card(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                        .clickable { showImageDialog = true },
                                    shape = RoundedCornerShape(20.dp),
                                    backgroundColor = Color.White.copy(alpha = 0.9f),
                                    elevation = 4.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 6.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "확대 및 위젯 등록",
                                            tint = Color(0xFF1565C0),
                                            modifier = Modifier.size(18.dp)
                                        )

                                        Spacer(modifier = Modifier.size(4.dp))

                                        Text(
                                            text = "위젯 등록",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = barry,
                                            color = Color(0xFF1565C0)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 안내 문구
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color.White.copy(alpha = 0.9f),
            elevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎲",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "각 컨셉을 누르면 랜덤 이미지가 표시됩니다",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = barry,
                    color = Color(0xFF1565C0),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 앨범 커버를 활용한 카페 메뉴
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 5.dp,
            backgroundColor = Color.White.copy(alpha = 0.96f)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🌊 🐚",
                        modifier = Modifier.align(Alignment.CenterStart),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Album Menu",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = barry,
                        color = Color(0xFF0D47A1)
                    )
                    Text(
                        text = "☀️ 🏖️",
                        modifier = Modifier.align(Alignment.CenterEnd),
                        fontSize = 20.sp
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFBBDEFB))
                )
                categories.forEach { category ->
                    AlbumMenuItem(
                        categoryInfo = category,
                        isSelected = selectedCategory == category.queryName,
                        onClick = { viewModel.selectRandomImage(category.queryName) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp)) // Add padding at the bottom
    }

    // 이미지 확대 다이얼로그
    if (showImageDialog && currentImage != null) {
        ImageDetailDialog(
            imageUrl = currentImage!!,
            onDismiss = { showImageDialog = false },
            context = context,
            viewModel = viewModel,
            navController = navController
        )
    }
}

@Composable
fun AlbumMenuItem(
    categoryInfo: CategoryInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(categoryInfo.imageRes),
            contentDescription = "${categoryInfo.name} 앨범 커버",
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = categoryInfo.name,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontWeight = if (isSelected || categoryInfo.isNew) FontWeight.Bold else FontWeight.Medium,
            fontFamily = barry,
            color = if (isSelected) Color(0xFF0D47A1) else Color(0xFF1565C0),
            maxLines = 1
        )
        if (categoryInfo.isNew) {
            Text(
                text = "NEW",
                modifier = Modifier
                    .background(Color(0xFFFFD54F), RoundedCornerShape(8.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                color = Color(0xFF5D4300),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
        Text(
            text = categoryInfo.releaseDate,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF78909C)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {

}

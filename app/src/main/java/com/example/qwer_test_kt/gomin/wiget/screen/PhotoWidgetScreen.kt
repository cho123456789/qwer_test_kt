package com.example.qwer_test_kt.gomin.wiget.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.gomin.wiget.dialog.ImageDetailDialog
import com.example.qwer_test_kt.presentation.PhotoWidgetViewModel


data class CategoryInfo(val name: String, val imageRes: Int)

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
    val imageScale by viewModel.imageScale.collectAsStateWithLifecycle()
    var showImageDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // 카테고리 목록과 이미지 매핑
    val categories = listOf(
        CategoryInfo("디스코드", R.drawable.discord_title),
        CategoryInfo("고민중독", R.drawable.gomin_title),
        CategoryInfo("내이름맑음", R.drawable.my_name_title),
        CategoryInfo("눈물참기", R.drawable.dear_title)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 중앙 이미지 카드 (클릭 가능) - 겨울 느낌 테두리
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
                    .height(320.dp)
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
                        // 실제 이미지 카드
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(imageScale)
                                .clickable {
                                    if (currentImage != null) {
                                        showImageDialog = true
                                    }
                                },
                            shape = RoundedCornerShape(14.dp),
                            elevation = 8.dp,
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

                                    Image(
                                        painter = painter,
                                        contentDescription = "Selected Photo",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(14.dp)),
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

        Spacer(modifier = Modifier.height(12.dp))

        // 카테고리 버튼들 (앨범 이미지 포함)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 첫 번째 행: 디스코드, 고민중독
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CategoryButtonWithImage(
                    categoryInfo = categories[0],
                    isSelected = selectedCategory == categories[0].name,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.selectRandomImage(categories[0].name)
                    }
                )
                CategoryButtonWithImage(
                    categoryInfo = categories[1],
                    isSelected = selectedCategory == categories[1].name,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.selectRandomImage(categories[1].name)
                    }
                )
            }

            // 두 번째 행: 내이름맑음, 눈물참기
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CategoryButtonWithImage(
                    categoryInfo = categories[2],
                    isSelected = selectedCategory == categories[2].name,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.selectRandomImage(categories[2].name)
                    }
                )
                CategoryButtonWithImage(
                    categoryInfo = categories[3],
                    isSelected = selectedCategory == categories[3].name,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.selectRandomImage(categories[3].name)
                    }
                )
            }
        }
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
fun CategoryButtonWithImage(
    categoryInfo: CategoryInfo,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = if (isSelected) 8.dp else 4.dp,
        backgroundColor = Color.White
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isSelected) Color(0xFFE3F2FD) else Color.White,
                contentColor = Color(0xFF1565C0)
            ),
            contentPadding = PaddingValues(6.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                // 앨범 이미지
                Image(
                    painter = painterResource(id = categoryInfo.imageRes),
                    contentDescription = categoryInfo.name,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .then(
                            if (isSelected) {
                                Modifier.border(
                                    2.dp,
                                    Color(0xFF1565C0),
                                    RoundedCornerShape(6.dp)
                                )
                            } else {
                                Modifier
                            }
                        ),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 카테고리 이름
                Text(
                    text = categoryInfo.name,
                    fontSize = 11.sp,
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
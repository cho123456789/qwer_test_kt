package com.example.qwer_test_kt.gomin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun WallpaperPreviewScreen(wallpaperUrls: List<String>, onWallpaperSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
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
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(wallpaperUrls) { url ->
                Image(
                    painter = rememberAsyncImagePainter(model = url),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onWallpaperSelected(url) },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun WallpaperDetailScreen(wallpaperUrl: String, onBackPressed: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 선택된 이미지를 크게 표시
        Image(
            painter = rememberAsyncImagePainter(model = wallpaperUrl),
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
            onClick = { /* TODO: 배경화면 설정 로직 구현 */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "배경화면으로 설정하기", fontFamily = onePop)
        }
    }
}

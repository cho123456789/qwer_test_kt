package com.example.qwer_test_kt.gomin.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.qwer_test_kt.gomin.onePop

@Composable
fun WallpaperPreviewScreen(wallpaperUrls: List<String>, onWallpaperSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.
            fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "배경화면을 선택해주세요",
            fontSize = 30.sp,
            fontFamily = onePop,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        // LazyVerticalGrid를 사용해 스크롤 가능한 2x2 격자 구현
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2개의 열로 고정
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally),
            contentPadding = PaddingValues(10.dp),
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
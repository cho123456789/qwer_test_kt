package com.example.qwer_test_kt.gomin

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qwer_test_kt.R


val cafe24 = FontFamily(Font(R.font.cafe24decoshadow))
val onePop = FontFamily(Font(R.font.onepop))

@Composable
fun GominjungdokScreen(navController: NavHostController) {
    var selectedNavIndex by remember { mutableStateOf(0) }
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA), // 더 밝고 화사한 하늘색
            Color(0xFFE1BEE7)  // 더 밝은 연보라색
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MemberProfile()
        // 상단 슬라이더
        Column(
            modifier = Modifier
                .weight(1f) // 핵심! 하단 네비게이션을 제외한 모든 공간을 차지
                .fillMaxSize()
        ) {
            when (selectedNavIndex) {
                0 -> WallpaperScreen() // '배경화면' 탭일 때의 화면
                1 -> WidgetScreen()    // '위젯' 탭일 때의 화면
                2 -> IconScreen()      // '아이콘' 탭일 때의 화면
            }
        }
        BottomNavigationBar(
            selectedIndex = selectedNavIndex,
            onItemSelected = { index -> selectedNavIndex = index }
        )
    }
}

@Composable
fun MemberProfile() {
    val albumList = listOf(
        Album(title = "1st Mini Album 'MANITO'", imageResId = R.drawable.mani4, Color.Blue),
        Album(title = "1st Mini Album 'MANITO'", imageResId = R.drawable.go, Color.Black)
    )
    AlbumPhoto(albumList)
}

@Composable
fun AlbumPhoto(album: List<Album>) {
    var pageState = rememberPagerState(initialPage = 0) {
        album.size
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = album[pageState.currentPage].title,
            fontSize = 25.sp,
            fontFamily = cafe24,
            color = album[pageState.currentPage].textColor,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )
        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            val album = album[page]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)), // 반투명 흰색
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 위젯 배경 이미지
                    Image(
                        painter = painterResource(id = album.imageResId), // 멤버 그룹 사진
                        contentDescription = "QWER Member Group Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(24.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val navItems = listOf(
        Pair(R.drawable.discord, "배경화면"),
        Pair(R.drawable.qwer2, "위젯"),
        Pair(R.drawable.dear, "아이콘")
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White.copy(alpha = 0.8f)), // 반투명 흰색
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationItem(
                iconResId = item.first,
                text = item.second,
                isSelected = index == selectedIndex, // 현재 아이템이 선택되었는지 전달
                onItemSelected = { onItemSelected(index) } // 클릭 시 인덱스 반환
            )
        }
    }
}

// 하단 내비게이션 아이템
@Composable
fun NavigationItem(iconResId: Int, text: String, isSelected: Boolean, onItemSelected: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(60.dp)
            .clickable(onClick = onItemSelected)
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit,
            colorFilter = if (isSelected) ColorFilter.tint(Color.Blue) else null
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isSelected) Color.Blue else Color.Black
        )
    }
}

data class Album(
    val title: String,
    val imageResId: Int,
    val textColor: Color
)


package com.example.qwer_test_kt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun GominjungdokScreen(navController: NavHostController) {

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
        // 상단: 멤버 소개 카드뷰
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MemberProfileCard()

            Spacer(modifier = Modifier.height(16.dp))

            PreviewImageSection()
        }
        BottomNavigationBar()
    }
}

@Composable
fun MemberProfileCard() {

    val cafe24 = FontFamily(Font(R.font.cafe24decoshadow))
    val onePop = FontFamily(Font(R.font.onepop))

    Column(
        Modifier.padding(10.dp)
    ) {
        Text(
            text = "1st Mini Album ‘MANITO'", // 가사 또는 문구
            fontSize = 25.sp,
            fontFamily = cafe24,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
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
                painter = painterResource(id = R.drawable.mani4), // 멤버 그룹 사진
                contentDescription = "QWER Member Group Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
            )
        }
    }
}
@Composable
fun PreviewImageSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)), // 반투명 흰색
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.qwer2), // 미리보기 이미지
            contentDescription = "Preview Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop
        )
    }
}
@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White.copy(alpha = 0.8f)), // 반투명 흰색
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            iconResId = R.drawable.discord, // 아이콘 리소스 ID 교체
            text = "배경화면"
        )
        NavigationItem(
            iconResId = R.drawable.qwer2, // 아이콘 리소스 ID 교체
            text = "위젯"
        )
        NavigationItem(
            iconResId = R.drawable.dear, // 아이콘 리소스 ID 교체
            text = "아이콘"
        )
    }
}
// 하단 내비게이션 아이템
@Composable
fun NavigationItem(iconResId: Int, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(60.dp)
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}



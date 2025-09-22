package com.example.qwer_test_kt.gomin.view

import SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.qwer_test_kt.R // This is the corrected import


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdolProfileScreenWithScaffold(navController: NavController) {
    // Scaffold를 사용하여 앱의 기본 구조를 정의합니다.
    Scaffold(
        topBar = {
            // topBar 슬롯에 TopAppBar 컴포넌트를 제공
            TopAppBar(
                title = { Text("QWER 프로필") },
            )
        }
    ) { paddingValues ->
        // content 슬롯에 실제 화면 콘텐츠를 넣습니다.
        // Scaffold가 제공하는 여백(paddingValues)을 modifier에 적용하여
        // UI가 TopAppBar 아래에 위치하도록 합니다.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // 여기서 Scaffold의 여백을 적용
                .background(Color.White)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 그룹 이미지
            Image(
                painter = painterResource(id = R.drawable.main5),
                contentDescription = "QWER",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )

            // 여백
            Spacer(modifier = Modifier.height(32.dp))

            // 멤버 목록
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MemberProfile("쵸단", "드럼", R.drawable.gomin_cho_profile)
                MemberProfile("마젠타", "베이스", R.drawable.gomin_ma_profile)
                MemberProfile("히나", "기타", R.drawable.gomin_hina_profile)
                MemberProfile("시연", "리드 보컬", R.drawable.gomin_siyeon_profile)
            }
        }
    }
}

// 기존 MemberProfile 함수는 그대로 사용
@Composable
fun MemberProfile(name: String, position: String, imageId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "$name 프로필 이미지",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(text = name, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Text(text = position, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IdolProfileScreenWithScaffoldPreview() {
    IdolProfileScreenWithScaffold(navController = NavHostController(LocalContext.current))
}
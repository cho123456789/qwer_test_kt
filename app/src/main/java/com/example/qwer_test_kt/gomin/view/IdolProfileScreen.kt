package com.example.qwer_test_kt.gomin.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.gomin.onePop
import com.example.qwer_test_kt.gomin.view.model.MemberInfo
import com.example.qwer_test_kt.gomin.view.model.members


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdolProfileScreenWithScaffold(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("QWER 프로필", fontFamily = onePop)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        var selectedMember by remember { mutableStateOf<MemberInfo?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                members.forEach { member ->
                    MemberProfile(member) {
                        selectedMember = if (selectedMember == member) null else member
                    }
                    if (selectedMember == member) {
                        Spacer(modifier = Modifier.height(8.dp))
                        MemberDetails(member)
                    }
                }
            }
        }
    }
}

@Composable
fun MemberProfile(memberInfo: MemberInfo, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = memberInfo.image),
            contentDescription = "$memberInfo.name 프로필 이미지",
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
            Text(
                text = memberInfo.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                fontFamily = onePop
            )
            Text(
                text = memberInfo.position,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = onePop
            )
        }
    }
}

@Composable
fun MemberDetails(memberInfo: MemberInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0F7FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = memberInfo.name,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            fontFamily = onePop,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = memberInfo.description,
            fontSize = 14.sp,
            fontFamily = onePop,
            textAlign = TextAlign.Center,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun IdolProfileScreenWithScaffoldPreview() {
    IdolProfileScreenWithScaffold(navController = NavHostController(LocalContext.current))
}
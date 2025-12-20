package com.example.qwer_test_kt.gomin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.domin.model.MemberDetail
import com.example.qwer_test_kt.gomin.wiget.PhotoWidgetContent
import com.example.qwer_test_kt.presentation.GominJungdokViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

val cafe24 = FontFamily(Font(R.font.cafe24decoshadow))
val onePop = FontFamily(Font(R.font.onepop))
val barry = FontFamily(Font(R.font.barry))

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GominjungdokScreen(
    navController: NavHostController,
    viewModel: GominJungdokViewModel = hiltViewModel()
) {
    val memberDetails by viewModel.memberDetails.collectAsStateWithLifecycle()
    
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA), // 더 밝고 화사한 하늘색
            Color(0xFFE1BEE7)  // 더 밝은 연보라색
        )
    )

    val pagerState = rememberPagerState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                ) // StatusBar 높이만큼 패딩
                .padding(5.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // 멤버 프로필 슬라이드
            if (memberDetails.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 화면 크기에 따라 카드 크기 계산
                        val screenWidth = this.maxWidth
                        val cardWidth = screenWidth.coerceAtMost(600.dp)
                        val cardHeight =
                            (cardWidth * 0.5f).coerceAtLeast(160.dp).coerceAtMost(200.dp)

                        HorizontalPager(
                            count = memberDetails.size,
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(cardHeight)
                        ) { page ->
                            val memberDetail = memberDetails[page]
                            MemberProfileCard(memberDetail = memberDetail)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 페이지 인디케이터 (간단한 점 표시)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(memberDetails.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pagerState.currentPage == index)
                                            Color(0xFFC2185B)
                                        else
                                            Color.LightGray
                                    )
                            )
                        }
                    }
                }
            } else {
                // 로딩 중이거나 데이터가 없을 때 표시
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "멤버 정보를 불러오는 중...",
                        fontSize = 16.sp,
                        fontFamily = onePop,
                        color = Color(0xFF666666)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // PhotoWidgetScreen을 바로 표시
            PhotoWidgetContent(navController)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MemberProfileCard(memberDetail: MemberDetail) {
    // 닉네임에 따른 QWER 알파벳과 색상 매핑
    val (letter, letterColor) = when (memberDetail.nickname) {
        "쵸단" -> "Q" to Color(0xFF000000)  // 검정
        "마젠타" -> "W" to Color(0xFFFFC0CB)  // 핑크
        "히나" -> "E" to Color(0xFF00B0FF)  // 파랑
        "시연" -> "R" to Color(0xFF8BC34A)  // 초록
        else -> "" to Color.Black
    }

    // 겨울 느낌 그라데이션 배경
    val winterGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF0F8FF),  // 앨리스 블루 (밝은 하늘색)
            Color(0xFFE6F3FF),  // 연한 파란색
            Color(0xFFFFFFFF)   // 흰색
        )
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        // 화면 크기에 따라 카드 너비 조정: 최대 600dp, 그 이하는 화면에 맞춤
        val cardWidth = this.maxWidth.coerceAtMost(600.dp)
        // 카드 높이는 너비의 50%로 설정하되, 최소 160dp, 최대 200dp
        val cardHeight = (cardWidth * 0.5f).coerceAtLeast(160.dp).coerceAtMost(200.dp)

        Card(
            modifier = Modifier
                .width(cardWidth)
                .height(cardHeight)
                .align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,
            backgroundColor = Color(0xFFF0F8FF)  // 앨리스 블루
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(winterGradient)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // 멤버 사진
                    AsyncImage(
                        model = memberDetail.profileImg,
                        contentDescription = memberDetail.name,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.45f)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // 멤버 정보
                    Column(
                        modifier = Modifier.weight(0.55f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // 닉네임과 QWER 알파벳을 Row로 배치
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = memberDetail.nickname,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = barry,
                                color = Color(0xFF1E3A8A)  // 진한 겨울 파란색
                            )

                            if (letter.isNotEmpty()) {
                                Text(
                                    text = letter,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = barry,
                                    color = letterColor
                                )
                            }
                        }

                        Text(
                            text = "👤 ${memberDetail.name}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = barry,
                            color = Color(0xFF64748B)  // 슬레이트 그레이
                        )

                        Text(
                            text = "🎸 ${memberDetail.position}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = barry,
                            color = Color(0xFF475569)  // 어두운 슬레이트
                        )

                        Text(
                            text = "🎂 ${memberDetail.birthday}",
                            fontSize = 12.sp,
                            fontFamily = barry,
                            color = Color(0xFF64748B)
                        )

                        Text(
                            text = "✨ ${memberDetail.mbti}",
                            fontSize = 12.sp,
                            fontFamily = barry,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}
package com.example.qwer_test_kt.gomin

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.Route
import com.example.qwer_test_kt.gomin.view.WallpaperListScreen
import com.example.qwer_test_kt.member.Member
import com.example.qwer_test_kt.member.loadMembersFromAssets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState


val cafe24 = FontFamily(Font(R.font.cafe24decoshadow))
val onePop = FontFamily(Font(R.font.onepop))
val sam = FontFamily(Font(R.font.samliphop))

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GominjungdokScreen(navController: NavHostController) {
    val context = LocalContext.current
    var members by remember { mutableStateOf<List<Member>>(emptyList()) }
    var filteredWallpapers by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        members = loadMembersFromAssets(context)
        filteredWallpapers = members.flatMap { it.wallPaperImageUrls }
    }

    fun filterByMember(memberName: String) {
        filteredWallpapers = if (memberName == "전체") {
            members.flatMap { it.wallPaperImageUrls }
        } else {
            // wallPaperImageUrls는 List<String>
            members.find { it.name == memberName }?.wallPaperImageUrls ?: emptyList()
        }
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA), // 더 밝고 화사한 하늘색
            Color(0xFFE1BEE7)  // 더 밝은 연보라색
        )
    )
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            LazyRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // "전체" 버튼
                item {
                    MemberFilterButton(name = "전체") {
                        filterByMember("전체")
                    }
                }
                // 각 멤버 버튼
                items(members) { member ->
                    MemberFilterButton(name = member.name) {
                        filterByMember(member.name)
                    }
                }
            }

            val qwerAnnotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFFFFFFFF))) {
                    append("Q")
                }
                withStyle(style = SpanStyle(color = Color(0xFFFFC0CB))) {
                    append("W")
                }
                withStyle(style = SpanStyle(color = Color(0xFF00B0FF))) {
                    append("E")
                }
                withStyle(style = SpanStyle(color = Color(0xFF8BC34A))) {
                    append("R")
                }
            }

            Text(
                text = qwerAnnotatedString, // 여기에 AnnotatedString 객체를 전달합니다.
                fontSize = 75.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = cafe24,
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier.padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "최애 ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800),
                    fontFamily = onePop,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "사진을 골라주세요",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    fontFamily = onePop,
                    textAlign = TextAlign.Center
                )
            }
            WallpaperListScreen(
                allWallpapers = filteredWallpapers,
                onWallpaperClick = { wallpaperUrl ->
                    val encodedUrl = Uri.encode(wallpaperUrl)
                    navController.navigate("${Route.Gomin_detail}/$encodedUrl")
                }
            )
        }
    }
}

@Composable
fun MemberFilterButton(name: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        )
    ) {
        Text(text = name, fontFamily = onePop)
    }
}
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.Route
import com.example.qwer_test_kt.gomin.onePop
import com.example.qwer_test_kt.presentation.SplashViewModel


@Composable
fun SplashScreen(navController: NavHostController, viewModel: SplashViewModel = hiltViewModel()) {
    val barry = FontFamily(Font(R.font.barry))
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF5F5DC), // 옅은 베이지색
            Color(0xFFFFF0F5)  // 옅은 복숭아색
        )
    )

    // 프로필 타입 목록
    val profileTypes = listOf("디스코드", "고민중독", "내이름맑음", "눈물참기")

    // 랜덤 프로필 타입 선택
    val randomProfileType = remember { profileTypes.random() }

    val profilesState by viewModel.profiles.collectAsStateWithLifecycle()

    val commonFontSize = 70.sp
    val commonFontWeight = FontWeight.Bold

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // QWER 텍스트에 애니메이션 적용
            val qwerAnnotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF000000))) { append("Q") }
                withStyle(style = SpanStyle(color = Color(0xFFFFC0CB))) { append("W") }
                withStyle(style = SpanStyle(color = Color(0xFF00B0FF))) { append("E") }
                withStyle(style = SpanStyle(color = Color(0xFF8BC34A))) { append("R") }
            }

            // 🌟 겹치기 위한 Box: QWER 외곽선 구현 🌟
            Box(
                modifier = Modifier.padding(bottom = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                // 하얀색 테두리 효과를 위한 여러 방향의 그림자
                val strokeWidth = 8.dp.value

                // 더 많은 방향으로 테두리 생성 (8방향 -> 16방향)
                val offsets = buildList {
                    for (angle in 0 until 360 step 22) {
                        val radian = Math.toRadians(angle.toDouble())
                        add(
                            Offset(
                                (strokeWidth * kotlin.math.cos(radian)).toFloat(),
                                (strokeWidth * kotlin.math.sin(radian)).toFloat()
                            )
                        )
                    }
                }

                // 각 방향으로 하얀색 그림자를 그려서 테두리 효과 생성
                offsets.forEach { offset ->
                    Text(
                        text = "QWER",
                        fontSize = commonFontSize,
                        fontWeight = commonFontWeight,
                        fontFamily = barry,
                        color = Color.White,
                        letterSpacing = 8.sp,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.White,
                                offset = offset,
                                blurRadius = 0f
                            )
                        )
                    )
                }

                // 안쪽 배경 색상 텍스트 (각 글자마다 다른 색상)
                Text(
                    text = qwerAnnotatedString,
                    fontSize = commonFontSize,
                    fontWeight = commonFontWeight,
                    fontFamily = barry,
                    letterSpacing = 8.sp
                )
            }

            // 2x2 사진 그리드 - Supabase의 이미지 사용 (랜덤 프로필 타입)
            if (profilesState == null ) {
                // 로딩 중
                Box(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // 랜덤으로 선택된 프로필 타입 가져오기
                val gominProfile = profilesState?.find { it.typeName == "고민중독" }
                val memberNames = listOf("쵸단", "마젠타", "히나", "시연" )

                Column(
                    modifier = Modifier.padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 쵸단
                        gominProfile?.members?.get(memberNames[0])?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = memberNames[0],
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        // 마젠타
                        gominProfile?.members?.get(memberNames[1])?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = memberNames[1],
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 히나
                        gominProfile?.members?.get(memberNames[2])?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = memberNames[2],
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        // 시연
                        gominProfile?.members?.get(memberNames[3])?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = memberNames[3],
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Photo 텍스트에 하얀색 테두리 적용 (이모지 제외)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "♪",
                        fontSize = 35.sp,
                        color = Color(0xFFFF6B35),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        val strokeWidth = 6.dp.value
                        val offsets = buildList {
                            for (angle in 0 until 360 step 22) {
                                val radian = Math.toRadians(angle.toDouble())
                                add(
                                    Offset(
                                        (strokeWidth * kotlin.math.cos(radian)).toFloat(),
                                        (strokeWidth * kotlin.math.sin(radian)).toFloat()
                                    )
                                )
                            }
                        }

                        // 각 방향으로 하얀색 그림자를 그려서 테두리 효과 생성
                        offsets.forEach { offset ->
                            Text(
                                text = "Photo",
                                fontSize = 50.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontFamily = barry,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.White,
                                        offset = offset,
                                        blurRadius = 0f
                                    )
                                )
                            )
                        }

                        // 원본 색상의 Photo 텍스트
                        Text(
                            text = "Photo",
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2185B),
                            textAlign = TextAlign.Center,
                            fontFamily = barry,
                        )
                    }
                    Text(
                        text = "★",
                        fontSize = 35.sp,
                        color = Color(0xFFFFC107),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Widget 텍스트에 하얀색 테두리 적용 (이모지 제외)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "✦",
                        fontSize = 35.sp,
                        color = Color(0xFF9C27B0),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        val strokeWidth = 6.dp.value
                        val offsets = buildList {
                            for (angle in 0 until 360 step 22) {
                                val radian = Math.toRadians(angle.toDouble())
                                add(
                                    Offset(
                                        (strokeWidth * kotlin.math.cos(radian)).toFloat(),
                                        (strokeWidth * kotlin.math.sin(radian)).toFloat()
                                    )
                                )
                            }
                        }

                        // 각 방향으로 하얀색 그림자를 그려서 테두리 효과 생성
                        offsets.forEach { offset ->
                            Text(
                                text = "Widget",
                                fontSize = 50.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontFamily = barry,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.White,
                                        offset = offset,
                                        blurRadius = 0f
                                    )
                                )
                            )
                        }

                        // 원본 색상의 Widget 텍스트
                        Text(
                            text = "Widget",
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2185B),
                            textAlign = TextAlign.Center,
                            fontFamily = barry,
                        )
                    }
                    Text(
                        text = "♫",
                        fontSize = 35.sp,
                        color = Color(0xFF00BCD4),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = {
                    navController.navigate(Route.Gominjungdok) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFC2185B),
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "📢",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "입장하기",
                        fontSize = 20.sp,
                        fontFamily = onePop,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    SplashScreen(navController = NavHostController(LocalContext.current))
}
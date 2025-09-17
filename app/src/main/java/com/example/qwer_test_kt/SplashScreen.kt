import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.Route
import com.example.qwer_test_kt.gomin.onePop
import kotlinx.coroutines.delay

data class Song(val title: String, val artist: String)

@Composable
fun SplashScreen(navController: NavHostController) {
    val cafe24 = FontFamily(Font(R.font.cafe24decoshadow))

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF5F5DC), // 옅은 베이지색
            Color(0xFFFFF0F5)  // 옅은 복숭아색
        )
    )

    val songList = listOf(
        Song(title = "Discord", artist = "QWER"),
        Song(title = "고민중독", artist = "QWER"),
        Song(title = "가짜아이돌", artist = "QWER"),
        Song(title = "눈물참기", artist = "QWER")
    )

    // 오늘의 추천곡 상태
    val randomSong = remember { songList.random() }


    // 진행률을 추적하는 상태 변수
    var progress by remember { mutableStateOf(0f) }

    // 진행률 값을 애니메이션 처리
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 2000, // 2초 동안 애니메이션
            easing = LinearEasing
        ),
        label = "loadingProgress"
    )

    // 진행률을 업데이트하고 화면을 전환하기 위한 효과 실행
    LaunchedEffect(key1 = true) {
        // 진행률을 0에서 1로 시작
        progress = 1f
        // 애니메이션이 끝날 때까지 기다림
        delay(2000)
        // 메인 화면으로 이동
        navController.navigate(Route.Gominjungdok) {
            // 스플래시 화면을 백 스택에서 제거
            popUpTo(Route.Splash) {
                inclusive = true
            }
        }
    }

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
                withStyle(style = SpanStyle(color = Color(0xFFFFFFFF))) { append("Q") }
                withStyle(style = SpanStyle(color = Color(0xFFFFC0CB))) { append("W") }
                withStyle(style = SpanStyle(color = Color(0xFF00B0FF))) { append("E") }
                withStyle(style = SpanStyle(color = Color(0xFF8BC34A))) { append("R") }
            }

            Text(
                text = qwerAnnotatedString,
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = cafe24,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Photo Widget 텍스트
            Text(
                text = "Photo\nWidget",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontFamily = cafe24,
            )

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "오늘의 추천곡",
                fontSize = 25.sp,
                color = Color.Black,
                fontFamily = onePop,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // 노래 제목과 아티스트
            Text(
                text = "${randomSong.title} - ${randomSong.artist}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Magenta,
                fontFamily = onePop,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // 로딩바
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .padding(horizontal = 50.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFFB3E5FC), // 옅은 하늘색
                backgroundColor = Color(0xFFE0F7FA) // 아주 옅은 민트색
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    SplashScreen(navController = NavHostController(LocalContext.current))
}
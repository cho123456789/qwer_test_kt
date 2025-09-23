import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            Column (
                modifier = Modifier.padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Photo",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontFamily = cafe24,
                )
                Text(
                    text = "Widget",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontFamily = cafe24,
                )
            }
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

            Row(
                modifier = Modifier.padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .width(120.dp)
                        .height(50.dp),
                    onClick = {
                        navController.navigate(Route.Memeber)
                    },
                    shape = RoundedCornerShape(20.dp), // 👈 버튼을 둥글게 만듭니다.
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0B0FF) // 👈 연보라색 배경으로 변경
                    )
                ) {
                    Text(
                        text = "멤버소개",
                        fontSize = 18.sp,
                        fontFamily = onePop,
                        color = Color.White
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .width(120.dp)
                        .height(50.dp),
                    onClick = {
                        navController.navigate(Route.Gominjungdok)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB3E5FC)
                    )
                ) {
                    Text(
                        text = "위젯기능",
                        fontSize = 18.sp,
                        fontFamily = onePop,
                        color = Color.White
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
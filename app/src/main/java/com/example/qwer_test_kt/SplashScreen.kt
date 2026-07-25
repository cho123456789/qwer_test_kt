import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.Route
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

    val profilesState by viewModel.profiles.collectAsStateWithLifecycle()


    // 로딩 완료 시 자동으로 메인 페이지로 이동
    LaunchedEffect(profilesState) {
        if (profilesState != null) {
            // 데이터 로딩이 완료되면 즉시 메인 페이지로 이동
            navController.navigate(Route.HOME) {
                popUpTo(Route.Splash) { inclusive = true }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 로딩 인디케이터
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = Color(0xFFC2185B),
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 로딩 메시지
            Text(
                text = "리센느는 오늘도 열일중",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = barry,
                color = Color(0xFFC2185B)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    val barry = FontFamily(Font(R.font.barry))
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF5F5DC), // 옅은 베이지색
            Color(0xFFFFF0F5)  // 옅은 복숭아색
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 로딩 인디케이터
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = Color(0xFFC2185B),
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 로딩 메시지
            Text(
                text = "리센느는 오늘도 열일중",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = barry,
                color = Color(0xFFC2185B)
            )
        }
    }
}

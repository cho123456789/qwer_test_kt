package com.example.qwer_test_kt.gomin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qwer_test_kt.R

@Composable
fun WidgetScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // 전체 화면에 패딩을 적용하여 여백을 줍니다.
    ) {
        // 첫 번째 요소로 TouchableImageCard를 배치하여 화면 최상단에 위치시킵니다.
        TouchableImageCard(
            imagePainter = painterResource(id = R.drawable.mm),
            titles = listOf("The 1st Mini Album", "MANITO", "QWER"),
            onClick = {
                // 이 카드를 클릭했을 때 실행할 동작
            },
            modifier = Modifier
                .fillMaxHeight(0.2f) // 화면 높이의 30%를 차지하도록 설정
                .padding(horizontal = 16.dp, vertical = 8.dp) // 좌우 및 위아래 패딩을 추가
        )
    }
}

@Composable
fun TouchableImageCard(
    imagePainter: Painter,
    titles: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() } // 전체 박스를 터치 가능하게 만듭니다.
    ) {
        // 배경 이미지
        Image(
            painter = imagePainter,
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center // 이미지를 위쪽에 맞춰서 자릅니다.
        )

        // 이미지 위에 겹칠 내용들을 담을 Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom, // 내용을 아래쪽으로 정렬합니다.
            horizontalAlignment = Alignment.End // 내용을 오른쪽으로 정렬합니다.
        ) {
            // 배경과 텍스트의 가독성을 높이기 위해 배경을 살짝 어둡게 처리할 수도 있습니다.
            // Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f))) { ... }

            titles.forEach { title ->
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontFamily = onePop,
                    textAlign = TextAlign.End,
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTouchableImageCard() {
    // 미리보기를 위한 더미 데이터
    val dummyImagePainter = painterResource(id = R.drawable.mm) // 예시 이미지 리소스 ID로 변경하세요.
    val dummyTitles = listOf("The 1st Mini Album", "MANITO", "QWER")

    TouchableImageCard(
        imagePainter = dummyImagePainter,
        titles = dummyTitles,
        onClick = {
            // 클릭 이벤트 처리 로직 (미리보기에서는 동작하지 않습니다.)
        }
    )
}

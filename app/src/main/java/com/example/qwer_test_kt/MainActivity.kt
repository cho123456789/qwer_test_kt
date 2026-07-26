package com.example.qwer_test_kt

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startRoute = if (intent.getBooleanExtra("open_widget_registration", false)) Route.WidgetRegistration else Route.Splash
        setContent { MaterialTheme { AppNavGraph(startRoute) } }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    RescenePhotoDiary(onStart = { navController.navigate(Route.PhotoWidget) })
}

@Composable
private fun RescenePhotoDiary(onStart: () -> Unit) {
    val pixel = FontFamily(Font(R.font.onepop))
    val ink = Color(0xFF4B3B55)
    val windowShape = RoundedCornerShape(28.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF8D5E2), Color(0xFFFFF2DF), Color(0xFFE4DDF6))))
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        BackgroundSticker("✦", Modifier.align(Alignment.TopStart).offset(17.dp, 54.dp), Color.White, 27.sp)
        BackgroundSticker("+", Modifier.align(Alignment.TopEnd).offset((-18).dp, 112.dp), Color(0xFFC0A7E8), 28.sp)
        BackgroundSticker("✿", Modifier.align(Alignment.BottomStart).offset(18.dp, (-62).dp), Color(0xFFFFC4D7), 26.sp)
        BackgroundSticker("⋆  ·  ✦  ·  ⋆", Modifier.align(Alignment.BottomCenter).offset(y = (-22).dp), Color.White.copy(alpha = .82f), 19.sp)
        BackgroundSticker("○", Modifier.align(Alignment.BottomEnd).offset((-82).dp, (-142).dp), Color(0xFFBBA1E1), 24.sp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .shadow(14.dp, windowShape, clip = false)
                .clip(windowShape)
                .background(Color(0xFFFFFBFC))
                .border(2.dp, Color(0xFFE7B6CD), windowShape)
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DiaryTitleBar(pixel, ink)
            Column(
                modifier = Modifier.padding(horizontal = 13.dp, vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    DiaryChip("YOYO", Color(0xFFB497DB), pixel)
                    DiaryChip("UhUh", Color(0xFFB497DB), pixel)
                    DiaryChip("LOVE ATTACK", Color(0xFFB497DB), pixel)
                    DiaryChip("Glow Up", Color(0xFFB497DB), pixel)
                    DiaryChip("Deja Vu", Color(0xFFB497DB), pixel)
                    DiaryChip("Heart Drop", Color(0xFFB497DB), pixel)
                    DiaryChip("Runaway", Color(0xFFB497DB), pixel)
                    DiaryChip("Pretty Girl", Color(0xFFB497DB), pixel)
                }
                Spacer(Modifier.height(11.dp))
                PolaroidPreview(pixel, ink)
                Spacer(Modifier.height(15.dp))
                Text("RESCENE", fontFamily = pixel, fontSize = 24.sp, letterSpacing = 1.sp, color = ink)
                Text(
                    "향기로 다시(RE) 장면(SCENE)을 떠올린다는 의미로,\n대중의 마음속에 오래도록 남을 음악적 향기를\n선사하겠다는 리센느의 포부",
                    modifier = Modifier.padding(top = 4.dp),
                    fontFamily = pixel,
                    fontSize = 13.sp,
                    letterSpacing = .8.sp,
                    color = Color(0xFF9C7F96),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                DiaryButton(pixel, onStart)
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun DiaryTitleBar(pixel: FontFamily, ink: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 21.dp, topEnd = 21.dp, bottomStart = 7.dp, bottomEnd = 7.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFF2C4D9), Color(0xFFE2C6EF), Color(0xFFC9DDF3))))
            .padding(start = 13.dp, end = 8.dp, top = 10.dp, bottom = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("RESCENE_DIARY.EXE", modifier = Modifier.weight(1f), fontFamily = pixel, fontSize = 16.sp, letterSpacing = .6.sp, color = ink)
    }
}

@Composable
private fun DiaryWindowButton(symbol: String, ink: Color) {
    Text(
        symbol,
        modifier = Modifier.padding(start = 4.dp).size(23.dp).background(Color(0xFFFFFBFF)).border(1.dp, ink.copy(alpha = .6f)).wrapContentSize(Alignment.Center),
        color = ink,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun PolaroidPreview(pixel: FontFamily, ink: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(.86f)
            .background(Color.White)
            .border(3.dp, Color(0xFF5B4D6E))
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp))
                .border(2.dp, Color(0xFFFFF9FD))
        ) {
            Image(
                painter = painterResource(R.drawable.rescene_photo_diary),
                contentDescription = "RESCENE photo diary memory",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            DiarySticker("♥", Modifier.align(Alignment.TopStart).offset((-7).dp, (-9).dp).rotate(-13f), Color(0xFFFFD974), ink, pixel)
            DiarySticker("★", Modifier.align(Alignment.TopEnd).offset(8.dp, 11.dp).rotate(12f), Color(0xFFB7DCF1), ink, pixel)
            DiarySticker("cute!", Modifier.align(Alignment.BottomStart).offset((-5).dp, 8.dp).rotate(-8f), Color(0xFFFFB7CC), ink, pixel)
            DiarySticker("☆", Modifier.align(Alignment.BottomEnd).offset(8.dp, (-7).dp).rotate(10f), Color(0xFFC5A8F5), ink, pixel)
            MemberNameSticker("메이", Modifier.align(Alignment.TopStart).offset(50.dp, 51.dp).rotate(-7f), Color(0xFFFFD2A7), ink, pixel)
            MemberNameSticker("원이", Modifier.align(Alignment.TopEnd).offset((-70).dp, 40.dp).rotate(8f), Color(0xFFBFE6F3), ink, pixel)
            MemberNameSticker("제나", Modifier.align(Alignment.CenterStart).offset(20.dp, 56.dp).rotate(-9f), Color(0xFFFFBFD4), ink, pixel)
            MemberNameSticker("미나미", Modifier.align(Alignment.CenterEnd).offset((-7).dp, 73.dp).rotate(9f), Color(0xFFD6BDF4), ink, pixel)
            MemberNameSticker("리브", Modifier.align(Alignment.BottomCenter).offset(y = (-30).dp).rotate(-4f), Color(0xFFFFE490), ink, pixel)
        }
    }
}

@Composable
private fun DiarySticker(text: String, modifier: Modifier, color: Color, ink: Color, pixel: FontFamily) {
    Text(
        text,
        modifier = modifier.shadow(2.dp, RoundedCornerShape(4.dp)).background(color, RoundedCornerShape(4.dp)).border(2.dp, ink, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 3.dp),
        fontFamily = pixel,
        fontWeight = FontWeight.Bold,
        fontSize = if (text.length > 1) 10.sp else 18.sp,
        color = ink
    )
}

@Composable
private fun MemberNameSticker(text: String, modifier: Modifier, color: Color, ink: Color, pixel: FontFamily) {
    Text(
        text,
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .background(color, RoundedCornerShape(12.dp))
            .border(2.dp, Color.White, RoundedCornerShape(12.dp))
            .padding(horizontal = 7.dp, vertical = 4.dp),
        fontFamily = pixel,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = .3.sp,
        color = ink
    )
}

@Composable
private fun DiaryChip(text: String, color: Color, pixel: FontFamily) {
    Text(text, modifier = Modifier.background(color).padding(horizontal = 5.dp, vertical = 5.dp), fontFamily = pixel, fontSize = 9.sp, color = Color.White)
}

@Composable
private fun DiaryButton(pixel: FontFamily, onStart: () -> Unit) {
    Text(
        "PHOTO WIDGET ♡",
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFEE9CB8))
            .border(2.dp, Color(0xFF69526E), RoundedCornerShape(22.dp))
            .clickable(onClick = onStart)
            .padding(vertical = 14.dp),
        fontFamily = pixel,
        fontSize = 20.sp,
        letterSpacing = .4.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        style = TextStyle(shadow = Shadow(Color(0xFF9B5270), blurRadius = 1.5f))
    )
}

@Composable
private fun BackgroundSticker(symbol: String, modifier: Modifier, color: Color, size: androidx.compose.ui.unit.TextUnit) {
    Text(symbol, modifier = modifier, color = color, fontSize = size, style = TextStyle(shadow = Shadow(Color.White.copy(alpha = .7f), blurRadius = 2f)))
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, heightDp = 850)
@Composable
private fun RescenePhotoDiaryPreview() {
    MaterialTheme { RescenePhotoDiary(onStart = {}) }
}

package com.example.qwer_test_kt

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavGraph()
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    val cafe24 = FontFamily(Font(R.font.cafe24decoshadow))
    val onePop = FontFamily(Font(R.font.onepop))
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE1F5FE),
            Color(0xFFFCE4EC)
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
            modifier = Modifier.padding(top = 100.dp)
        )

        Column(
            modifier = Modifier.padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Photo",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9800),
                fontFamily = cafe24,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Widget",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                fontFamily = cafe24,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Concept Cards Grid
        Column(
            modifier = Modifier
                .background(Color.Transparent, shape = RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // First row of cards
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)

            ) {
                ConceptCard(
                    imageResId = R.drawable.discord,
                    text = "Discord",
                    fontFamily = onePop,
                    onClick = { /* Handle Discord card click */ }
                )
                ConceptCard(
                    imageResId = R.drawable.mani2,
                    text = "고민중독",
                    fontFamily = onePop,
                    onClick = {
                        navController.navigate(Route.Gominjungdok)
                    }
                )
            }
            // Second row of cards
            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ConceptCard(
                    imageResId = R.drawable.qwer2,
                    text = "가짜아이돌",
                    fontFamily = onePop,
                    onClick = { /* Handle 가짜아이돌 card click */ }
                )
                ConceptCard(
                    imageResId = R.drawable.dear,
                    text = "눈물참기",
                    fontFamily = onePop,
                    onClick = { /* Handle 눈물참기 card click */ }
                )
            }
        }
    }
}

@Composable
fun ConceptCard(
    imageResId: Int,
    text: String,
    fontFamily: FontFamily,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cornerSize by animateDpAsState(
        targetValue = if (isPressed) 10.dp else 20.dp, // 눌렸을 때 12dp, 평상시 24dp
        label = "cornerAnimation"
    )

    Card(
        modifier = Modifier
            .size(160.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(cornerSize),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = text,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
            )
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    AppNavGraph()
}


//        enableEdgeToEdge()
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val qwerText = "QWER"
//        val spannableString = SpannableString(qwerText)
//
//
//        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#FFC0CB")), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#00B0FF")), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#8BC34A")), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.titleTextViewQWER.text = spannableString
//
//
//        binding.cardDiscord.setOnClickListener {
//           // val intent = Intent(this, PhotoWidgetActivity::class.java)
//            //startActivity(intent)
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            if (!alarmManager.canScheduleExactAlarms()) {
//                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
//                intent.data = Uri.parse("package:$packageName")
//                startActivity(intent)
//            }
//        }
//
//        val intent = Intent(this, BatteryMonitorService::class.java)
//        ContextCompat.startForegroundService(this, intent)
//
//        SiyeonWidgetProvider().scheduleNextUpdate(this)
//    }
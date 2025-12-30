package com.example.qwer_test_kt.gomin.wiget.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.qwer_test_kt.R

val cafe24 = FontFamily(Font(R.font.cafe24decoshadow))
val onePop = FontFamily(Font(R.font.onepop))
val barry = FontFamily(Font(R.font.barry))

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GominjungdokScreen(
    navController: NavHostController,
) {
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
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                ) // StatusBar 높이만큼 패딩
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // PhotoWidgetScreen을 바로 표시
            PhotoWidgetContent(navController)
        }
    }
}
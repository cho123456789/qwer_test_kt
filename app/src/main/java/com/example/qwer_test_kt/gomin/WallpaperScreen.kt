package com.example.qwer_test_kt.gomin

import android.app.WallpaperManager
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.qwer_test_kt.R

@Composable
fun WallpaperScreen() {
    val context = LocalContext.current
    val imageBitmap =
        (context.getDrawable(R.drawable.mani5) as BitmapDrawable).bitmap.asImageBitmap()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 1. 화면을 가득 채우는 이미지
        Image(
            painter = painterResource(id = R.drawable.mani5),
            contentDescription = "qwer",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. 하단에 위치한 버튼
        Button(
            onClick = {
                val drawable = context.getDrawable(R.drawable.mani5)
                if (drawable is BitmapDrawable) {
                    val bitmap = drawable.bitmap
                    val wallpaperManager = WallpaperManager.getInstance(context)
                    try {
                        wallpaperManager.setBitmap(bitmap)
                        Toast.makeText(context, "배경화면이 성공적으로 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "배경화면 설정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.8f), // 너비를 부모의 80%로 설정
            colors = ButtonDefaults.buttonColors(Color.Blue)
        ) {
            Text(text = "배경화면 등록하기", color = Color.White)
        }
    }
}
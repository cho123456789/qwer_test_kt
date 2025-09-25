package com.example.qwer_test_kt.gomin.view

import android.content.Context
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.qwer_test_kt.gomin.onePop
import com.example.qwer_test_kt.presentation.GominJungdokViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WallpaperDetailScreen(
    wallpaperUrl: String,
    onBackPressed: () -> Unit,
    viewModel: GominJungdokViewModel,
) {
    val scrollState = rememberScrollState()

    BackHandler {
        onBackPressed()
    }

    val context = LocalContext.current
    var showWidgetSelectionDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기"
                )
            }
        }


        AsyncImage(
            model = wallpaperUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        WallpaperAndWidgetRow(
            viewModel = viewModel, // Pass the instance
            context = context,
            wallpaperUrl = wallpaperUrl,
            onSetWidgetClicked = {
                showWidgetSelectionDialog = true
            } // Pass the lambda
        )
        if (showWidgetSelectionDialog) {
            WidgetSelectionDialog(
                onDismissRequest = {
                    showWidgetSelectionDialog = false
                },
                onWidgetSelected = {
                    showWidgetSelectionDialog = false
                },
                wallpaperUrl = wallpaperUrl
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WallpaperAndWidgetRow(
    viewModel: GominJungdokViewModel,
    context: Context,
    wallpaperUrl: String,
    onSetWidgetClicked: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            onClick = {
                viewModel.setWallpaper(context, wallpaperUrl) {
                    context.startActivity(it)
                }
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF80CBC4),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "배경화면 등록",
                fontFamily = onePop,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onSetWidgetClicked,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF80CBC4),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "위젯 등록",
                fontFamily = onePop,
                fontSize = 18.sp
            )
        }
        if (showDialog) {
            WidgetSelectionDialog(
                onDismissRequest = {
                    showDialog = false
                },
                onWidgetSelected = {
                    showDialog = false
                },
                wallpaperUrl = wallpaperUrl
            )
        }
    }
}
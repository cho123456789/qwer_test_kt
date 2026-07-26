package com.example.qwer_test_kt.gomin.wiget.dialog

import android.content.ComponentName
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.qwer_test_kt.gomin.view.WidgetButton
import com.example.qwer_test_kt.gomin.view.requestPinWidget
import com.example.qwer_test_kt.gomin.wiget.GoDdayWidgetReceiver
import com.example.qwer_test_kt.gomin.wiget.GoWatchWidgetReceiver
import com.example.qwer_test_kt.gomin.wiget.PhotoWidgetReceiver
import com.example.qwer_test_kt.gomin.wiget.screen.barry

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WidgetSelectionDialog(
    onDismissRequest: () -> Unit,
    onWidgetSelected: () -> Unit,
    wallpaperUrl: String,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedProvider by remember { mutableStateOf<ComponentName?>(null) }
    var selectedName by remember { mutableStateOf<String?>(null) }
    var showClockDialog by remember { mutableStateOf(false) }
    val gradient = Brush.linearGradient(listOf(Color(0xFFF8D5E2), Color(0xFFFFF2DF), Color(0xFFE4DDF6)))

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color(0xFFFFF8FB)
        ) {
            Column(
                modifier = Modifier.background(gradient).verticalScroll(rememberScrollState()).padding(24.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("위젯 스타일 선택", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontFamily = barry, fontSize = 20.sp, color = Color(0xFF4B3B55))
                    IconButton(onClick = onDismissRequest, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = "닫기", tint = Color(0xFF9B5270))
                    }
                }
                Spacer(Modifier.height(24.dp))
                WidgetButton("시계 위젯", selectedName == "clock") {
                    selectedName = "clock"
                    selectedProvider = ComponentName(context, GoWatchWidgetReceiver::class.java)
                }
                Spacer(Modifier.height(16.dp))
                WidgetButton("사진 위젯", selectedName == "photo") {
                    selectedName = "photo"
                    selectedProvider = ComponentName(context, PhotoWidgetReceiver::class.java)
                }
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(onClick = onDismissRequest, colors = ButtonDefaults.buttonColors(Color(0xFFF8D5E2), Color(0xFF9B5270))) {
                        Text("취소", fontFamily = barry)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val provider = selectedProvider ?: return@Button
                            when (selectedName) {
                                "clock" -> showClockDialog = true
                                else -> {
                                    requestPinWidget(context, provider, wallpaperUrl, "photo", "center")
                                    onWidgetSelected()
                                }
                            }
                        },
                        enabled = selectedProvider != null,
                        colors = ButtonDefaults.buttonColors(Color(0xFFEE9CB8), Color.White)
                    ) { Text("확인", fontFamily = barry) }
                }
            }
        }
    }

    if (showClockDialog) {
        ClockPositionDialog(
            wallpaperUrl = wallpaperUrl,
            onDismiss = { showClockDialog = false },
            onPositionSelected = { position ->
                selectedProvider?.let { requestPinWidget(context, it, wallpaperUrl, "clock", position) }
                showClockDialog = false
                onWidgetSelected()
            }
        )
    }
}

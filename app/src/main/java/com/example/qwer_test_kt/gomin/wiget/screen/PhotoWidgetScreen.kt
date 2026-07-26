package com.example.qwer_test_kt.gomin.wiget.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.qwer_test_kt.Route
import com.example.qwer_test_kt.gomin.wiget.dialog.ImageDetailDialog
import com.example.qwer_test_kt.presentation.PhotoWidgetViewModel

private data class MemberProfile(
    val displayName: String,
    val databaseName: String
)

@Composable
fun PhotoWidgetScreen(
    navController: NavHostController,
    viewModel: PhotoWidgetViewModel = hiltViewModel()
) {
    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(Color(0xFFF8D5E2), Color(0xFFFFF2DF), Color(0xFFE4DDF6))))
                    .padding(top = 40.dp, start = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate(Route.HOME) }, modifier = Modifier.size(44.dp)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF9B5270))
                }
                Text("MEMBER PHOTO", modifier = Modifier.weight(1f), fontFamily = barry, fontWeight = FontWeight.Bold, color = Color(0xFF4B3B55))
            }
        }
    ) { padding ->
        PhotoWidgetContent(navController, viewModel, Modifier.padding(padding))
    }
}

@Composable
fun PhotoWidgetContent(
    navController: NavHostController,
    viewModel: PhotoWidgetViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val currentImage by viewModel.currentImage.collectAsStateWithLifecycle()
    val profileImages by viewModel.memberProfileImages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var selectedMember by remember { mutableStateOf<MemberProfile?>(null) }
    var showImageDialog by remember { mutableStateOf(false) }
    val members = listOf(
        MemberProfile("원이", "WONI"),
        MemberProfile("리브", "LIV"),
        MemberProfile("메이", "MEI"),
        MemberProfile("제나", "JENA"),
        MemberProfile("미나미", "MINAMI")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("MEMBER PHOTO WIDGET", fontFamily = barry, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF4B3B55))
        Text(
            "프로필을 누르면 해당 멤버의 사진이 랜덤으로 갱신됩니다",
            modifier = Modifier.padding(top = 4.dp),
            fontFamily = barry,
            fontSize = 12.sp,
            color = Color(0xFF9C7F96),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            members.forEach { member ->
                Column(
                    modifier = Modifier.clickable {
                        selectedMember = member
                        viewModel.selectRandomImage(member.databaseName)
                    },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context).data(profileImages[member.databaseName]).crossfade(true).build(),
                        contentDescription = member.displayName,
                        modifier = Modifier.size(66.dp)
                            .background(if (member == selectedMember) Color(0xFFEE9CB8) else Color.White, CircleShape)
                            .padding(3.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        loading = { CircularProgressIndicator(Modifier.size(18.dp), Color(0xFF9B5270), 2.dp) },
                        error = { Box(Modifier.fillMaxSize().background(Color.LightGray)) }
                    )
                    Text(member.displayName, modifier = Modifier.padding(top = 5.dp), fontFamily = barry, fontSize = 10.sp, color = Color(0xFF9B5270))
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Card(Modifier.fillMaxWidth().height(440.dp), RoundedCornerShape(22.dp), Color.White, elevation = 6.dp) {
            Box(contentAlignment = Alignment.Center) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = Color(0xFFEE9CB8),
                        strokeWidth = 3.dp
                    )
                } else if (selectedMember != null && currentImage != null) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context).data(currentImage).crossfade(true).build(),
                        contentDescription = selectedMember!!.displayName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = { CircularProgressIndicator(Modifier.size(30.dp), Color(0xFFEE9CB8), 3.dp) },
                        error = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp),
                                color = Color(0xFFEE9CB8),
                                strokeWidth = 3.dp
                            )
                        }
                    )
                } else {
                    Text("멤버를 클릭하면 랜덤 이미지가 표시됩니다", color = Color(0xFF9C7F96), textAlign = TextAlign.Center)
                }
            }
        }
        if (selectedMember != null && currentImage != null) {
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { showImageDialog = true },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEE9CB8), contentColor = Color.White)
            ) { Text("위젯등록하기", fontFamily = barry, fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(20.dp))
    }

    if (showImageDialog && currentImage != null) {
        ImageDetailDialog(currentImage!!, { showImageDialog = false }, context, viewModel, navController)
    }
}

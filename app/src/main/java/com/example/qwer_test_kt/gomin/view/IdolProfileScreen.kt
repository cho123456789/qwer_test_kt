package com.example.qwer_test_kt.gomin.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.gomin.onePop
import com.example.qwer_test_kt.gomin.view.model.MemberInfo
import com.example.qwer_test_kt.gomin.view.model.albumInfo
import com.example.qwer_test_kt.gomin.view.model.albums
import com.example.qwer_test_kt.gomin.view.model.members


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdolProfileScreenWithScaffold(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("QWER 프로필", fontFamily = onePop)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        var showMembers by remember { mutableStateOf(false) }
        var selectedAlbumId by remember { mutableStateOf<String?>(null) }
        val uriHandler = LocalUriHandler.current

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // 메인 그룹 이미지 (클릭 시 멤버 프로필 표시)
                Image(
                    painter = painterResource(id = R.drawable.qwer_ban),
                    contentDescription = "QWER",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .clickable {
                            showMembers = !showMembers
                        },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (showMembers) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "멤버 정보",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        fontFamily = onePop,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                // 멤버 프로필 리스트
                items(members) { member ->
                    MemberProfile(member)
                }
            }

            if (!showMembers) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "앨범 정보",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        fontFamily = onePop,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                items(albums) { album ->
                    AlbumCard(
                        album = album,
                        isSelected = selectedAlbumId == album.id,
                        onCardSelect = {
                            selectedAlbumId = if (selectedAlbumId == album.id) null else album.id
                        },
                        onImageClick = {
                            uriHandler.openUri(album.musicVideo)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MemberProfile(memberInfo: MemberInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, Color(0xFF0000FF)), CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = memberInfo.image),
                        contentDescription = "${memberInfo.name} 프로필 이미지",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = memberInfo.nickname,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        fontFamily = onePop,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "# ${memberInfo.position}",
                        fontSize = 14.sp,
                        color = Color(0xFFC70039),
                        fontFamily = onePop,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                // Full Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "본명: ${memberInfo.name}",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        fontFamily = onePop
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                // Birthday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Birthday Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "생일: ${memberInfo.birthday}",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        fontFamily = onePop
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = "설명: ${memberInfo.description}",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontFamily = onePop,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun AlbumListScreen() {
    var selectedAlbumId by remember { mutableStateOf<String?>(null) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        items(albums) { album ->
            AlbumCard(
                album = album,
                isSelected = selectedAlbumId == album.id,
                onCardSelect = {
                    selectedAlbumId = if (selectedAlbumId == album.id) null else album.id
                },
                onImageClick = {

                }
            )
        }
    }
}

@Composable
fun AlbumCard(
    album: albumInfo,
    onCardSelect: () -> Unit,
    onImageClick: () -> Unit,
    isSelected: Boolean
) {
    var likesCount by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color.Blue else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onCardSelect() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album Image
            Image(
                painter = painterResource(id = album.image),
                contentDescription = "${album.title} Album Cover",
                modifier = Modifier
                    .size(120.dp)
                    .clickable { onImageClick() }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                // This part is correct because it's using stringResource()
                Text(
                    modifier = Modifier.padding(
                        start = 5.dp,
                        top = 5.dp,
                        end = 5.dp,
                        bottom = 5.dp
                    ),
                    text = stringResource(id = album.title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = buildAnnotatedString {
                        album.trackList.forEachIndexed { index, trackId ->
                            val trackString = stringResource(id = trackId)
                            if (index == 0) {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(trackString)
                                }
                            } else {
                                append(trackString)
                            }
                            if (index < album.trackList.size - 1) {
                                append("\n")
                            }
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(
                onClick = {
                    likesCount++
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    tint = if (likesCount > 0) Color.Red else Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$likesCount",
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontFamily = onePop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IdolProfileScreenWithScaffoldPreview() {
    IdolProfileScreenWithScaffold(navController = NavHostController(LocalContext.current))
}

@Preview(showBackground = true, locale = "KR")
@Composable
fun Profile() {
    AlbumListScreen()
}

@Preview(showBackground = true, locale = "fr-rFR", showSystemUi = true)
@Composable
fun MemberProfilePreview() {
    MemberProfile(
        memberInfo = MemberInfo(
            "이시연",
            "리드 보컬",
            "이시연",
            "2000.05.16",
            R.drawable.gomin_siyeon_profile2,
            "밴드의 메인 보컬을 맡고 있습니다. 🎤"
        )
    )
}
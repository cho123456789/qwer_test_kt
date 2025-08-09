package com.example.qwer_test_kt.gomin

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.Route
import com.example.qwer_test_kt.presentation.GominJungdokViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState


val cafe24 = FontFamily(Font(R.font.cafe24decoshadow))
val onePop = FontFamily(Font(R.font.onepop))

val members = listOf(
    Member(
        "쵸단 ", R.drawable.gomin_cho_profile,
        listOf(
            "https://scontent-icn2-1.cdninstagram.com/v/t51.29350-15/434297446_3756819281310444_7596647869360208337_n.jpg?stp=dst-jpg_e35_tt6&efg=eyJ2ZW5jb2RlX3RhZyI6IkNBUk9VU0VMX0lURU0uaW1hZ2VfdXJsZ2VuLjE0NDB4MTgwMC5zZHIuZjI5MzUwLmRlZmF1bHRfaW1hZ2UuYzIifQ&_nc_ht=scontent-icn2-1.cdninstagram.com&_nc_cat=100&_nc_oc=Q6cZ2QEnF0WSzQcnsIKRb8ioD6xdOHjLjlCYrRg5B2dEWROeBLTUtgEdbBsAYmcP8IFw06Y&_nc_ohc=9-h6Pb3t-3MQ7kNvwFA__e4&_nc_gid=EGOX6AZD2f8G35Mho-LBcQ&edm=APs17CUBAAAA&ccb=7-5&ig_cache_key=MzMzMDMxODA5Mzk5Nzc3MTEzOQ%3D%3D.3-ccb7-5&oh=00_AfXY-2rImq5fwewpmgT7pFuAutUHtEQTEPkkiWq_OfQ_uA&oe=689CE140&_nc_sid=10d13b",
            "https://scontent-icn2-1.cdninstagram.com/v/t51.29350-15/434300692_2076879782711749_8654505459255521025_n.jpg?stp=dst-jpg_e35_tt6&efg=eyJ2ZW5jb2RlX3RhZyI6IkNBUk9VU0VMX0lURU0uaW1hZ2VfdXJsZ2VuLjE0NDB4MTc5Ni5zZHIuZjI5MzUwLmRlZmF1bHRfaW1hZ2UuYzIifQ&_nc_ht=scontent-icn2-1.cdninstagram.com&_nc_cat=101&_nc_oc=Q6cZ2QEnF0WSzQcnsIKRb8ioD6xdOHjLjlCYrRg5B2dEWROeBLTUtgEdbBsAYmcP8IFw06Y&_nc_ohc=UhmzR8FiN2wQ7kNvwERWdLL&_nc_gid=EGOX6AZD2f8G35Mho-LBcQ&edm=APs17CUBAAAA&ccb=7-5&ig_cache_key=MzMzMDMxODA5Mzg4MDMwMjYxMQ%3D%3D.3-ccb7-5&oh=00_AfU-DgHWLLEV6cH9nurIwY03HcmgIOfhajEYkCnYw9CbWQ&oe=689CBA37&_nc_sid=10d13b",
            "https://scontent-icn2-1.cdninstagram.com/v/t51.29350-15/421030505_3669590759993293_7685181885209961154_n.jpg?stp=dst-jpg_e35_tt6&efg=eyJ2ZW5jb2RlX3RhZyI6IkNBUk9VU0VMX0lURU0uaW1hZ2VfdXJsZ2VuLjE0NDB4MTc5OS5zZHIuZjI5MzUwLmRlZmF1bHRfaW1hZ2UuYzIifQ&_nc_ht=scontent-icn2-1.cdninstagram.com&_nc_cat=101&_nc_oc=Q6cZ2QEnF0WSzQcnsIKRb8ioD6xdOHjLjlCYrRg5B2dEWROeBLTUtgEdbBsAYmcP8IFw06Y&_nc_ohc=4f7-eiA-pHgQ7kNvwFjg9G6&_nc_gid=EGOX6AZD2f8G35Mho-LBcQ&edm=APs17CUBAAAA&ccb=7-5&ig_cache_key=MzMzMDMxODA5Mzg4MDIzNjQ0NA%3D%3D.3-ccb7-5&oh=00_AfUgWo_--WCcdsSnEZ84M7_PEYPDYSPVZjuc79hmIRkB5g&oe=689CE113&_nc_sid=10d13b",
            "https://scontent-icn2-1.cdninstagram.com/v/t51.29350-15/421042277_7397278296981580_7460217418024092784_n.jpg?stp=dst-jpg_e35_tt6&efg=eyJ2ZW5jb2RlX3RhZyI6IkNBUk9VU0VMX0lURU0uaW1hZ2VfdXJsZ2VuLjE0NDB4MTgwMC5zZHIuZjI5MzUwLmRlZmF1bHRfaW1hZ2UuYzIifQ&_nc_ht=scontent-icn2-1.cdninstagram.com&_nc_cat=104&_nc_oc=Q6cZ2QEnF0WSzQcnsIKRb8ioD6xdOHjLjlCYrRg5B2dEWROeBLTUtgEdbBsAYmcP8IFw06Y&_nc_ohc=OdIpEZnYcWEQ7kNvwFyndjr&_nc_gid=EGOX6AZD2f8G35Mho-LBcQ&edm=APs17CUBAAAA&ccb=7-5&ig_cache_key=MzMzMDMxODA5Mzg4MDE5NTEwNA%3D%3D.3-ccb7-5&oh=00_AfWgOsRO_ABCfE9bRQcFCAK04LXAZyEuqc10L7K66AGhLQ&oe=689CEDD8&_nc_sid=10d13b"
        ),

//    Member("마젠타", R.drawable.gomin_ma_profile, "https://kpopping.com/cloudflare-proxy/e81ca1e707f7f057a9eb7a135d70d286"),
//    Member("히나", R.drawable.gomin_hina_profile, "https://kpopping.com/cloudflare-proxy/b70a864fa1d55a2de57be9db71cd8848"),
//    Member("시연", R.drawable.gomin_siyeon_profile, "https://kpopping.com/cloudflare-proxy/e0123392956af229160dad89fd45a23b"),
//    Member("단체", R.drawable.gomin_group,"https://kpopping.com/cloudflare-proxy/5a2985494474cecd8d77954ae7e627c6")
    )
)
//val membersSecond = listOf(
//    Member("쵸단 ", R.drawable.gomin_cho_profile2, R.drawable.gomin_cho2),
//    Member("마젠타", R.drawable.gomin_ma_profile2, R.drawable.gomin_ma2),
//    Member("히나", R.drawable.gomin_hina_profile2, R.drawable.gomin_hina2),
//    Member("시연", R.drawable.gomin_siyeon_profile2, R.drawable.gomin_si2),
//    Member("단체", R.drawable.gomin_group_profile2, R.drawable.gomin_group2)
//)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GominjungdokScreen(navController: NavHostController) {

    val viewModel : GominJungdokViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0)
    var selectedNavIndex by remember { mutableStateOf(0) }
    var selectedMember by remember { mutableStateOf(members[0]) }
    var isSecondTheme by remember { mutableStateOf(false) }
    //val currentMembers = if (isSecondTheme) membersSecond else members

    var selectedWallpaperUrl by remember { mutableStateOf<String?>(null) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA), // 더 밝고 화사한 하늘색
            Color(0xFFE1BEE7)  // 더 밝은 연보라색
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "테마 변경",
                fontSize = 16.sp,
                fontFamily = onePop,
                color = Color.Blue,
                modifier = Modifier.clickable {
                    isSecondTheme = !isSecondTheme
                    selectedMember = members[0]
                }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {

            MemberProfile(
                members = members,
                selectedMember = selectedMember,
                onMemberSelected = { member -> selectedMember = member }
            )

            when (selectedNavIndex) {
                0 -> {
                    if (uiState.selectedWallpaper == null) {
                        WallpaperPreviewScreen(
                            wallpaperUrls = selectedMember.wallPaperImageResId,
                            // ViewModel의 함수를 호출하여 상태 변경을 요청합니다.
                            onWallpaperSelected = { url -> viewModel.onWallpaperSelected(url) }
                        )
                    } else {
                        WallpaperDetailScreen(
                            wallpaperUrl = uiState.selectedWallpaper!!,
                            onBackPressed = { viewModel.onBackPressed() },
                            viewModel = viewModel
                        )
                    }
                }

                1 -> WidgetScreen()
                2 -> IconScreen()
            }
        }

        BottomNavigationBar(
            selectedIndex = selectedNavIndex,
            onItemSelected = { index -> selectedNavIndex = index }
        )
    }
}

@Composable
fun MemberProfile(
    members: List<Member>,
    selectedMember: Member,
    onMemberSelected: (Member) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        members.forEach {
            MemberProfileImage(
                members = it,
                isSelected = it == selectedMember, // 현재 멤버와 선택된 멤버를 비교합니다.
                onClick = { onMemberSelected(it) }
            )
        }
    }
}

@Composable
fun MemberProfileImage(members: Member, isSelected: Boolean, onClick: () -> Unit) {

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = 0.5f,
        )
    )

    val borderWidth: Dp by animateDpAsState(
        targetValue = if (isSelected) 4.dp else 8.dp,
        animationSpec = tween(durationMillis = 300)
    )

    val borderColor = if (isSelected) Color.White else Color.White

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            .scale(scale)
    ) {
        Image(
            painter = painterResource(id = members.profileImageResId),
            contentDescription = "${members.name} profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(65.dp)
                .clip(RoundedCornerShape(40.dp))
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(40.dp),
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = members.name,
            fontSize = 14.sp,
            fontFamily = onePop,
        )
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val navItems = listOf(
        Pair(R.drawable.discord, "배경화면"),
        Pair(R.drawable.qwer2, "위젯"),
        Pair(R.drawable.dear, "아이콘")
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White.copy(alpha = 0.8f)), // 반투명 흰색
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationItem(
                iconResId = item.first,
                text = item.second,
                isSelected = index == selectedIndex, // 현재 아이템이 선택되었는지 전달
                onItemSelected = { onItemSelected(index) } // 클릭 시 인덱스 반환
            )
        }
    }
}

// 하단 내비게이션 아이템
@Composable
fun NavigationItem(iconResId: Int, text: String, isSelected: Boolean, onItemSelected: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(60.dp)
            .clickable(onClick = onItemSelected)
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit,
            colorFilter = if (isSelected) ColorFilter.tint(Color.Blue) else null
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isSelected) Color.Blue else Color.Black
        )
    }
}

data class Member(
    val name: String,
    val profileImageResId: Int,
    val wallPaperImageResId: List<String>
)

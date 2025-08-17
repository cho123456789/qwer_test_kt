package com.example.qwer_test_kt.gomin

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.Route
import com.example.qwer_test_kt.gomin.view.WallpaperListScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState


val cafe24 = FontFamily(Font(R.font.cafe24decoshadow))
val onePop = FontFamily(Font(R.font.onepop))
val sam = FontFamily(Font(R.font.samliphop))
val members = listOf(
    Member(
        "쵸단 ", R.drawable.gomin_cho_profile,
        listOf(
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjRfMTg5/MDAxNzExMjU2MTU1OTgx.MwMlG3nmyMRbPJlLTAdxdxvKBYvH1hhtlp_RHqYKWr4g.LxcylwxRiVGVtOHPxXcKTw5DdLQIeluzlUF1sP3qNMEg.JPEG/qwer_x_manito_concept_q_2_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjRfODMg/MDAxNzExMjU2MTU1NzUz.-gDWGk24Aii4L4rbiMKfqvsIPsdWdogQApXAA0s1U-sg.CbfvsVwiE8yX-pZ1HSDTut3GDIk7Q_pYeYY61BzJ-jAg.JPEG/qwer_x_manito_concept_q_1_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjRfMjM1/MDAxNzExMjU2MTU1NjIy.W3NNSoSdyqOKV4at2ttzhiML-2Gjc-Irz6WTH88bnnIg._TYxEAhfmCYIM8Im7dbj_vhMlDTt_Y4bAZL_zsl1SEEg.JPEG/qwer_x_manito_concept_q_3_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjRfMzkg/MDAxNzExMjU2MTU1NDg3.neBP6LMXMCr1Y-H658HSpRJyPflNjDZLNACDbjVNZGUg.mkMIXssOBFLYwuJchs4xknGHm0mlYucPaM1_tZnO9VQg.JPEG/qwer_x_manito_concept_q_4_u.jpeg?type=w1600"
        ),
    ),
    Member(
        "마젠타", R.drawable.gomin_ma_profile,
        listOf(
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjVfMTE5/MDAxNzExMzE1NDgxMjAz.qfGPUCEEOSj-NToEhBunZSIsvgWbpRydG1vVoQxMQWMg.Niu8s5NIVKQY8sA1osQQddMCUiZsOb0nPeWmkbV1iBkg.JPEG/qwer_x_manito_concept_w_1_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjVfMjU5/MDAxNzExMzE1NDgxNTQy.J9TI4fA3yOOBBEAdY3VrWzoSGxFdilwJ1AUq1dZSuZog.Xtmq5FtK1eeW6xeNaucrzuNeRko0d-U_3WjkyuEqXiAg.JPEG/qwer_x_manito_concept_w_2_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjVfODAg/MDAxNzExMzE1NDgxNTQw.G6Xw8cW6I2ObFIHB9DMxKubpwjKtw1R7UTskzO35KfYg.2E6wgQ8-QPCPamwMOvbfsOaLA0x_Z_O_3fykCWV6XYEg.JPEG/qwer_x_manito_concept_w_3_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjVfMjA4/MDAxNzExMzE1NDgxMzYw.iWSjsrP07mEUZQaIH-NNU0Ywc0_VXIF7DN8Z_9x_yAcg.OUYI4OsFNLnRm5mZ-Wasn6Hr2tjtss6sOyOZrBtK8ukg.JPEG/qwer_x_manito_concept_w_4_u.jpeg?type=w1600"

        )
    ), Member(
        "히나", R.drawable.gomin_hina_profile,
        listOf(
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjZfMTEy/MDAxNzExNDMxMjQ5MTQz.G8qJ0eYlLdCOufa3AwudnP_W3zOLun7lmh9nA4h7W_Ig.BLTD8qo41MdIu6tDR0SuloyYJh1SOVSMrYQwsDoZQbwg.JPEG/qwer_x_manito_concept_e_1_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjZfMjg4/MDAxNzExNDMxMjQ5MTA0.kYkrEUrh9fTOqcnzS7p4F912bSFmsBfNkPvwd5J9IUcg.itcOVRZRUZMKBHL_WXzfJQSyIytF6XTFUR8Fn2DANDQg.JPEG/qwer_x_manito_concept_e_2_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjZfMTk4/MDAxNzExNDMxMjQ4OTE1.wDoCtKb8rmfKdGHtDyV-jOKRTNwltp_xzmnmJTFQeqQg.9_Y7ffKw5yKUuXLO0mYaDdhUAAOwBStULtrhkweQBZ4g.JPEG/qwer_x_manito_concept_e_3_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjZfMjA3/MDAxNzExNDMxMjQ4NzM5.Qoqu1-w6CHlpTwSx-96YdmggdNcdlhy2mpiTQsmNYpog.ZuTnVc_g_Z5A7252thjBXRrrcF33wd1MnhDfS1L22e0g.JPEG/qwer_x_manito_concept_e_4_u.jpeg?type=w1600"
        )
    ),
    Member(
        "시연", R.drawable.gomin_siyeon_profile,
        listOf(
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjZfMjgg/MDAxNzExNDY0MTQwOTM4.VZHCNsjTu3Zj3dngX9x24ovmrCBzl4DYHnsRiiNmMn4g.BqTuRM0NuABo6YYrDzr6nwy2O2uyQhchCUAF3r4AGJog.JPEG/qwer_x_manito_concept_r_1_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjZfMTc4/MDAxNzExNDY0MTQxMDgx.BQcb7fK1AOzDsdVjvXo533GX1FOugV8f0KBCpz_GByIg.XcDdal42nsRmWQXSi6vdXAbMxFqHqVUXtHSFzX8hYFYg.JPEG/qwer_x_manito_concept_r_2_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjZfMjY0/MDAxNzExNDY0MTQwODY5.kj0_d3eRXSx9YXDFDwOPgSU3guTG_-sfrCPwvpuBS6gg.lL5I2MCVHqbXD77xO2Yb0QO0sEgCrI_yTzHyh3V6xXAg.JPEG/qwer_x_manito_concept_r_3_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjZfMTA5/MDAxNzExNDY0MTQwNjcw.MWKGYXlblk10lnHnRnBTlw0eajHX4abWexAa5WeJ4T4g.VnP-VQqShLIm40qrOvZ-H4KXFwSPm6dBsDKThi7QhxAg.JPEG/qwer_x_manito_concept_r_4_u.jpeg?type=w1600"
        )
    ),
    Member(
        "단체", R.drawable.gomin_group,
        listOf(
            "https://cdnimg.melon.co.kr/resource/image/cds/musicstory/imgUrl20240328060528574.jpg",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjdfMTQw/MDAxNzExNTMxNzg0OTg0.4Z_stj0LyrS2GFPVA4XmWJClxnq-c3t3LkgEz5_SUg0g.c2G46aep_5O6NH_Sh2ZUeMkJoNkyYxp2B-lSS_lIpZUg.JPEG/qwer_weverse_manito_concept_qwer_1_u.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDA0MjlfMTk2/MDAxNzE0MzU0NzY3MTMz.IDwEqUR7pBy3aG4WnJWikCZKT4oPnX963naWqOEB7NYg.WSGxZSjyrE3zZC51RoR1mfc5O8P2PVYndh9iXhu8mF4g.JPEG/IMG_5107.jpeg?type=w1600",
            "https://cafeptthumb-phinf.pstatic.net/MjAyNDAzMjdfMjEz/MDAxNzExNTMxNzg1MTQ3.3OhJev7qTje4tGm9YqooCi8C9IqPDWJSyZAmg2JNA4kg.zkDaJcGH2yt1FZsAsO0a3ZIOmH3m1lVKm2sdFGdfxV4g.JPEG/qwer_weverse_manito_concept_qwer_2_u.jpeg?type=w1600",
        )
    )
)


sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Wallpaper : BottomNavItem("wallpaper_list", Icons.Filled.Home, "배경화면")
    object Widget : BottomNavItem("widget_list", Icons.Filled.List, "위젯")
    object Icon : BottomNavItem("icon_list", Icons.Filled.Settings, "아이콘")
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Composable
fun GominjungdokScreen(navController: NavHostController) {

    val bottomNavController = rememberNavController()

    val allWallpapers = members.flatMap { it.wallPaperImageResId }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA), // 더 밝고 화사한 하늘색
            Color(0xFFE1BEE7)  // 더 밝은 연보라색
        )
    )
    Scaffold(
        bottomBar = { GominBottomNavigation(navController = bottomNavController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "The 1st Mini Album",
                    fontSize = 40.sp,
                    color = Color.Black,
                    fontFamily = sam,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "MANITO",
                    fontSize = 45.sp,
                    color = Color.Magenta,
                    fontFamily = sam,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
            }

            // 하단 내비게이션 탭에 따라 다른 화면을 보여주는 NavHost
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNavItem.Wallpaper.route,
                modifier = Modifier.weight(1f)
            ) {
                composable(BottomNavItem.Wallpaper.route) {
                    WallpaperListScreen(
                        allWallpapers = allWallpapers,
                        onWallpaperClick = { wallpaperUrl ->
                            val encodedUrl = Uri.encode(wallpaperUrl)
                            // 메인 NavController를 사용해 상세 화면으로 이동하며 URL 전달
                            navController.navigate("${Route.Gomin_detail}/$encodedUrl")
                        }
                    )
                }
                composable(BottomNavItem.Widget.route) {
                    EmptyScreen(title = "위젯 목록")
                }
                composable(BottomNavItem.Icon.route) {
                    EmptyScreen(title = "아이콘 목록")
                }
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MemberProfile(
    members: List<Member>,
    pagerState: PagerState,
    onMemberSelected: (Member) -> Unit,
    isClickable: Boolean
) {
    // LazyRow를 사용하여 멤버 프로필을 가로로 스크롤 가능하게 만듭니다.
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(members.size) { index ->
            val member = members[index]
            MemberProfileImage(
                member = member,
                isSelected = index == pagerState.currentPage, // 현재 페이지와 일치하는지 확인
                onClick = {
                    if (isClickable)
                        onMemberSelected(member)
                }
            )
        }
    }
}

@Composable
fun GominBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Wallpaper,
        BottomNavItem.Widget,
        BottomNavItem.Icon
    )
    BottomNavigation(
        backgroundColor = Color(0xFFE1BEE7)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = Color.Magenta,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// 위젯 및 아이콘 탭에 표시할 임시 화면 (기존과 동일)
@Composable
fun EmptyScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$title 화면입니다.",
            fontSize = 24.sp,
            fontFamily = sam
        )
    }
}

@Composable
fun MemberProfileImage(member: Member, isSelected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1.0f,
        animationSpec = spring(dampingRatio = 0.5f)
    )
    val borderWidth: Dp by animateDpAsState(
        targetValue = if (isSelected) 4.dp else 8.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val borderColor = if (isSelected) Color.White else Color.White

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .scale(scale)
    ) {
        Image(
            painter = painterResource(id = member.profileImageResId),
            contentDescription = "${member.name} profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(40.dp))
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(40.dp),
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = member.name,
            fontSize = 14.sp,
            fontFamily = onePop
        )
    }
}

data class Member(
    val name: String,
    val profileImageResId: Int,
    val wallPaperImageResId: List<String>
)

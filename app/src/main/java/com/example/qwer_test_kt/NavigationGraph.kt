package com.example.qwer_test_kt

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.qwer_test_kt.discord.DiscordScreen
import com.example.qwer_test_kt.gomin.GominjungdokScreen
import com.example.qwer_test_kt.gomin.view.WallpaperDetailScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.HOME
    ) {
        composable(Route.HOME) {
            MainScreen(
                navController = navController
            )
        }
        composable(Route.Gominjungdok) {
            GominjungdokScreen(
                navController = navController
            )
        }
        composable(
            route = "${Route.Gomin_detail}/{wallpaperUrl}", // 경로 이름 수정
            arguments = listOf(navArgument("wallpaperUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("wallpaperUrl")
            val wallpaperUrl = Uri.decode(encodedUrl)
            if (wallpaperUrl != null) {
                WallpaperDetailScreen(
                    wallpaperUrl = wallpaperUrl,
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    viewModel = hiltViewModel()
                )
            }
        }
        composable(Route.Discord) {
            DiscordScreen(
                navController = navController
            )
        }
    }

}

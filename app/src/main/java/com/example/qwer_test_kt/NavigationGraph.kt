package com.example.qwer_test_kt

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qwer_test_kt.discord.DiscordScreen
import com.example.qwer_test_kt.gomin.GominjungdokScreen

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
        composable(Route.Discord) {
                DiscordScreen(
                    navController = navController
                )
            }
        }
}

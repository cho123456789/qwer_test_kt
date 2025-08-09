package com.example.qwer_test_kt

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qwer_test_kt.gomin.GominjungdokScreen
import com.example.qwer_test_kt.presentation.GominJungdokViewModel

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
    }
}
package com.example.module6task3.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.module6task3.presentation.viewmodel.MainViewModel
import com.example.module6task3.presentation.ui.LoginScreen
import com.example.module6task3.presentation.ui.UserDetailScreen
import com.example.module6task3.presentation.ui.UserListScreen

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    // 1. Оставляем только одно объявление token
    val token by viewModel.token.collectAsState(initial = null)

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(viewModel) {
                navController.navigate("list") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("list") {
            UserListScreen(viewModel, onUserClick = { userId ->
                navController.navigate("detail/$userId")
            })
        }
        // 2. Добавляем типизацию аргумента для безопасности
        composable(
            "detail/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            // Исправлено: убрана лишняя лямбда (callback), так как в UserDetailScreen её нет
            UserDetailScreen(viewModel, userId)
        }
    }

    // 3. Логика автоматического перехода при изменении токена
    LaunchedEffect(token) {
        if (token == null) {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // Если мы на экране логина и токен появился — переходим в список
            if (navController.currentBackStackEntry?.destination?.route == "login") {
                navController.navigate("list") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
}
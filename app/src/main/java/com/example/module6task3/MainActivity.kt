package com.example.module6task3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.module6task3.data.remote.ApiService
import com.example.module6task3.data.repository.UserRepositoryImpl
import com.example.module6task3.navigation.AppNavigation
import com.example.module6task3.presentation.viewmodel.MainViewModel
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.* // ИМПОРТ ВСЕХ ПЛАГИНОВ LOGGING
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                    coerceInputValues = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                // Исправлено: используем интерфейс Logger из Ktor
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Log.d("HTTP_LOG", message)
                    }
                }
            }
        }

        val apiService = ApiService(client)
        val userRepository = UserRepositoryImpl(applicationContext, apiService)
        val viewModel = MainViewModel(userRepository)

        setContent {
            AppNavigation(viewModel)
        }
    }
}
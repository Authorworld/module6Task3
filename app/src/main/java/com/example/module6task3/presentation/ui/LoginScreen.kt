package com.example.module6task3.presentation.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.module6task3.presentation.viewmodel.AuthState
import com.example.module6task3.presentation.viewmodel.MainViewModel
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun LoginScreen(viewModel: MainViewModel, onSuccess: () -> Unit) {
    val state by viewModel.authState.collectAsState()
    var username by remember { mutableStateOf("emilys") }
    var password by remember { mutableStateOf("emilyspass") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // Увеличил отступ для красоты
        verticalArrangement = Arrangement.Center, // Центрируем по вертикали
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state is AuthState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
        }

        // --- Поле Логин ---
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- Поле Пароль (в том же стиле) ---
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(), // Скрываем текст
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (state is AuthState.Error) {
            Text(
                text = "Ошибка входа: ${(state as AuthState.Error).message}",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.login(username, password) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Войти")
        }
    }

    // Если во ViewModel статус сменился на Success — вызываем переход
    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            onSuccess()
        }
    }
}
package com.example.module6task3.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.module6task3.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(viewModel: MainViewModel, userId: Int) {
    val user by viewModel.selectedUser.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUserById(userId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Профиль") }) }
    ) { padding ->
        user?.let { item ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Центрируем аватарку и кнопку
            ) {
                // Большая аватарка
                AsyncImage(
                    model = item.image,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(200.dp) // Увеличенный размер
                        .padding(bottom = 16.dp)
                )

                // Информация о пользователе
                Text(
                    text = "${item.firstName} ${item.lastName}",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = item.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Кнопка «Выйти»
                Button(
                    onClick = { viewModel.logout() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Выйти")
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
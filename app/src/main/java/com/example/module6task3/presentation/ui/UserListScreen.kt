package com.example.module6task3.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.module6task3.presentation.viewmodel.MainViewModel
import androidx.compose.runtime.getValue // ОБЯЗАТЕЛЬНО

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(viewModel: MainViewModel, onUserClick: (Int) -> Unit) {
    // Наблюдаем за списком пользователей и состоянием из ViewModel
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState() // Нужно добавить во ViewModel
    val errorMessage by viewModel.error.collectAsState() // Нужно добавить во ViewModel

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    TextButton(onClick = { viewModel.logout() }) {
                        Text("Выход", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // 1. Состояние загрузки
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // 2. Состояние ошибки
            errorMessage?.let { message ->
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.loadUsers() }) {
                        Text("Повторить")
                    }
                }
            }

            // 3. Список пользователей (Success)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(users) { user ->
                    ListItem(
                        modifier = Modifier.clickable { onUserClick(user.id) }, // Переход на детали
                        headlineContent = { Text("${user.firstName} ${user.lastName}") },
                        supportingContent = {
                            Column {
                                Text("@${user.username}")
                                Text(user.email, style = MaterialTheme.typography.bodySmall) // Email по ТЗ
                            }
                        },
                        leadingContent = {
                            AsyncImage(
                                model = user.image,
                                contentDescription = "Avatar",
                                modifier = Modifier.size(48.dp).padding(4.dp)
                            )
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
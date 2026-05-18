package com.example.module6task3.domain.repository

import com.example.module6task3.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(username: String, password: String): Result<String> // Возвращаем String (токен)
    suspend fun getUsers(): Result<List<User>>
    suspend fun logout()

    fun getToken(): Flow<String?>
}
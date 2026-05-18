package com.example.module6task3.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.module6task3.data.mapper.toDomain
import com.example.module6task3.data.remote.ApiService
import com.example.module6task3.domain.model.User
import com.example.module6task3.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Расширение для работы с DataStore (вынесено за пределы класса)
private val Context.dataStore by preferencesDataStore(name = "settings")

class UserRepositoryImpl(
    private val context: Context,
    private val apiService: ApiService
) : UserRepository {

    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    // 1. Реализация получения списка пользователей
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = apiService.fetchUsers() // Убедись, что fetchUsers возвращает UserListResponse или List<UserDto>
            // Если fetchUsers возвращает UserListResponse, используй response.users.map...
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 2. Реализация входа (Login)
    override suspend fun login(username: String, password: String): Result<String> {
        return try {
            val userDto = apiService.login(username, password)
            val token = userDto.token ?: throw Exception("Token not found")

            // Исправлено: вызываем внутреннюю suspend функцию для сохранения
            saveToken(token)

            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Вспомогательная функция для сохранения токена в DataStore
    private suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // 3. Реализация выхода (Logout)
    override suspend fun logout() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }

    override fun getToken(): Flow<String?> {
        return context.dataStore.data.map { it[TOKEN_KEY] }
    }
}
package com.example.module6task3.data.remote

import com.example.module6task3.data.model.UserDto
import com.example.module6task3.data.model.UserListResponse // Если создал такой класс
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.request.setBody

class ApiService(private val client: HttpClient) {

    // Функция для получения списка пользователей
    suspend fun fetchUsers(): List<UserDto> {
        return try {
            // Запрос к DummyJSON
            val response: UserListResponse = client.get("https://dummyjson.com/users").body()
            response.users
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun login(u: String, p: String): UserDto {
        return client.post("https://dummyjson.com/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("username" to u, "password" to p))
        }.body() // Здесь Ktor превращает JSON в UserDto
    }
}
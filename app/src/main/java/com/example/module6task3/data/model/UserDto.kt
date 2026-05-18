package com.example.module6task3.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val image: String,
    // Аннотация говорит Ktor искать "accessToken", но в коде оставляем имя "token"
    @SerialName("accessToken")
    val token: String? = null
)

@Serializable
data class UserListResponse(
    val users: List<UserDto>
)
package com.example.module6task3.data.mapper

import com.example.module6task3.data.model.UserDto
import com.example.module6task3.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        username = this.username,
        email = this.email,
        image = this.image
    )
}
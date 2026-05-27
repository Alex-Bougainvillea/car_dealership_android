package com.example.car_dealership_android.data.remote.dto

data class AuthRequestDto(
    val username: String,
    val password: String
)

data class RegisterRequestDto(
    val username: String,
    val password: String,
    val role: String
)

data class AuthResponseDto(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Int,
    val username: String,
    val role: String
)

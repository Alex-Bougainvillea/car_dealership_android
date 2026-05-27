package com.example.car_dealership_android.domain.model

data class User(
    val id: Int,
    val username: String,
    val role: UserRole
)

enum class UserRole {
    ADMIN,
    CLIENT
}

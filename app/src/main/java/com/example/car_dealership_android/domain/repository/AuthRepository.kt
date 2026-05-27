package com.example.car_dealership_android.domain.repository

import com.example.car_dealership_android.domain.model.User

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult
}

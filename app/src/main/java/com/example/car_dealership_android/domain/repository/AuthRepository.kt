package com.example.car_dealership_android.domain.repository

import com.example.car_dealership_android.domain.model.User
import com.example.car_dealership_android.domain.model.UserRole

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult
    suspend fun register(username: String, password: String, role: UserRole): AuthResult
}

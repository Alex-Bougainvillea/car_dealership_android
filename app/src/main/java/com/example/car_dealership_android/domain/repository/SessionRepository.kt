package com.example.car_dealership_android.domain.repository

import com.example.car_dealership_android.domain.model.User
import com.example.car_dealership_android.domain.model.UserRole

interface SessionRepository {
    fun getToken(): String?
    fun getUser(): User?
    fun getRole(): UserRole?
    fun saveSession(token: String, user: User)
    fun clear()
}

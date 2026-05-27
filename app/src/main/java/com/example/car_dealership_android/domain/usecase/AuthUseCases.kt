package com.example.car_dealership_android.domain.usecase

import com.example.car_dealership_android.domain.model.User
import com.example.car_dealership_android.domain.model.UserRole
import com.example.car_dealership_android.domain.repository.AuthRepository
import com.example.car_dealership_android.domain.repository.AuthResult
import com.example.car_dealership_android.domain.repository.SessionRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): AuthResult =
        repository.login(username, password)
}

class GetCurrentUserUseCase(private val sessionRepository: SessionRepository) {
    operator fun invoke(): User? = sessionRepository.getUser()
}

class GetUserRoleUseCase(private val sessionRepository: SessionRepository) {
    operator fun invoke(): UserRole? = sessionRepository.getRole()
}

class ClearSessionUseCase(private val sessionRepository: SessionRepository) {
    operator fun invoke() = sessionRepository.clear()
}

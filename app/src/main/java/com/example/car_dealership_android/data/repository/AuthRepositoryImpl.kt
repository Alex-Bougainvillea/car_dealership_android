package com.example.car_dealership_android.data.repository

import com.example.car_dealership_android.data.mapper.toDomain
import com.example.car_dealership_android.data.remote.CarDealershipApi
import com.example.car_dealership_android.data.remote.dto.AuthRequestDto
import com.example.car_dealership_android.domain.repository.AuthRepository
import com.example.car_dealership_android.domain.repository.AuthResult
import com.example.car_dealership_android.domain.repository.SessionRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: CarDealershipApi,
    private val sessionRepository: SessionRepository
) : AuthRepository {
    override suspend fun login(username: String, password: String): AuthResult =
        runCatching {
            val response = api.login(AuthRequestDto(username, password))
            val user = response.user.toDomain()
            sessionRepository.saveSession(response.token, user)
            AuthResult.Success(user)
        }.getOrElse { AuthResult.Error(it.message ?: "Login failed") }
}

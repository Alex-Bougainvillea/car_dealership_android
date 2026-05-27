package com.example.car_dealership_android.data.remote.dto

data class ClientDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String
)

data class ClientCreateRequestDto(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String
)

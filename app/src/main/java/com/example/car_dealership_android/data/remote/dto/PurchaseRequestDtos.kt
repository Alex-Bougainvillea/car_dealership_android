package com.example.car_dealership_android.data.remote.dto

data class PurchaseRequestDto(
    val id: Int,
    val clientId: Int,
    val carId: Int,
    val createdAt: String
)

data class PurchaseRequestCreateRequestDto(
    val clientId: Int,
    val carId: Int
)

package com.example.car_dealership_android.domain.model

data class PurchaseRequest(
    val id: Int,
    val clientId: Int,
    val carId: Int,
    val createdAt: String
)

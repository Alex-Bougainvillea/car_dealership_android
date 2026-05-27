package com.example.car_dealership_android.data.remote.dto

data class CarDto(
    val id: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val price: Double,
    val status: String,
    val description: String?,
    val posterUrl: String?
)

data class CarCreateRequestDto(
    val brand: String,
    val model: String,
    val year: Int,
    val price: Double,
    val description: String?,
    val posterUrl: String?
)

data class CarUpdateRequestDto(
    val brand: String,
    val model: String,
    val year: Int,
    val price: Double,
    val status: String,
    val description: String?,
    val posterUrl: String?
)

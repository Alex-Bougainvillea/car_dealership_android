package com.example.car_dealership_android.domain.repository

import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.CarStatus

interface CarRepository {
    suspend fun getCars(): List<Car>
    suspend fun getCar(id: Int): Car
    suspend fun createCar(
        brand: String,
        model: String,
        year: Int,
        price: Double,
        description: String?,
        posterUrl: String?
    ): Car

    suspend fun updateCar(
        id: Int,
        brand: String,
        model: String,
        year: Int,
        price: Double,
        status: CarStatus,
        description: String?,
        posterUrl: String?
    ): Car

    suspend fun deleteCar(id: Int)
}

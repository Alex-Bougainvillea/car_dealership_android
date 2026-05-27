package com.example.car_dealership_android.data.repository

import com.example.car_dealership_android.data.mapper.toDomain
import com.example.car_dealership_android.data.remote.CarDealershipApi
import com.example.car_dealership_android.data.remote.dto.CarCreateRequestDto
import com.example.car_dealership_android.data.remote.dto.CarUpdateRequestDto
import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.domain.repository.CarRepository
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val api: CarDealershipApi
) : CarRepository {
    override suspend fun getCars(): List<Car> = api.getCars().map { it.toDomain() }

    override suspend fun getCar(id: Int): Car = api.getCar(id).toDomain()

    override suspend fun createCar(
        brand: String,
        model: String,
        year: Int,
        price: Double,
        description: String?,
        posterUrl: String?
    ): Car = api.createCar(
        CarCreateRequestDto(brand, model, year, price, description, posterUrl)
    ).toDomain()

    override suspend fun updateCar(
        id: Int,
        brand: String,
        model: String,
        year: Int,
        price: Double,
        status: CarStatus,
        description: String?,
        posterUrl: String?
    ): Car = api.updateCar(
        id,
        CarUpdateRequestDto(brand, model, year, price, status.name, description, posterUrl)
    ).toDomain()

    override suspend fun deleteCar(id: Int) {
        api.deleteCar(id)
    }
}

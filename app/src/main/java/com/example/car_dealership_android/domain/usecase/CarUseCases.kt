package com.example.car_dealership_android.domain.usecase

import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.domain.repository.CarRepository

class GetCarsUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(): List<Car> = repository.getCars()
}

class GetCarUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(id: Int): Car = repository.getCar(id)
}

class CreateCarUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(
        brand: String,
        model: String,
        year: Int,
        price: Double,
        description: String?,
        posterUrl: String?
    ): Car = repository.createCar(brand, model, year, price, description, posterUrl)
}

class UpdateCarUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(
        id: Int,
        brand: String,
        model: String,
        year: Int,
        price: Double,
        status: CarStatus,
        description: String?,
        posterUrl: String?
    ): Car = repository.updateCar(id, brand, model, year, price, status, description, posterUrl)
}

class DeleteCarUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(id: Int) = repository.deleteCar(id)
}

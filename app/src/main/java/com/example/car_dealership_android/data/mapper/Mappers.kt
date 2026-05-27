package com.example.car_dealership_android.data.mapper

import com.example.car_dealership_android.data.remote.dto.CarDto
import com.example.car_dealership_android.data.remote.dto.ClientDto
import com.example.car_dealership_android.data.remote.dto.PurchaseRequestDto
import com.example.car_dealership_android.data.remote.dto.UserDto
import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.domain.model.Client
import com.example.car_dealership_android.domain.model.PurchaseRequest
import com.example.car_dealership_android.domain.model.User
import com.example.car_dealership_android.domain.model.UserRole

fun UserDto.toDomain(): User =
    User(
        id = id,
        username = username,
        role = UserRole.valueOf(role)
    )

fun CarDto.toDomain(): Car =
    Car(
        id = id,
        brand = brand,
        model = model,
        year = year,
        price = price,
        status = CarStatus.valueOf(status),
        description = description,
        posterUrl = posterUrl
    )

fun ClientDto.toDomain(): Client =
    Client(
        id = id,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        email = email
    )

fun PurchaseRequestDto.toDomain(): PurchaseRequest =
    PurchaseRequest(
        id = id,
        clientId = clientId,
        carId = carId,
        createdAt = createdAt
    )

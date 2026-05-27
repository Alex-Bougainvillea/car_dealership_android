package com.example.car_dealership_android.domain.usecase

import com.example.car_dealership_android.domain.model.PurchaseRequest
import com.example.car_dealership_android.domain.repository.RequestRepository

class GetRequestsUseCase(private val repository: RequestRepository) {
    suspend operator fun invoke(): List<PurchaseRequest> = repository.getRequests()
}

class CreateRequestUseCase(private val repository: RequestRepository) {
    suspend operator fun invoke(clientId: Int, carId: Int): PurchaseRequest =
        repository.createRequest(clientId, carId)
}

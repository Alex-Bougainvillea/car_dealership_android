package com.example.car_dealership_android.domain.repository

import com.example.car_dealership_android.domain.model.PurchaseRequest

interface RequestRepository {
    suspend fun getRequests(): List<PurchaseRequest>
    suspend fun createRequest(clientId: Int, carId: Int): PurchaseRequest
    suspend fun deleteRequest(id: Int)
}

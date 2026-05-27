package com.example.car_dealership_android.data.repository

import com.example.car_dealership_android.data.mapper.toDomain
import com.example.car_dealership_android.data.remote.CarDealershipApi
import com.example.car_dealership_android.data.remote.dto.PurchaseRequestCreateRequestDto
import com.example.car_dealership_android.domain.model.PurchaseRequest
import com.example.car_dealership_android.domain.repository.RequestRepository
import javax.inject.Inject

class RequestRepositoryImpl @Inject constructor(
    private val api: CarDealershipApi
) : RequestRepository {
    override suspend fun getRequests(): List<PurchaseRequest> =
        api.getRequests().map { it.toDomain() }

    override suspend fun createRequest(clientId: Int, carId: Int): PurchaseRequest =
        api.createRequest(PurchaseRequestCreateRequestDto(clientId, carId)).toDomain()
}

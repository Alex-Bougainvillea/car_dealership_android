package com.example.car_dealership_android.data.repository

import com.example.car_dealership_android.data.mapper.toDomain
import com.example.car_dealership_android.data.remote.CarDealershipApi
import com.example.car_dealership_android.data.remote.dto.PurchaseRequestCreateRequestDto
import com.example.car_dealership_android.data.remote.toReadableNetworkMessage
import com.example.car_dealership_android.domain.model.PurchaseRequest
import com.example.car_dealership_android.domain.repository.RequestRepository
import javax.inject.Inject

class RequestRepositoryImpl @Inject constructor(
    private val api: CarDealershipApi
) : RequestRepository {
    override suspend fun getRequests(): List<PurchaseRequest> =
        runCatching {
            api.getRequests().map { it.toDomain() }
        }.getOrElse { throw IllegalStateException(it.toReadableNetworkMessage("Failed to load requests")) }

    override suspend fun createRequest(clientId: Int, carId: Int): PurchaseRequest =
        runCatching {
            api.createRequest(PurchaseRequestCreateRequestDto(clientId, carId)).toDomain()
        }.getOrElse { throw IllegalStateException(it.toReadableNetworkMessage("Failed to create request")) }

    override suspend fun deleteRequest(id: Int) {
        runCatching {
            api.deleteRequest(id)
        }.getOrElse { throw IllegalStateException(it.toReadableNetworkMessage("Failed to delete request")) }
    }
}

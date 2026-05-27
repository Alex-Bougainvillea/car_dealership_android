package com.example.car_dealership_android.data.repository

import com.example.car_dealership_android.data.mapper.toDomain
import com.example.car_dealership_android.data.remote.CarDealershipApi
import com.example.car_dealership_android.data.remote.dto.ClientCreateRequestDto
import com.example.car_dealership_android.domain.model.Client
import com.example.car_dealership_android.domain.repository.ClientRepository
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val api: CarDealershipApi
) : ClientRepository {
    override suspend fun getClients(): List<Client> = api.getClients().map { it.toDomain() }

    override suspend fun createClient(
        firstName: String,
        lastName: String,
        phone: String,
        email: String
    ): Client = api.createClient(ClientCreateRequestDto(firstName, lastName, phone, email)).toDomain()
}

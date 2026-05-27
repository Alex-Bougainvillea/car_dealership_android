package com.example.car_dealership_android.domain.usecase

import com.example.car_dealership_android.domain.model.Client
import com.example.car_dealership_android.domain.repository.ClientRepository

class GetClientsUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(): List<Client> = repository.getClients()
}

class CreateClientUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phone: String,
        email: String
    ): Client = repository.createClient(firstName, lastName, phone, email)
}

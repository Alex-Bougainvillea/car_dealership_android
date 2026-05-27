package com.example.car_dealership_android.domain.repository

import com.example.car_dealership_android.domain.model.Client

interface ClientRepository {
    suspend fun getClients(): List<Client>
    suspend fun createClient(
        firstName: String,
        lastName: String,
        phone: String,
        email: String
    ): Client
}

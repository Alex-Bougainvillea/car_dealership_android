package com.example.car_dealership_android.di

import com.example.car_dealership_android.data.repository.AuthRepositoryImpl
import com.example.car_dealership_android.data.repository.CarRepositoryImpl
import com.example.car_dealership_android.data.repository.ClientRepositoryImpl
import com.example.car_dealership_android.data.repository.RequestRepositoryImpl
import com.example.car_dealership_android.domain.repository.AuthRepository
import com.example.car_dealership_android.domain.repository.CarRepository
import com.example.car_dealership_android.domain.repository.ClientRepository
import com.example.car_dealership_android.domain.repository.RequestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCarRepository(impl: CarRepositoryImpl): CarRepository

    @Binds
    @Singleton
    abstract fun bindClientRepository(impl: ClientRepositoryImpl): ClientRepository

    @Binds
    @Singleton
    abstract fun bindRequestRepository(impl: RequestRepositoryImpl): RequestRepository
}

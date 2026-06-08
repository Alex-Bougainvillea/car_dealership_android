package com.example.car_dealership_android.di

import com.example.car_dealership_android.domain.repository.AuthRepository
import com.example.car_dealership_android.domain.repository.CarRepository
import com.example.car_dealership_android.domain.repository.ClientRepository
import com.example.car_dealership_android.domain.repository.RequestRepository
import com.example.car_dealership_android.domain.repository.SessionRepository
import com.example.car_dealership_android.domain.usecase.ClearSessionUseCase
import com.example.car_dealership_android.domain.usecase.CreateCarUseCase
import com.example.car_dealership_android.domain.usecase.CreateClientUseCase
import com.example.car_dealership_android.domain.usecase.CreateRequestUseCase
import com.example.car_dealership_android.domain.usecase.DeleteCarUseCase
import com.example.car_dealership_android.domain.usecase.DeleteRequestUseCase
import com.example.car_dealership_android.domain.usecase.GetCarUseCase
import com.example.car_dealership_android.domain.usecase.GetCarsUseCase
import com.example.car_dealership_android.domain.usecase.GetClientsUseCase
import com.example.car_dealership_android.domain.usecase.GetCurrentUserUseCase
import com.example.car_dealership_android.domain.usecase.GetRequestsUseCase
import com.example.car_dealership_android.domain.usecase.GetUserRoleUseCase
import com.example.car_dealership_android.domain.usecase.LoginUseCase
import com.example.car_dealership_android.domain.usecase.RegisterUseCase
import com.example.car_dealership_android.domain.usecase.UpdateCarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideLoginUseCase(repository: AuthRepository) = LoginUseCase(repository)

    @Provides
    fun provideRegisterUseCase(repository: AuthRepository) = RegisterUseCase(repository)

    @Provides
    fun provideGetCurrentUserUseCase(repository: SessionRepository) = GetCurrentUserUseCase(repository)

    @Provides
    fun provideGetUserRoleUseCase(repository: SessionRepository) = GetUserRoleUseCase(repository)

    @Provides
    fun provideClearSessionUseCase(repository: SessionRepository) = ClearSessionUseCase(repository)

    @Provides
    fun provideGetCarsUseCase(repository: CarRepository) = GetCarsUseCase(repository)

    @Provides
    fun provideGetCarUseCase(repository: CarRepository) = GetCarUseCase(repository)

    @Provides
    fun provideCreateCarUseCase(repository: CarRepository) = CreateCarUseCase(repository)

    @Provides
    fun provideUpdateCarUseCase(repository: CarRepository) = UpdateCarUseCase(repository)

    @Provides
    fun provideDeleteCarUseCase(repository: CarRepository) = DeleteCarUseCase(repository)

    @Provides
    fun provideGetClientsUseCase(repository: ClientRepository) = GetClientsUseCase(repository)

    @Provides
    fun provideCreateClientUseCase(repository: ClientRepository) = CreateClientUseCase(repository)

    @Provides
    fun provideGetRequestsUseCase(repository: RequestRepository) = GetRequestsUseCase(repository)

    @Provides
    fun provideCreateRequestUseCase(repository: RequestRepository) = CreateRequestUseCase(repository)

    @Provides
    fun provideDeleteRequestUseCase(repository: RequestRepository) = DeleteRequestUseCase(repository)
}

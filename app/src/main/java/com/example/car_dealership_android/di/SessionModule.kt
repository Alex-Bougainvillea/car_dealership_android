package com.example.car_dealership_android.di

import com.example.car_dealership_android.data.local.SessionStorage
import com.example.car_dealership_android.domain.repository.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {
    @Binds
    @Singleton
    abstract fun bindSessionRepository(storage: SessionStorage): SessionRepository
}

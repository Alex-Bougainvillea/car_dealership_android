package com.example.car_dealership_android

import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.domain.model.UserRole
import com.example.car_dealership_android.domain.repository.CarRepository
import com.example.car_dealership_android.domain.repository.SessionRepository
import com.example.car_dealership_android.domain.usecase.DeleteCarUseCase
import com.example.car_dealership_android.domain.usecase.GetCarsUseCase
import com.example.car_dealership_android.domain.usecase.GetUserRoleUseCase
import com.example.car_dealership_android.presentation.viewmodel.CarsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CarsViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun filtersCarsByBrandAndStatus() = runTest {
        val cars = listOf(
            Car(1, "Toyota", "Camry", 2020, 10000.0, CarStatus.AVAILABLE, null, null),
            Car(2, "BMW", "X5", 2019, 20000.0, CarStatus.SOLD, null, null)
        )
        val repository = FakeCarRepository(cars)
        val viewModel = CarsViewModel(
            getCarsUseCase = GetCarsUseCase(repository),
            deleteCarUseCase = DeleteCarUseCase(repository),
            getUserRoleUseCase = GetUserRoleUseCase(FakeSessionRepository())
        )

        viewModel.loadCars()
        advanceUntilIdle()

        viewModel.updateBrandFilter("toy")
        viewModel.updateStatusFilter(CarStatus.AVAILABLE)
        val filtered = viewModel.filteredCars()

        assertEquals(1, filtered.size)
        assertEquals("Toyota", filtered.first().brand)
    }
}

private class FakeCarRepository(
    private val cars: List<Car>
) : CarRepository {
    override suspend fun getCars(): List<Car> = cars
    override suspend fun getCar(id: Int): Car = cars.first { it.id == id }
    override suspend fun createCar(
        brand: String,
        model: String,
        year: Int,
        price: Double,
        description: String?,
        posterUrl: String?
    ): Car = cars.first()
    override suspend fun updateCar(
        id: Int,
        brand: String,
        model: String,
        year: Int,
        price: Double,
        status: CarStatus,
        description: String?,
        posterUrl: String?
    ): Car = cars.first()
    override suspend fun deleteCar(id: Int) {}
}

private class FakeSessionRepository : SessionRepository {
    override fun getToken(): String? = null
    override fun getUser() = null
    override fun getRole() = UserRole.ADMIN
    override fun saveSession(token: String, user: com.example.car_dealership_android.domain.model.User) {}
    override fun clear() {}
}

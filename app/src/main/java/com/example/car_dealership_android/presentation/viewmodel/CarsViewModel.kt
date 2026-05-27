package com.example.car_dealership_android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.domain.model.UserRole
import com.example.car_dealership_android.domain.usecase.DeleteCarUseCase
import com.example.car_dealership_android.domain.usecase.GetCarsUseCase
import com.example.car_dealership_android.domain.usecase.GetUserRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CarsUiState(
    val cars: List<Car> = emptyList(),
    val brandFilter: String = "",
    val statusFilter: CarStatus? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAdmin: Boolean = false
)

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val getCarsUseCase: GetCarsUseCase,
    private val deleteCarUseCase: DeleteCarUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        CarsUiState(isAdmin = getUserRoleUseCase() == UserRole.ADMIN)
    )
    val state: StateFlow<CarsUiState> = _state

    fun loadCars() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching { getCarsUseCase() }
                .onSuccess { cars ->
                    _state.update { it.copy(isLoading = false, cars = cars) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun updateBrandFilter(value: String) {
        _state.update { it.copy(brandFilter = value) }
    }

    fun updateStatusFilter(value: CarStatus?) {
        _state.update { it.copy(statusFilter = value) }
    }

    fun deleteCar(id: Int) {
        viewModelScope.launch {
            runCatching { deleteCarUseCase(id) }
                .onSuccess { loadCars() }
                .onFailure { error ->
                    _state.update { it.copy(error = error.message) }
                }
        }
    }

    fun filteredCars(): List<Car> {
        val current = _state.value
        return current.cars.filter { car ->
            val brandOk = current.brandFilter.isBlank() ||
                car.brand.contains(current.brandFilter, ignoreCase = true)
            val statusOk = current.statusFilter == null || car.status == current.statusFilter
            brandOk && statusOk
        }
    }
}

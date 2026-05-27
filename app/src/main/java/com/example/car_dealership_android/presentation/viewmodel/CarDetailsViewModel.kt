package com.example.car_dealership_android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.UserRole
import com.example.car_dealership_android.domain.usecase.GetCarUseCase
import com.example.car_dealership_android.domain.usecase.GetUserRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CarDetailsUiState(
    val car: Car? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAdmin: Boolean = false
)

@HiltViewModel
class CarDetailsViewModel @Inject constructor(
    private val getCarUseCase: GetCarUseCase,
    getUserRoleUseCase: GetUserRoleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        CarDetailsUiState(isAdmin = getUserRoleUseCase() == UserRole.ADMIN)
    )
    val state: StateFlow<CarDetailsUiState> = _state

    fun loadCar(id: Int) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching { getCarUseCase(id) }
                .onSuccess { car ->
                    _state.update { it.copy(isLoading = false, car = car) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}

package com.example.car_dealership_android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.domain.usecase.CreateCarUseCase
import com.example.car_dealership_android.domain.usecase.GetCarUseCase
import com.example.car_dealership_android.domain.usecase.UpdateCarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CarFormUiState(
    val carId: Int? = null,
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val price: String = "",
    val status: CarStatus = CarStatus.AVAILABLE,
    val description: String = "",
    val posterUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class CarFormViewModel @Inject constructor(
    private val getCarUseCase: GetCarUseCase,
    private val createCarUseCase: CreateCarUseCase,
    private val updateCarUseCase: UpdateCarUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CarFormUiState())
    val state: StateFlow<CarFormUiState> = _state

    fun loadCar(id: Int) {
        _state.update { it.copy(isLoading = true, error = null, carId = id) }
        viewModelScope.launch {
            runCatching { getCarUseCase(id) }
                .onSuccess { car ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            brand = car.brand,
                            model = car.model,
                            year = car.year.toString(),
                            price = car.price.toString(),
                            status = car.status,
                            description = car.description.orEmpty(),
                            posterUrl = car.posterUrl.orEmpty()
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun updateBrand(value: String) = _state.update { it.copy(brand = value) }
    fun updateModel(value: String) = _state.update { it.copy(model = value) }
    fun updateYear(value: String) = _state.update { it.copy(year = value) }
    fun updatePrice(value: String) = _state.update { it.copy(price = value) }
    fun updateStatus(value: CarStatus) = _state.update { it.copy(status = value) }
    fun updateDescription(value: String) = _state.update { it.copy(description = value) }
    fun updatePosterUrl(value: String) = _state.update { it.copy(posterUrl = value) }

    fun submit() {
        val current = _state.value
        val year = current.year.toIntOrNull()
        val price = current.price.toDoubleOrNull()
        if (current.brand.isBlank() || current.model.isBlank() || year == null || price == null) {
            _state.update { it.copy(error = "Заполните все обязательные поля") }
            return
        }
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = runCatching {
                if (current.carId == null) {
                    createCarUseCase(
                        brand = current.brand,
                        model = current.model,
                        year = year,
                        price = price,
                        description = current.description.ifBlank { null },
                        posterUrl = current.posterUrl.ifBlank { null }
                    )
                } else {
                    updateCarUseCase(
                        id = current.carId,
                        brand = current.brand,
                        model = current.model,
                        year = year,
                        price = price,
                        status = current.status,
                        description = current.description.ifBlank { null },
                        posterUrl = current.posterUrl.ifBlank { null }
                    )
                }
            }
            result.onSuccess {
                _state.update { it.copy(isLoading = false, success = true) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }
}

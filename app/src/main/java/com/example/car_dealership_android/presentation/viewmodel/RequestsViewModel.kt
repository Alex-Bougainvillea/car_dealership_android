package com.example.car_dealership_android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.car_dealership_android.domain.model.PurchaseRequest
import com.example.car_dealership_android.domain.usecase.CreateRequestUseCase
import com.example.car_dealership_android.domain.usecase.GetRequestsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RequestsUiState(
    val requests: List<PurchaseRequest> = emptyList(),
    val clientId: String = "",
    val carId: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val getRequestsUseCase: GetRequestsUseCase,
    private val createRequestUseCase: CreateRequestUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RequestsUiState())
    val state: StateFlow<RequestsUiState> = _state

    fun loadRequests() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching { getRequestsUseCase() }
                .onSuccess { requests ->
                    _state.update { it.copy(isLoading = false, requests = requests) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun updateClientId(value: String) = _state.update { it.copy(clientId = value) }
    fun updateCarId(value: String) = _state.update { it.copy(carId = value) }

    fun createRequest() {
        val current = _state.value
        val clientId = current.clientId.toIntOrNull()
        val carId = current.carId.toIntOrNull()
        if (clientId == null || carId == null) {
            _state.update { it.copy(error = "Введите корректные ID") }
            return
        }
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching { createRequestUseCase(clientId, carId) }
                .onSuccess {
                    _state.update { it.copy(isLoading = false, clientId = "", carId = "") }
                    loadRequests()
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}

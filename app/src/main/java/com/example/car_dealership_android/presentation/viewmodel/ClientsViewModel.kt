package com.example.car_dealership_android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.car_dealership_android.domain.model.Client
import com.example.car_dealership_android.domain.usecase.CreateClientUseCase
import com.example.car_dealership_android.domain.usecase.GetClientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClientsUiState(
    val clients: List<Client> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val getClientsUseCase: GetClientsUseCase,
    private val createClientUseCase: CreateClientUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ClientsUiState())
    val state: StateFlow<ClientsUiState> = _state

    fun loadClients() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching { getClientsUseCase() }
                .onSuccess { clients ->
                    _state.update { it.copy(isLoading = false, clients = clients) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun updateFirstName(value: String) = _state.update { it.copy(firstName = value) }
    fun updateLastName(value: String) = _state.update { it.copy(lastName = value) }
    fun updatePhone(value: String) = _state.update { it.copy(phone = value) }
    fun updateEmail(value: String) = _state.update { it.copy(email = value) }

    fun createClient() {
        val current = _state.value
        if (current.firstName.isBlank() || current.lastName.isBlank()) {
            _state.update { it.copy(error = "Введите имя и фамилию") }
            return
        }
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching {
                createClientUseCase(
                    firstName = current.firstName,
                    lastName = current.lastName,
                    phone = current.phone,
                    email = current.email
                )
            }.onSuccess {
                _state.update {
                    it.copy(
                        isLoading = false,
                        firstName = "",
                        lastName = "",
                        phone = "",
                        email = ""
                    )
                }
                loadClients()
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }
}

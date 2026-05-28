package com.example.car_dealership_android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.car_dealership_android.domain.model.User
import com.example.car_dealership_android.domain.model.UserRole
import com.example.car_dealership_android.domain.repository.AuthResult
import com.example.car_dealership_android.domain.usecase.ClearSessionUseCase
import com.example.car_dealership_android.domain.usecase.GetCurrentUserUseCase
import com.example.car_dealership_android.domain.usecase.LoginUseCase
import com.example.car_dealership_android.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val role: UserRole = UserRole.CLIENT,
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val clearSessionUseCase: ClearSessionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState(user = getCurrentUserUseCase()))
    val state: StateFlow<AuthUiState> = _state

    fun onUsernameChange(value: String) {
        _state.update { it.copy(username = value) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value) }
    }

    fun onRoleChange(value: UserRole) {
        _state.update { it.copy(role = value) }
    }

    fun login() {
        val current = _state.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _state.update { it.copy(error = "Введите логин и пароль") }
            return
        }
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = loginUseCase(current.username, current.password)) {
                is AuthResult.Success -> _state.update {
                    it.copy(isLoading = false, user = result.user, password = "")
                }
                is AuthResult.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun register() {
        val current = _state.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _state.update { it.copy(error = "Введите логин и пароль") }
            return
        }
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = registerUseCase(current.username, current.password, current.role)) {
                is AuthResult.Success -> _state.update {
                    it.copy(isLoading = false, user = result.user, password = "")
                }
                is AuthResult.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun logout() {
        clearSessionUseCase()
        _state.update { it.copy(user = null) }
    }
}

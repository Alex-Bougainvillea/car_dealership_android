package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.domain.model.UserRole
import com.example.car_dealership_android.presentation.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    onCars: () -> Unit,
    onClients: () -> Unit,
    onRequests: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isAdmin = state.user?.role == UserRole.ADMIN
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Главное меню")
        Button(onClick = onCars) { Text("Автомобили") }
        if (isAdmin) {
            Button(onClick = onClients) { Text("Клиенты") }
            Button(onClick = onRequests) { Text("Заявки") }
        }
        Button(onClick = {
            viewModel.logout()
            onLogout()
        }) {
            Text("Выйти")
        }
    }
}

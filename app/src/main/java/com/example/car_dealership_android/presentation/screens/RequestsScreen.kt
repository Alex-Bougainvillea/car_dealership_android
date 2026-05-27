package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.presentation.viewmodel.RequestsViewModel

@Composable
fun RequestsScreen(
    viewModel: RequestsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRequests()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Заявки")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.requests) { request ->
                Text(text = "Клиент ${request.clientId} → Авто ${request.carId}")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Создать заявку")
        OutlinedTextField(
            value = state.clientId,
            onValueChange = viewModel::updateClientId,
            label = { Text("ID клиента") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.carId,
            onValueChange = viewModel::updateCarId,
            label = { Text("ID авто") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = viewModel::createRequest) {
            Text(text = if (state.isLoading) "Создание..." else "Создать")
        }
        state.error?.let { Text(text = it) }
    }
}

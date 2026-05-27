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
import com.example.car_dealership_android.presentation.viewmodel.ClientsViewModel

@Composable
fun ClientsScreen(
    viewModel: ClientsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadClients()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Клиенты")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.clients) { client ->
                Text(text = "${client.firstName} ${client.lastName} • ${client.phone}")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Добавить клиента")
        OutlinedTextField(
            value = state.firstName,
            onValueChange = viewModel::updateFirstName,
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.lastName,
            onValueChange = viewModel::updateLastName,
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.phone,
            onValueChange = viewModel::updatePhone,
            label = { Text("Телефон") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::updateEmail,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = viewModel::createClient) {
            Text(text = if (state.isLoading) "Сохранение..." else "Создать")
        }
        state.error?.let { Text(text = it) }
    }
}

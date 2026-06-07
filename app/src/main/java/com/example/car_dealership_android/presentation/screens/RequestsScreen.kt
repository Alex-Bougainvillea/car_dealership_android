package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.domain.model.PurchaseRequest
import com.example.car_dealership_android.presentation.components.AnimatedContentContainer
import com.example.car_dealership_android.presentation.components.CardShape
import com.example.car_dealership_android.presentation.components.FieldShape
import com.example.car_dealership_android.presentation.components.LoadingButton
import com.example.car_dealership_android.presentation.components.ScreenTitle
import com.example.car_dealership_android.presentation.components.dealershipTextFieldColors
import com.example.car_dealership_android.presentation.viewmodel.RequestsUiState
import com.example.car_dealership_android.presentation.viewmodel.RequestsViewModel
import com.example.car_dealership_android.ui.theme.Car_dealership_androidTheme

@Composable
fun RequestsScreen(
    viewModel: RequestsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRequests()
    }

    RequestsContent(
        state = state,
        onClientId = viewModel::updateClientId,
        onCarId = viewModel::updateCarId,
        onCreate = viewModel::createRequest
    )
}

@Composable
private fun RequestsContent(
    state: RequestsUiState,
    onClientId: (String) -> Unit,
    onCarId: (String) -> Unit,
    onCreate: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        AnimatedContentContainer {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { ScreenTitle(title = "Заявки", subtitle = "Всего: ${state.requests.size}") }
                items(state.requests, key = { it.id }) { request ->
                    RequestCard(request = request)
                }
                item {
                    NewRequestCard(
                        state = state,
                        onClientId = onClientId,
                        onCarId = onCarId,
                        onCreate = onCreate
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestCard(request: PurchaseRequest) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("Заявка #${request.id}", style = MaterialTheme.typography.titleMedium)
                Text("Клиент ID: ${request.clientId}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Авто ID: ${request.carId}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(request.createdAt, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun NewRequestCard(
    state: RequestsUiState,
    onClientId: (String) -> Unit,
    onCarId: (String) -> Unit,
    onCreate: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Создать заявку", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = state.clientId,
                onValueChange = onClientId,
                label = { Text("ID клиента") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = FieldShape,
                colors = dealershipTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.carId,
                onValueChange = onCarId,
                label = { Text("ID авто") },
                leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = FieldShape,
                colors = dealershipTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            LoadingButton("Создать", state.isLoading, onCreate, Modifier.fillMaxWidth(), Icons.Default.Save)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RequestsScreenPreview() {
    Car_dealership_androidTheme {
        RequestsContent(
            state = RequestsUiState(
                requests = listOf(
                    PurchaseRequest(1, 3, 7, "2026-06-07T12:00"),
                    PurchaseRequest(2, 4, 8, "2026-06-07T12:30")
                )
            ),
            onClientId = {},
            onCarId = {},
            onCreate = {}
        )
    }
}

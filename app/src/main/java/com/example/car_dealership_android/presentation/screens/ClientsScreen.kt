package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.domain.model.Client
import com.example.car_dealership_android.presentation.components.AnimatedContentContainer
import com.example.car_dealership_android.presentation.components.CardShape
import com.example.car_dealership_android.presentation.components.FieldShape
import com.example.car_dealership_android.presentation.components.LoadingButton
import com.example.car_dealership_android.presentation.components.ScreenTitle
import com.example.car_dealership_android.presentation.components.dealershipTextFieldColors
import com.example.car_dealership_android.presentation.viewmodel.ClientsUiState
import com.example.car_dealership_android.presentation.viewmodel.ClientsViewModel
import com.example.car_dealership_android.ui.theme.Car_dealership_androidTheme

@Composable
fun ClientsScreen(
    viewModel: ClientsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadClients()
    }

    ClientsContent(
        state = state,
        onFirstName = viewModel::updateFirstName,
        onLastName = viewModel::updateLastName,
        onPhone = viewModel::updatePhone,
        onEmail = viewModel::updateEmail,
        onCreate = viewModel::createClient
    )
}

@Composable
private fun ClientsContent(
    state: ClientsUiState,
    onFirstName: (String) -> Unit,
    onLastName: (String) -> Unit,
    onPhone: (String) -> Unit,
    onEmail: (String) -> Unit,
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
                item {
                    ScreenTitle(title = "Клиенты", subtitle = "Всего: ${state.clients.size}")
                }
                items(state.clients, key = { it.id }) { client ->
                    ClientCard(client = client)
                }
                item {
                    NewClientCard(
                        state = state,
                        onFirstName = onFirstName,
                        onLastName = onLastName,
                        onPhone = onPhone,
                        onEmail = onEmail,
                        onCreate = onCreate
                    )
                }
            }
        }
    }
}

@Composable
private fun ClientCard(client: Client) {
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
            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("Клиент #${client.id}", color = MaterialTheme.colorScheme.secondary)
                Text("${client.firstName} ${client.lastName}", style = MaterialTheme.typography.titleMedium)
                Text(client.phone, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(client.email, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun NewClientCard(
    state: ClientsUiState,
    onFirstName: (String) -> Unit,
    onLastName: (String) -> Unit,
    onPhone: (String) -> Unit,
    onEmail: (String) -> Unit,
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
            Text("Новый клиент", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(state.firstName, onFirstName, Modifier.fillMaxWidth(), label = { Text("Имя") }, leadingIcon = { Icon(Icons.Default.Person, null) }, shape = FieldShape, colors = dealershipTextFieldColors())
            OutlinedTextField(state.lastName, onLastName, Modifier.fillMaxWidth(), label = { Text("Фамилия") }, leadingIcon = { Icon(Icons.Default.PersonOutline, null) }, shape = FieldShape, colors = dealershipTextFieldColors())
            OutlinedTextField(state.phone, onPhone, Modifier.fillMaxWidth(), label = { Text("Телефон") }, leadingIcon = { Icon(Icons.Default.Phone, null) }, shape = FieldShape, colors = dealershipTextFieldColors())
            OutlinedTextField(state.email, onEmail, Modifier.fillMaxWidth(), label = { Text("Email") }, leadingIcon = { Icon(Icons.Default.Email, null) }, shape = FieldShape, colors = dealershipTextFieldColors())
            LoadingButton("Создать", state.isLoading, onCreate, Modifier.fillMaxWidth(), Icons.Default.Save)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientsScreenPreview() {
    Car_dealership_androidTheme {
        ClientsContent(
            state = ClientsUiState(
                clients = listOf(
                    Client(1, "Иван", "Петров", "+7 900 111-22-33", "ivan@mail.ru"),
                    Client(2, "Анна", "Смирнова", "+7 901 222-33-44", "anna@mail.ru")
                )
            ),
            onFirstName = {},
            onLastName = {},
            onPhone = {},
            onEmail = {},
            onCreate = {}
        )
    }
}

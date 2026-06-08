package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.presentation.components.AnimatedContentContainer
import com.example.car_dealership_android.presentation.components.CarImage
import com.example.car_dealership_android.presentation.components.InfoCard
import com.example.car_dealership_android.presentation.components.StatusChip
import com.example.car_dealership_android.presentation.viewmodel.CarDetailsUiState
import com.example.car_dealership_android.presentation.viewmodel.CarDetailsViewModel
import com.example.car_dealership_android.ui.theme.Car_dealership_androidTheme
import com.example.car_dealership_android.ui.theme.DealershipTextStyles

@Composable
fun CarDetailsScreen(
    carId: Int,
    onEdit: (Int) -> Unit,
    viewModel: CarDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(carId) {
        viewModel.loadCar(carId)
    }

    CarDetailsContent(state = state, onEdit = onEdit)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarDetailsContent(
    state: CarDetailsUiState,
    onEdit: (Int) -> Unit
) {
    val car = state.car
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(car?.let { "${it.brand} ${it.model}" } ?: "Автомобиль") }
            )
        }
    ) { padding ->
        AnimatedContentContainer {
            when {
                state.isLoading && car == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                car == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = state.error ?: "Автомобиль не найден")
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CarImage(
                            posterUrl = car.posterUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 10f)
                        )
                        Text(
                            text = "Авто #${car.id}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(text = "${car.brand} ${car.model}", style = MaterialTheme.typography.headlineMedium)
                        Text(
                            text = "%,.0f ₽".format(car.price),
                            style = DealershipTextStyles.Price,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            StatusChip(status = car.status)
                            AssistChip(
                                onClick = {},
                                label = { Text("${car.year} год") },
                                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) }
                            )
                        }
                        InfoCard(
                            icon = Icons.Default.Info,
                            title = "Описание",
                            body = car.description?.takeIf { it.isNotBlank() } ?: "Описание пока не добавлено"
                        )
                        if (state.isAdmin) {
                            OutlinedButton(
                                onClick = { onEdit(car.id) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                                Text(text = "Редактировать", modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailsScreenPreview() {
    Car_dealership_androidTheme {
        CarDetailsContent(
            state = CarDetailsUiState(
                car = Car(
                    id = 1,
                    brand = "Toyota",
                    model = "Camry",
                    year = 2022,
                    price = 2950000.0,
                    status = CarStatus.AVAILABLE,
                    description = "Комфортный седан бизнес-класса в отличном состоянии.",
                    posterUrl = null
                ),
                isAdmin = true
            ),
            onEdit = {}
        )
    }
}

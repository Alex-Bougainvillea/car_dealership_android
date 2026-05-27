package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.presentation.viewmodel.CarDetailsViewModel

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

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val car = state.car
        if (car == null) {
            Text(text = state.error ?: "Загрузка...")
        } else {
            Text(text = "${car.brand} ${car.model}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Год: ${car.year}")
            Text(text = "Цена: ${car.price}")
            Text(text = "Статус: ${car.status.name}")
            car.description?.let { Text(text = "Описание: $it") }
            car.posterUrl?.let { Text(text = "Изображение: $it") }
            Spacer(modifier = Modifier.height(12.dp))
            if (state.isAdmin) {
                Button(onClick = { onEdit(car.id) }) { Text("Редактировать") }
            }
        }
    }
}

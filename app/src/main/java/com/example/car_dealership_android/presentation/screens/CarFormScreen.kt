package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.presentation.viewmodel.CarFormViewModel

@Composable
fun CarFormScreen(
    carId: Int?,
    onDone: () -> Unit,
    viewModel: CarFormViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(carId) {
        if (carId != null) {
            viewModel.loadCar(carId)
        }
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            onDone()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = if (carId == null) "Добавить авто" else "Редактировать авто")
        OutlinedTextField(
            value = state.brand,
            onValueChange = viewModel::updateBrand,
            label = { Text("Бренд") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.model,
            onValueChange = viewModel::updateModel,
            label = { Text("Модель") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.year,
            onValueChange = viewModel::updateYear,
            label = { Text("Год") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.price,
            onValueChange = viewModel::updatePrice,
            label = { Text("Цена") },
            modifier = Modifier.fillMaxWidth()
        )
        if (carId != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.updateStatus(CarStatus.AVAILABLE) }) { Text("В наличии") }
                Button(onClick = { viewModel.updateStatus(CarStatus.SOLD) }) { Text("Продано") }
            }
        }
        OutlinedTextField(
            value = state.description,
            onValueChange = viewModel::updateDescription,
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.posterUrl,
            onValueChange = viewModel::updatePosterUrl,
            label = { Text("URL изображения") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = viewModel::submit) {
            Text(text = if (state.isLoading) "Сохранение..." else "Сохранить")
        }
        state.error?.let { Text(text = it) }
    }
}

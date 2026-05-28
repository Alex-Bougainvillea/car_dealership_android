package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.presentation.viewmodel.CarsViewModel

@Composable
fun CarsScreen(
    onCarClick: (Int) -> Unit,
    onAddCar: () -> Unit,
    onEditCar: (Int) -> Unit,
    viewModel: CarsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCars()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Автомобили")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.brandFilter,
            onValueChange = viewModel::updateBrandFilter,
            label = { Text("Фильтр по бренду") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.minPriceFilter,
                onValueChange = viewModel::updateMinPriceFilter,
                label = { Text("Цена от") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = state.maxPriceFilter,
                onValueChange = viewModel::updateMaxPriceFilter,
                label = { Text("Цена до") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.updateStatusFilter(null) }) { Text("Все") }
            Button(onClick = { viewModel.updateStatusFilter(CarStatus.AVAILABLE) }) { Text("В наличии") }
            Button(onClick = { viewModel.updateStatusFilter(CarStatus.SOLD) }) { Text("Продано") }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (state.isAdmin) {
            Button(onClick = onAddCar) { Text("Добавить авто") }
            Spacer(modifier = Modifier.height(12.dp))
        }
        state.error?.let { Text(text = it) }
        val cars = viewModel.filteredCars()
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(cars) { car ->
                CarItem(
                    car = car,
                    isAdmin = state.isAdmin,
                    onOpen = { onCarClick(car.id) },
                    onEdit = { onEditCar(car.id) },
                    onDelete = { viewModel.deleteCar(car.id) }
                )
                Divider()
            }
        }
    }
}

@Composable
private fun CarItem(
    car: Car,
    isAdmin: Boolean,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "${car.brand} ${car.model} (${car.year})")
        Text(text = "Цена: ${car.price}")
        Text(text = "Статус: ${car.status.name}")
        Row {
            Button(onClick = onOpen) { Text("Подробнее") }
            Spacer(modifier = Modifier.width(8.dp))
            if (isAdmin) {
                Button(onClick = onEdit) { Text("Редактировать") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete) { Text("Удалить") }
            }
        }
    }
}

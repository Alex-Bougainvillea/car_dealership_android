package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DriveEta
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ModelTraining
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.presentation.components.AnimatedContentContainer
import com.example.car_dealership_android.presentation.components.FieldShape
import com.example.car_dealership_android.presentation.components.LoadingButton
import com.example.car_dealership_android.presentation.components.dealershipTextFieldColors
import com.example.car_dealership_android.presentation.viewmodel.CarFormUiState
import com.example.car_dealership_android.presentation.viewmodel.CarFormViewModel
import com.example.car_dealership_android.ui.theme.Car_dealership_androidTheme

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

    CarFormContent(
        carId = carId,
        state = state,
        onBrand = viewModel::updateBrand,
        onModel = viewModel::updateModel,
        onYear = viewModel::updateYear,
        onPrice = viewModel::updatePrice,
        onStatus = viewModel::updateStatus,
        onDescription = viewModel::updateDescription,
        onPosterUrl = viewModel::updatePosterUrl,
        onSubmit = viewModel::submit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarFormContent(
    carId: Int?,
    state: CarFormUiState,
    onBrand: (String) -> Unit,
    onModel: (String) -> Unit,
    onYear: (String) -> Unit,
    onPrice: (String) -> Unit,
    onStatus: (CarStatus) -> Unit,
    onDescription: (String) -> Unit,
    onPosterUrl: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (carId == null) "Добавить авто" else "Редактировать авто") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        AnimatedContentContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.brand,
                    onValueChange = onBrand,
                    label = { Text("Бренд") },
                    leadingIcon = { Icon(Icons.Default.DriveEta, contentDescription = null) },
                    shape = FieldShape,
                    colors = dealershipTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.model,
                    onValueChange = onModel,
                    label = { Text("Модель") },
                    leadingIcon = { Icon(Icons.Default.ModelTraining, contentDescription = null) },
                    shape = FieldShape,
                    colors = dealershipTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.year,
                    onValueChange = onYear,
                    label = { Text("Год") },
                    leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = FieldShape,
                    colors = dealershipTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.price,
                    onValueChange = onPrice,
                    label = { Text("Цена") },
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = FieldShape,
                    colors = dealershipTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (carId != null) {
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = state.status == CarStatus.AVAILABLE,
                            onClick = { onStatus(CarStatus.AVAILABLE) },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            icon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                            label = { Text("В наличии") }
                        )
                        SegmentedButton(
                            selected = state.status == CarStatus.SOLD,
                            onClick = { onStatus(CarStatus.SOLD) },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            icon = { Icon(Icons.Default.Save, contentDescription = null) },
                            label = { Text("Продано") }
                        )
                    }
                }
                OutlinedTextField(
                    value = state.description,
                    onValueChange = onDescription,
                    label = { Text("Описание") },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                    shape = FieldShape,
                    colors = dealershipTextFieldColors(),
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.posterUrl,
                    onValueChange = onPosterUrl,
                    label = { Text("URL изображения") },
                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null) },
                    shape = FieldShape,
                    colors = dealershipTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                LoadingButton(
                    text = "Сохранить",
                    isLoading = state.isLoading,
                    onClick = onSubmit,
                    icon = Icons.Default.Save,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarFormScreenPreview() {
    Car_dealership_androidTheme {
        CarFormContent(
            carId = 1,
            state = CarFormUiState(
                brand = "Toyota",
                model = "Camry",
                year = "2022",
                price = "2950000",
                description = "Бизнес-седан",
                status = CarStatus.AVAILABLE
            ),
            onBrand = {},
            onModel = {},
            onYear = {},
            onPrice = {},
            onStatus = {},
            onDescription = {},
            onPosterUrl = {},
            onSubmit = {}
        )
    }
}

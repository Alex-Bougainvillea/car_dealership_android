package com.example.car_dealership_android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.domain.model.Car
import com.example.car_dealership_android.domain.model.CarStatus
import com.example.car_dealership_android.presentation.components.AnimatedContentContainer
import com.example.car_dealership_android.presentation.components.CardShape
import com.example.car_dealership_android.presentation.components.CarImage
import com.example.car_dealership_android.presentation.components.ScreenTitle
import com.example.car_dealership_android.presentation.components.StatusChip
import com.example.car_dealership_android.presentation.components.dealershipTextFieldColors
import com.example.car_dealership_android.presentation.viewmodel.CarsUiState
import com.example.car_dealership_android.presentation.viewmodel.CarsViewModel
import com.example.car_dealership_android.ui.theme.Car_dealership_androidTheme
import com.example.car_dealership_android.ui.theme.DealershipTextStyles
import kotlinx.coroutines.launch

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

    CarsContent(
        state = state,
        cars = viewModel.filteredCars(),
        onBrandFilter = viewModel::updateBrandFilter,
        onMinPriceFilter = viewModel::updateMinPriceFilter,
        onMaxPriceFilter = viewModel::updateMaxPriceFilter,
        onStatusFilter = viewModel::updateStatusFilter,
        onAddCar = onAddCar,
        onOpenCar = onCarClick,
        onEditCar = onEditCar,
        onDeleteCar = viewModel::deleteCar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarsContent(
    state: CarsUiState,
    cars: List<Car>,
    onBrandFilter: (String) -> Unit,
    onMinPriceFilter: (String) -> Unit,
    onMaxPriceFilter: (String) -> Unit,
    onStatusFilter: (CarStatus?) -> Unit,
    onAddCar: () -> Unit,
    onOpenCar: (Int) -> Unit,
    onEditCar: (Int) -> Unit,
    onDeleteCar: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                FiltersDrawer(
                    state = state,
                    onBrandFilter = onBrandFilter,
                    onMinPriceFilter = onMinPriceFilter,
                    onMaxPriceFilter = onMaxPriceFilter,
                    onStatusFilter = onStatusFilter,
                    onDone = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                if (state.isAdmin) {
                    FloatingActionButton(onClick = onAddCar) {
                        Icon(Icons.Default.Add, contentDescription = "Добавить авто")
                    }
                }
            }
        ) { padding ->
            AnimatedContentContainer {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ScreenTitle(
                        title = "Автомобили",
                        subtitle = "Найдено: ${cars.size}",
                        action = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Фильтры")
                            }
                        }
                    )
                    if (state.isLoading && cars.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(cars, key = { it.id }) { car ->
                                CarItem(
                                    car = car,
                                    isAdmin = state.isAdmin,
                                    onOpen = { onOpenCar(car.id) },
                                    onEdit = { onEditCar(car.id) },
                                    onDelete = { onDeleteCar(car.id) }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(72.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FiltersDrawer(
    state: CarsUiState,
    onBrandFilter: (String) -> Unit,
    onMinPriceFilter: (String) -> Unit,
    onMaxPriceFilter: (String) -> Unit,
    onStatusFilter: (CarStatus?) -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(320.dp)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(text = "Фильтры", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = state.brandFilter,
            onValueChange = onBrandFilter,
            label = { Text("Бренд") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            colors = dealershipTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = state.minPriceFilter,
                onValueChange = onMinPriceFilter,
                label = { Text("От") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = dealershipTextFieldColors(),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = state.maxPriceFilter,
                onValueChange = onMaxPriceFilter,
                label = { Text("До") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = dealershipTextFieldColors(),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = state.statusFilter == null, onClick = { onStatusFilter(null) }, label = { Text("Все") })
            FilterChip(selected = state.statusFilter == CarStatus.AVAILABLE, onClick = { onStatusFilter(CarStatus.AVAILABLE) }, label = { Text("В наличии") })
            FilterChip(selected = state.statusFilter == CarStatus.SOLD, onClick = { onStatusFilter(CarStatus.SOLD) }, label = { Text("Продано") })
        }
        TextButton(onClick = onDone, modifier = Modifier.align(Alignment.End)) {
            Text("Готово")
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
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CarImage(
                posterUrl = car.posterUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "${car.brand} ${car.model}", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "${car.year} год",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "%,.0f ₽".format(car.price),
                        style = DealershipTextStyles.Price,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                StatusChip(status = car.status)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onOpen) { Icon(Icons.Default.Info, contentDescription = "Подробнее") }
                if (isAdmin) {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Редактировать") }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Удалить") }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarsScreenPreview() {
    Car_dealership_androidTheme {
        CarsContent(
            state = CarsUiState(isAdmin = true),
            cars = listOf(
                Car(1, "Toyota", "Camry", 2022, 2950000.0, CarStatus.AVAILABLE, null, null),
                Car(2, "BMW", "X5", 2021, 6200000.0, CarStatus.SOLD, null, null)
            ),
            onBrandFilter = {},
            onMinPriceFilter = {},
            onMaxPriceFilter = {},
            onStatusFilter = {},
            onAddCar = {},
            onOpenCar = {},
            onEditCar = {},
            onDeleteCar = {}
        )
    }
}

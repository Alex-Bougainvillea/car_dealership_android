package com.example.car_dealership_android.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.car_dealership_android.domain.model.User
import com.example.car_dealership_android.domain.model.UserRole
import com.example.car_dealership_android.presentation.components.AnimatedContentContainer
import com.example.car_dealership_android.presentation.components.CardShape
import com.example.car_dealership_android.presentation.components.ScreenTitle
import com.example.car_dealership_android.presentation.viewmodel.AuthUiState
import com.example.car_dealership_android.presentation.viewmodel.AuthViewModel
import com.example.car_dealership_android.ui.theme.Car_dealership_androidTheme
import com.example.car_dealership_android.ui.theme.SoldRed

@Composable
fun HomeScreen(
    onCars: () -> Unit,
    onClients: () -> Unit,
    onRequests: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    HomeContent(
        state = state,
        onCars = onCars,
        onClients = onClients,
        onRequests = onRequests,
        onLogout = {
            viewModel.logout()
            onLogout()
        }
    )
}

@Composable
private fun HomeContent(
    state: AuthUiState,
    onCars: () -> Unit,
    onClients: () -> Unit,
    onRequests: () -> Unit,
    onLogout: () -> Unit
) {
    val isAdmin = state.user?.role == UserRole.ADMIN
    AnimatedContentContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScreenTitle(
                title = "Главное меню",
                subtitle = state.user?.username?.let { "Пользователь: $it" } ?: "Автосалон"
            )
            MenuCard(
                icon = Icons.Default.DirectionsCar,
                title = "Автомобили",
                description = "Каталог, фильтры и карточки автомобилей",
                onClick = onCars
            )
            AnimatedVisibility(visible = isAdmin) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    MenuCard(
                        icon = Icons.Default.People,
                        title = "Клиенты",
                        description = "Контакты покупателей и новые клиенты",
                        onClick = onClients
                    )
                    MenuCard(
                        icon = Icons.AutoMirrored.Filled.Assignment,
                        title = "Заявки",
                        description = "Оформление покупки и смена статуса авто",
                        onClick = onRequests
                    )
                }
            }
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                shape = CardShape
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = SoldRed)
                Text(
                    text = "Выйти",
                    modifier = Modifier.padding(start = 8.dp),
                    color = SoldRed
                )
            }
        }
    }
}

@Composable
private fun MenuCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = CardShape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(34.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Car_dealership_androidTheme {
        HomeContent(
            state = AuthUiState(user = User(1, "admin", UserRole.ADMIN)),
            onCars = {},
            onClients = {},
            onRequests = {},
            onLogout = {}
        )
    }
}

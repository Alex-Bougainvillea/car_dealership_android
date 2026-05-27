package com.example.car_dealership_android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.car_dealership_android.presentation.screens.CarDetailsScreen
import com.example.car_dealership_android.presentation.screens.CarFormScreen
import com.example.car_dealership_android.presentation.screens.CarsScreen
import com.example.car_dealership_android.presentation.screens.ClientsScreen
import com.example.car_dealership_android.presentation.screens.HomeScreen
import com.example.car_dealership_android.presentation.screens.LoginScreen
import com.example.car_dealership_android.presentation.screens.RequestsScreen

@Composable
fun CarDealershipNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.Login) {
        composable(NavRoutes.Login) {
            LoginScreen(onSuccess = {
                navController.navigate(NavRoutes.Home) {
                    popUpTo(NavRoutes.Login) { inclusive = true }
                }
            })
        }
        composable(NavRoutes.Home) {
            HomeScreen(
                onCars = { navController.navigate(NavRoutes.Cars) },
                onClients = { navController.navigate(NavRoutes.Clients) },
                onRequests = { navController.navigate(NavRoutes.Requests) },
                onLogout = {
                    navController.navigate(NavRoutes.Login) {
                        popUpTo(NavRoutes.Home) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.Cars) {
            CarsScreen(
                onCarClick = { id -> navController.navigate("${NavRoutes.CarDetails}/$id") },
                onAddCar = { navController.navigate(NavRoutes.CarForm) },
                onEditCar = { id -> navController.navigate("${NavRoutes.CarForm}?carId=$id") }
            )
        }
        composable(
            route = "${NavRoutes.CarDetails}/{carId}",
            arguments = listOf(navArgument("carId") { type = NavType.IntType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            CarDetailsScreen(
                carId = carId,
                onEdit = { id -> navController.navigate("${NavRoutes.CarForm}?carId=$id") }
            )
        }
        composable(
            route = "${NavRoutes.CarForm}?carId={carId}",
            arguments = listOf(navArgument("carId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: -1
            CarFormScreen(
                carId = if (carId == -1) null else carId,
                onDone = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.Clients) { ClientsScreen() }
        composable(NavRoutes.Requests) { RequestsScreen() }
    }
}

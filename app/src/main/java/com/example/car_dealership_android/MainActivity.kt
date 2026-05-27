package com.example.car_dealership_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.car_dealership_android.presentation.navigation.CarDealershipNavHost
import com.example.car_dealership_android.ui.theme.Car_dealership_androidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Car_dealership_androidTheme {
                CarDealershipNavHost()
            }
        }
    }
}
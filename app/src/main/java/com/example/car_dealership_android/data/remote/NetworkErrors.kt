package com.example.car_dealership_android.data.remote

import java.io.IOException
import retrofit2.HttpException

fun Throwable.toReadableNetworkMessage(defaultMessage: String): String =
    when (this) {
        is HttpException -> when (code()) {
            400 -> "Проверьте введенные данные"
            401 -> "Неверный логин или пароль"
            403 -> "Недостаточно прав для действия"
            404 -> "Данные не найдены"
            else -> "Ошибка сервера: HTTP ${code()}"
        }
        is IOException -> "Нет подключения к серверу. Проверьте, что backend запущен на порту 8080"
        else -> message ?: defaultMessage
    }

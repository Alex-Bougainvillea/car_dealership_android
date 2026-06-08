package com.example.car_dealership_android.data.remote

import java.io.IOException
import retrofit2.HttpException

fun Throwable.toReadableNetworkMessage(defaultMessage: String): String =
    when (this) {
        is HttpException -> {
            val serverMessage = response()?.errorBody()?.string()?.extractMessage()
            serverMessage ?: when (code()) {
                400 -> "Проверьте введенные данные"
                401 -> "Неверный логин или пароль"
                403 -> "Недостаточно прав для действия"
                404 -> "Данные не найдены"
                else -> "Ошибка сервера: HTTP ${code()}"
            }
        }
        is IOException -> "Нет подключения к серверу. Проверьте, что backend запущен на порту 8080"
        else -> message ?: defaultMessage
    }

private fun String.extractMessage(): String? {
    val marker = "\"message\""
    val markerIndex = indexOf(marker)
    if (markerIndex == -1) return null
    val colonIndex = indexOf(':', startIndex = markerIndex + marker.length)
    if (colonIndex == -1) return null
    val firstQuote = indexOf('"', startIndex = colonIndex + 1)
    if (firstQuote == -1) return null
    val secondQuote = indexOf('"', startIndex = firstQuote + 1)
    if (secondQuote == -1) return null
    return substring(firstQuote + 1, secondQuote).takeIf { it.isNotBlank() }
}

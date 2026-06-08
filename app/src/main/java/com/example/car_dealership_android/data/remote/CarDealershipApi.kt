package com.example.car_dealership_android.data.remote

import com.example.car_dealership_android.data.remote.dto.AuthRequestDto
import com.example.car_dealership_android.data.remote.dto.AuthResponseDto
import com.example.car_dealership_android.data.remote.dto.CarCreateRequestDto
import com.example.car_dealership_android.data.remote.dto.CarDto
import com.example.car_dealership_android.data.remote.dto.CarUpdateRequestDto
import com.example.car_dealership_android.data.remote.dto.ClientCreateRequestDto
import com.example.car_dealership_android.data.remote.dto.ClientDto
import com.example.car_dealership_android.data.remote.dto.PurchaseRequestCreateRequestDto
import com.example.car_dealership_android.data.remote.dto.PurchaseRequestDto
import com.example.car_dealership_android.data.remote.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CarDealershipApi {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequestDto): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @GET("cars")
    suspend fun getCars(): List<CarDto>

    @GET("cars/{id}")
    suspend fun getCar(@Path("id") id: Int): CarDto

    @POST("cars")
    suspend fun createCar(@Body request: CarCreateRequestDto): CarDto

    @PUT("cars/{id}")
    suspend fun updateCar(@Path("id") id: Int, @Body request: CarUpdateRequestDto): CarDto

    @DELETE("cars/{id}")
    suspend fun deleteCar(@Path("id") id: Int): Response<Unit>

    @GET("clients")
    suspend fun getClients(): List<ClientDto>

    @POST("clients")
    suspend fun createClient(@Body request: ClientCreateRequestDto): ClientDto

    @GET("requests")
    suspend fun getRequests(): List<PurchaseRequestDto>

    @POST("requests")
    suspend fun createRequest(@Body request: PurchaseRequestCreateRequestDto): PurchaseRequestDto

    @DELETE("requests/{id}")
    suspend fun deleteRequest(@Path("id") id: Int): Response<Unit>
}

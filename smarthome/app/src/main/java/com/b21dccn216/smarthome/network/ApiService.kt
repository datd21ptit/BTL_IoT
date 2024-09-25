package com.b21dccn216.smarthome.network

import com.b21dccn216.smarthome.model.DashboarUiState
import com.b21dccn216.smarthome.network.dto.DashboardDTO
import com.b21dccn216.smarthome.network.dto.TableDTO
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val IpAddress = "192.168.1.6"
private const val BASE_URL =
    "http://${IpAddress}:3001"


val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder().build()) // Optional: Customize OkHttp
    .build()

interface ApiService {
    @GET("/dashboard")
    suspend fun getDashboardData(
        @Query("limit") limit: Int
    ) : DashboardDTO

    @POST("/dashboard")
    suspend fun sendAction(
        @Query("device") device: String,
        @Query("state") state : String
    ): Response<Void>


    @GET("table/sensor")
    suspend fun getSensorTable(
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("temp") temp: String,
        @Query("humid") humid: String,
        @Query("light") light: String,
        @Query("time") time: String,
        @Query("wind") wind: String,
        @Query("sort") sort: List<String>,
        @Query("timeSearch") timeSearch: String,
    ): TableDTO

    @GET("table/action")
    suspend fun getActionTable(
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("device") device:String,
        @Query("state") state: String,
        @Query("time") time: String,
        @Query("sort") sort: List<String>,
        @Query("timeSearch") timeSearch: String,
    ): TableDTO
}
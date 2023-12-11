package com.example.mars.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

private const val API_KEY = "KoQucUZwGZgbQNVHbFxZUY1yDsQFj6ZcK6v3rdQt"
class RetrofitAndApi @Inject constructor() {
    private val api by lazy {
        Retrofit
            .Builder()
            .baseUrl("https://api.nasa.gov")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                }).build()
            )
            .build()
            .create(MarsApi::class.java)
    }
    suspend fun getMarsApi(rover: String, date: String, page: Int): MarsDataDto {
        return api.marsApi(rover, date, API_KEY, page)
    }
}

interface MarsApi {
    @GET("/mars-photos/api/v1/rovers/{rover}/photos")
    suspend fun marsApi(
        @Path("rover") rover: String,
        @Query("earth_date") date: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MarsDataDto
}
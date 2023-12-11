package com.example.mars.data

import com.example.mars.entity.Photo
import javax.inject.Inject
import kotlin.collections.List

class MarsDataRepository @Inject constructor(
    private val rover: String,
    private val date: String
) {
    private val retrofitAndApi = RetrofitAndApi()

    suspend fun getMarsDataDto(page: Int): List<Photo> {
        return retrofitAndApi.getMarsApi(rover, date, page).photos
    }
}
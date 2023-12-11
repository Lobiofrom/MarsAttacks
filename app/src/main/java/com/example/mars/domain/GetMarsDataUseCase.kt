package com.example.mars.domain

import com.example.mars.data.MarsDataRepository
import com.example.mars.entity.Photo
import javax.inject.Inject

class GetMarsDataUseCase @Inject constructor(
    val rover: String,
    val date: String
) {
    private val marsDataRepository = MarsDataRepository(rover, date)

    suspend fun execute(page: Int): List<Photo> {
        val photos = marsDataRepository.getMarsDataDto(page)
        return photos.ifEmpty {
            throw NoSuchElementException("error from UseCase")
        }
    }
}
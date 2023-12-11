package com.example.mars.data

import com.example.mars.entity.MarsData
import com.example.mars.entity.Photo

class MarsDataDto(
    override val photos: List<Photo>
) : MarsData

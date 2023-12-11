package com.example.mars.pagedlist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mars.domain.GetMarsDataUseCase
import com.example.mars.entity.Photo

class MyPagingSource(
     val rover: String,
     val date: String
) : PagingSource<Int, Photo>() {
    private val getMarsDataUseCase = GetMarsDataUseCase(rover, date)
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 0
        return kotlin.runCatching {
            getMarsDataUseCase.execute(page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }
}

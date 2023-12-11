package com.example.mars.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mars.data.State
import com.example.mars.domain.GetMarsDataUseCase
import com.example.mars.entity.Photo
import com.example.mars.pagedlist.MyPagingSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
     val rover: String,
     val date: String
) : ViewModel() {

    private val getMarsDataUseCase = GetMarsDataUseCase(rover, date)
    private val _state = MutableStateFlow<State>(State.Success)
    val state = _state.asStateFlow()

    fun sendError() {
        viewModelScope.launch {
            try {
                getMarsDataUseCase.execute(0)
            } catch (e: Exception) {
                _state.value = State.Error
            }
        }
    }

    val pagedMarsData: Flow<PagingData<Photo>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { MyPagingSource(rover, date) }
    ).flow.cachedIn(viewModelScope)
}

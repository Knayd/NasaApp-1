package com.example.nasaapp.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.network.model.PhotoApiMapper
import com.example.domain.Photo
import com.example.usecases.getphotos.GetPhotosApiUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPhotosApi: GetPhotosApiUseCaseImpl,
    private val apiMapper: PhotoApiMapper
) : ViewModel() {

    private val _scrolled = MutableLiveData(false)
    val scrolled: LiveData<Boolean>
        get() = _scrolled

    fun updateScrolled() {
        _scrolled.value = true
    }

    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>>
        get() = _photos

    private val _responseState = MutableLiveData("initial")
    val responseState: LiveData<String>
        get() = _responseState

    private var currentFlow = viewModelScope.launch { }

    init {
        getPhotosFromFlow("Curiosity", "1000", "All")
    }

    fun getPhotosFromFlow(name: String, sol: String, selectedCamera: String) {
        currentFlow.cancel()
        currentFlow = viewModelScope.launch {
            getPhotosApi(name, sol, selectedCamera).collect {
                if (_photos.value == null || _photos.value?.size == 0 || _photos.value != apiMapper.fromEntityList(
                        it.photos
                    ) || it.status != "initial"
                ) {
                    _responseState.value = it.status
                    _photos.value = apiMapper.fromEntityList(it.photos)
                }
            }
        }
    }
}
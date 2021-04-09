package com.cookandroid.gachon_study_room.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import com.cookandroid.gachon_study_room.data.repository.MainRepository
import com.cookandroid.gachon_study_room.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _roomList = MutableLiveData<Resource<RoomsData>>()
    val roomList = _roomList

    fun callRooms(data : HashMap<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            _roomList.postValue(Resource.loading(null))
            try {
                _roomList.postValue(Resource.success(mainRepository.rooms(data).body()!!))
            }
            catch(e: Exception) {
                _roomList.postValue(Resource.error(null, e.message ?: "Error Occurred!"))
            }
        }
    }
}
package com.cookandroid.gachon_study_room.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cookandroid.gachon_study_room.data.model.MySeat
import com.cookandroid.gachon_study_room.data.model.Reserve
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import com.cookandroid.gachon_study_room.data.repository.MainRepository
import com.cookandroid.gachon_study_room.util.Resource
import java.lang.Exception

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
//    private val _roomList = MutableLiveData<Resource<RoomsData>>()
//    val roomList = _roomList

    // Rooms API 통신
    fun callRooms(data: HashMap<String, Any>) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.rooms(data).body()))
        }
        catch(e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occurred!"))
        }
    }

    // Reserve API 통신
    var reservation = MutableLiveData<Reserve>()
    fun callReserve(data: HashMap<String, Any>) = liveData {
        emit(Resource.loading(null))
        try{
            emit(Resource.success(mainRepository.reserve(data).body()!!))
        }
        catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occurred!"))
        }
    }

    // My Seat API 통신
    var mySeatData = MySeat()
    fun callMySeat(data: HashMap<String, Any>) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.mySeat(data).body()!!))
        }
        catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occurred!"))
        }
    }

    // Cancel API 통신
    fun callCancel(data: HashMap<String, Any>) = liveData {
     emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.cancel(data).body()!!))
        }
        catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occurred!"))
        }
    }

    // Confirm API 통신
    fun callConfirm(data: HashMap<String, Any>) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.confirm(data).body()))
        }
        catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occurred!"))
        }
    }

    // Extend API 통신
    fun callExtend(data: HashMap<String, Any>) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.extend(data).body()))
        }
        catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occurred!"))
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "onCleared")
    }
}
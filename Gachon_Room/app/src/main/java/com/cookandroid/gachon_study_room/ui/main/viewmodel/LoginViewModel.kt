package com.cookandroid.gachon_study_room.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.cookandroid.gachon_study_room.data.model.Account
import com.cookandroid.gachon_study_room.data.model.LoginRequest
import com.cookandroid.gachon_study_room.data.repository.LoginRepository
import com.cookandroid.gachon_study_room.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private var _loginData = MutableLiveData<Resource<Account>>()
    val loginData: LiveData<Resource<Account>>
        get() = _loginData

    val TAG = "LOGINVIEWMODEL"
    // login API 통신
//    fun loginApiCall(loginRequest: LoginRequest) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _loginData.postValue(Resource.loading(null))
//            try {
//                _loginData.postValue(Resource.success(loginRepository.login(loginRequest).body()) as Resource<Account>?)
//            } catch (e: Exception) {
//                _loginData.postValue(Resource.error(null, e.message ?: "Error Occurred!"))
//            }
//        }
//    }

    fun loginApiCall(loginRequest: LoginRequest) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(loginRepository.login(loginRequest)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occurred!"))
            Log.d(TAG, "loginApiCall: ${e.toString()}")
        }
    }
}
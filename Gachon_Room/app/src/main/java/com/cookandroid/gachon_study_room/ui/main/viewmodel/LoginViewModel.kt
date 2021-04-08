package com.cookandroid.gachon_study_room.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.data.repository.LoginRepository
import com.cookandroid.gachon_study_room.util.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private var _loginData = MutableLiveData<Resource<Information>>()
    val loginData = _loginData

    fun loginApiCall(data: HashMap<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginData.postValue(Resource.loading(null))
            try {
                _loginData.postValue(Resource.success(loginRepository.login(data).body()!!))
            }
            catch (e: Exception) {
                _loginData.postValue(Resource.error(null, e.message ?: "Error Occurred!"))
            }
        }

    }
}
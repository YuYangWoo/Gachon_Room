package com.cookandroid.gachon_study_room.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.data.repository.LoginRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val _loginData = MutableLiveData<Information>()
    val loginData = _loginData

    fun loginApiCall(data: HashMap<String, Any>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            loginRepository.login(data).let {
                _loginData.postValue(it.body())
            }
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, t ->
        t.printStackTrace()
        Log.d("TAG", t.toString())
    }
}
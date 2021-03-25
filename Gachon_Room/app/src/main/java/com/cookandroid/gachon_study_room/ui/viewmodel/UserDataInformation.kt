package com.cookandroid.gachon_study_room.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cookandroid.gachon_study_room.data.Information

class UserDataInformation : ViewModel() {

    var userInfo = MutableLiveData<Information>()

    @JvmName("setUserInfo1")
    fun setUserInfo(data: MutableLiveData<Information>) {
        userInfo = data
    }
    @JvmName("getUserInfo1")
    fun getUserInfo() : MutableLiveData<Information> {
        return userInfo
    }
}

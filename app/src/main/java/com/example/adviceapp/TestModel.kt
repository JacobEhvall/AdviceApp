package com.example.adviceapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/*
class TestModel : ViewModel() {

    val _userEmail = MutableLiveData<String>()

    val userEmail : LiveData<String>
    get() = _userEmail

    init {
        _userEmail.value = ""
    }

    fun getData(uid:String): String {
        var userEmail = FirebaseData().getUserEmail(uid)
        return userEmail.toString()
    }

}
 */
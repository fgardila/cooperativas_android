package com.code93.linkcoop.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    val response = MutableLiveData<String>()

    fun execute(data: String) {
        viewModelScope.launch {

        }
    }

}
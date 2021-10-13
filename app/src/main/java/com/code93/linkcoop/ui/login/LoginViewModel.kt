package com.code93.linkcoop.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code93.linkcoop.domain.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    val response = MutableLiveData<String>()
    val loginUseCase = LoginUseCase()

    fun execute(data: String) {
        viewModelScope.launch {
            val result = loginUseCase.invoke(data)
            response.postValue(result)
        }
    }

}
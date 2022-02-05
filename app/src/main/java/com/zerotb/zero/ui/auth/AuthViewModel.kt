package com.zerotb.zero.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.data.repository.AuthRepository
import com.zerotb.zero.data.responses.auth.ForgotResponse
import com.zerotb.zero.data.responses.auth.LoginResponse
import com.zerotb.zero.data.responses.auth.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    private val _forgotResponse: MutableLiveData<Resource<ForgotResponse>> = MutableLiveData()

    val forgotResponse: LiveData<Resource<ForgotResponse>>
        get() = _forgotResponse

    private val _registerResponse: MutableLiveData<Resource<RegisterResponse>> = MutableLiveData()

    val registerResponse: LiveData<Resource<RegisterResponse>>
        get() = _registerResponse

    fun login(email: String, password: String, fcm: String) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password, fcm)
        _loginResponse.value = Resource.Dismiss
    }

    fun forgot(email: String) = viewModelScope.launch {
        _forgotResponse.value = Resource.Loading
        _forgotResponse.value = repository.forgot(email)
        _forgotResponse.value = Resource.Dismiss
    }

    fun register(
        name: String,
        username: String,
        email: String,
        gender: String,
        hp: String,
        password: String,
        role: String,
        medical: String,
    ) = viewModelScope.launch {
        _registerResponse.value = Resource.Loading
        _registerResponse.value =
            repository.register(name, username, email, gender, hp, password, role, medical)
        _registerResponse.value = Resource.Dismiss
    }

    suspend fun saveToken(token: String) = repository.saveToken(token)

    suspend fun saveRole(role: String) = repository.saveRole(role)

    suspend fun saveName(name: String) = repository.saveName(name)

    suspend fun saveId(id: Int) = repository.saveId(id)

}
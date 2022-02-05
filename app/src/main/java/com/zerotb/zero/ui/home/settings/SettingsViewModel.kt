package com.zerotb.zero.ui.home.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.data.repository.AuthRepository
import com.zerotb.zero.data.responses.auth.LogoutResponse
import com.zerotb.zero.data.responses.auth.ProfileResponse
import com.zerotb.zero.data.responses.auth.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _logoutResponse: MutableLiveData<Resource<LogoutResponse>> = MutableLiveData()

    val logoutResponse: LiveData<Resource<LogoutResponse>>
        get() = _logoutResponse

    private val _userResponse: MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    val userResponse: LiveData<Resource<UserResponse>>
        get() = _userResponse

    private val _profileResponse: MutableLiveData<Resource<ProfileResponse>> = MutableLiveData()

    val profileResponse: LiveData<Resource<ProfileResponse>>
        get() = _profileResponse

    fun logout(token: String) = viewModelScope.launch {
        _logoutResponse.value = Resource.Loading
        _logoutResponse.value = repository.logout(token)
        _logoutResponse.value = Resource.Dismiss
    }

    fun user(token: String) = viewModelScope.launch {
        _userResponse.value = Resource.Loading
        _userResponse.value = repository.user(token)
        _userResponse.value = Resource.Dismiss
    }

    fun profile(
        token: String,
        profile: MultipartBody.Part
    ) = viewModelScope.launch {
        _profileResponse.value = Resource.Loading
        _profileResponse.value = repository.profile(token, profile)
        _profileResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}
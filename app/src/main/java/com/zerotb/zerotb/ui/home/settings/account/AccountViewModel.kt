package com.zerotb.zerotb.ui.home.settings.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerotb.zerotb.data.network.Resource
import com.zerotb.zerotb.data.repository.AuthRepository
import com.zerotb.zerotb.data.responses.auth.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _profileResponse: MutableLiveData<Resource<ProfileResponse>> = MutableLiveData()

    val profileResponse: LiveData<Resource<ProfileResponse>>
        get() = _profileResponse

    fun profile(
        token: String,
        id: Int,
        name: String,
        username: String,
        email: String,
        gender: String,
        hp: String,
    ) = viewModelScope.launch {
        _profileResponse.value = Resource.Loading
        _profileResponse.value =
            repository.update(token, id, name, username, email, gender, hp)
        _profileResponse.value = Resource.Dismiss
    }

    fun medical(
        token: String,
        id: Int,
        medical: String
    ) = viewModelScope.launch {
        _profileResponse.value = Resource.Loading
        _profileResponse.value =
            repository.medical(token, id, medical)
        _profileResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()
}
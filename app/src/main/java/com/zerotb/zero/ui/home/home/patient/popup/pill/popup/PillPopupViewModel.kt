package com.zerotb.zero.ui.home.home.patient.popup.pill.popup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.data.repository.DrugRepository
import com.zerotb.zero.data.responses.pill.DrugResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PillPopupViewModel @Inject constructor(
    private val repository: DrugRepository
) : ViewModel() {

    private val _drugResponse: MutableLiveData<Resource<DrugResponse>> = MutableLiveData()

    val drugResponse: LiveData<Resource<DrugResponse>>
        get() = _drugResponse

    fun drug(
        token: String,
        id: Int
    ) = viewModelScope.launch {
        _drugResponse.value = Resource.Loading
        _drugResponse.value = repository.drug(token, id)
        _drugResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()
}
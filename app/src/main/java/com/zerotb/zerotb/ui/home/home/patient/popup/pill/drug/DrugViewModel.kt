package com.zerotb.zerotb.ui.home.home.patient.popup.pill.drug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerotb.zerotb.data.network.Resource
import com.zerotb.zerotb.data.repository.DrugRepository
import com.zerotb.zerotb.data.responses.pill.DrugResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrugViewModel @Inject constructor(
    private val repository: DrugRepository
): ViewModel() {

    private val _drugResponse: MutableLiveData<Resource<DrugResponse>> = MutableLiveData()

    val drugResponse: LiveData<Resource<DrugResponse>>
        get() = _drugResponse

    fun drug(
        token: String,
        drug: String,
        hour: String,
        user: Int,
        medical: String,
        patient: String,
        doctor: String,
    ) = viewModelScope.launch {
        _drugResponse.value = Resource.Loading
        _drugResponse.value = repository.drug(token, drug, hour, user, medical, patient, doctor)
        _drugResponse.value = Resource.Dismiss
    }

    fun drug(
        token: String,
        id: Int,
        drug: String,
        hour: String,
        user: Int,
        medical: String,
        patient: String,
        doctor: String,
    ) = viewModelScope.launch {
        _drugResponse.value = Resource.Loading
        _drugResponse.value = repository.drug(token, id, drug, hour, user, medical, patient, doctor)
        _drugResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

    fun userName() = repository.userName()

}
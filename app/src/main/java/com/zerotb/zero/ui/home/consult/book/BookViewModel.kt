package com.zerotb.zero.ui.home.consult.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.data.repository.ConsultRepository
import com.zerotb.zero.data.responses.consult.BookResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: ConsultRepository
) : ViewModel() {

    private val _bookResponse: MutableLiveData<Resource<BookResponse>> = MutableLiveData()

    val bookResponse: LiveData<Resource<BookResponse>>
        get() = _bookResponse

    fun book(
        token: String,
        patient: String,
        consult: String,
        doctor: String,
    ) = viewModelScope.launch {
        _bookResponse.value = Resource.Loading
        _bookResponse.value = repository.book(token, patient, consult, doctor)
        _bookResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

    fun userName() = repository.userName()

}
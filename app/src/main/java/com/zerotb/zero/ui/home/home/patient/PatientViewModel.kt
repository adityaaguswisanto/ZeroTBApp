package com.zerotb.zero.ui.home.home.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zerotb.zero.data.network.MyApi
import com.zerotb.zero.data.responses.data.User
import com.zerotb.zero.data.source.PatientPaging
import com.zerotb.zero.data.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : ViewModel() {

    fun patient(token: String, q: String): Flow<PagingData<User>> {
        return Pager(PagingConfig(50)) {
            PatientPaging(api, token, q)
        }.flow.cachedIn(viewModelScope)
    }

    fun user() = userStore.authToken

}
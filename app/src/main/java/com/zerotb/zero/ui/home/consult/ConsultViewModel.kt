package com.zerotb.zero.ui.home.consult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zerotb.zero.data.network.MyApi
import com.zerotb.zero.data.responses.data.Consult
import com.zerotb.zero.data.source.ConsultPaging
import com.zerotb.zero.data.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ConsultViewModel @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : ViewModel() {

    fun consult(token: String): Flow<PagingData<Consult>> {
        return Pager(PagingConfig(50)) {
            ConsultPaging(api, token)
        }.flow.cachedIn(viewModelScope)
    }

    fun user() = userStore.authToken

}
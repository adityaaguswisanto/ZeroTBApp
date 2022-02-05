package com.zerotb.zerotb.ui.home.home.patient.popup.pill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zerotb.zerotb.data.network.MyApi
import com.zerotb.zerotb.data.responses.data.Pill
import com.zerotb.zerotb.data.source.PillPaging
import com.zerotb.zerotb.data.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PillViewModel @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
): ViewModel() {

    fun pill(token: String, id: Int): Flow<PagingData<Pill>> {
        return Pager(PagingConfig(50)) {
            PillPaging(api, token, id)
        }.flow.cachedIn(viewModelScope)
    }

    fun user() = userStore.authToken

}
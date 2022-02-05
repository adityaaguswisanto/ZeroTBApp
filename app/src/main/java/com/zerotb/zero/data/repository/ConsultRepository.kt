package com.zerotb.zero.data.repository

import com.zerotb.zero.data.network.BaseRepository
import com.zerotb.zero.data.network.MyApi
import com.zerotb.zero.data.store.UserStore
import javax.inject.Inject

class ConsultRepository @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : BaseRepository() {

    suspend fun book(
        token: String,
        patient: String,
        consult: String,
        doctor: String,
    ) = safeApiCall {
        api.book(token, patient, consult, doctor)
    }

    fun user() = userStore.authToken

    fun userName() = userStore.userName

}
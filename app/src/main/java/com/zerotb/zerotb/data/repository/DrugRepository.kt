package com.zerotb.zerotb.data.repository

import com.zerotb.zerotb.data.network.BaseRepository
import com.zerotb.zerotb.data.network.MyApi
import com.zerotb.zerotb.data.store.UserStore
import javax.inject.Inject

class DrugRepository @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : BaseRepository() {

    suspend fun drug(
        token: String,
        drug: String,
        hour: String,
        user: Int,
        medical: String,
        patient: String,
        doctor: String,
    ) = safeApiCall {
        api.drug(
            token, drug, hour, user, medical, patient, doctor
        )
    }

    suspend fun drug(
        token: String,
        id: Int
    ) = safeApiCall {
        api.drug(token, id)
    }

    suspend fun drug(
        token: String,
        id: Int,
        drug: String,
        hour: String,
        user: Int,
        medical: String,
        patient: String,
        doctor: String,
    ) = safeApiCall {
        api.drug(
            token, id, drug, hour, user, medical, patient, doctor
        )
    }

    fun user() = userStore.authToken

    fun userName() = userStore.userName

}
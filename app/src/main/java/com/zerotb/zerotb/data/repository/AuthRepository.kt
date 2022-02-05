package com.zerotb.zerotb.data.repository

import com.zerotb.zerotb.data.network.BaseRepository
import com.zerotb.zerotb.data.network.MyApi
import com.zerotb.zerotb.data.store.UserStore
import okhttp3.MultipartBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : BaseRepository() {

    suspend fun login(
        email: String,
        password: String,
        fcm: String,
    ) = safeApiCall {
        api.login(email, password, fcm)
    }

    suspend fun forgot(
        email: String
    ) = safeApiCall {
        api.forgot(email)
    }

    suspend fun register(
        name: String,
        username: String,
        email: String,
        gender: String,
        hp: String,
        password: String,
        role: String,
        medical: String,
    ) = safeApiCall {
        api.register(
            name, username, email, gender, hp, password, role, medical
        )
    }

    suspend fun logout(
        token: String
    ) = safeApiCall {
        api.logout(token)
    }

    suspend fun user(
        token: String
    ) = safeApiCall {
        api.user(token)
    }

    suspend fun profile(
        token: String,
        profile: MultipartBody.Part
    ) = safeApiCall {
        api.profile(token, profile)
    }

    suspend fun update(
        token: String,
        id: Int,
        name: String,
        username: String,
        email: String,
        gender: String,
        hp: String,
    ) = safeApiCall {
        api.update(
            token, id, name, username, email, gender, hp
        )
    }

    suspend fun medical(
        token: String,
        id: Int,
        medical: String
    ) = safeApiCall {
        api.medical(token, id, medical)
    }

    suspend fun saveToken(
        token: String
    ) = userStore.saveToken(
        token
    )

    suspend fun saveRole(
        role: String
    ) = userStore.saveRole(
        role
    )

    suspend fun saveName(
        name: String
    ) = userStore.saveName(
        name
    )

    suspend fun saveId(
        id: Int
    ) = userStore.saveId(
        id
    )

    fun user() = userStore.authToken

}
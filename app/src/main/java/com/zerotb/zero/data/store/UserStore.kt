package com.zerotb.zero.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserStore(
    context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_store")

    private val mDataStore: DataStore<Preferences> = context.dataStore

    val authToken: Flow<String?> = mDataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[KEY_AUTH]
        }

    val userRole: Flow<String?> = mDataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[KEY_ROLE]
        }

    val userName: Flow<String?> = mDataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[KEY_NAME]
        }

    val userId: Flow<Int?> = mDataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[KEY_ID]
        }

    suspend fun saveToken(token: String) =
        mDataStore.edit {
            it[KEY_AUTH] = token
        }

    suspend fun saveRole(role: String) =
        mDataStore.edit {
            it[KEY_ROLE] = role
        }

    suspend fun saveId(id: Int) =
        mDataStore.edit {
            it[KEY_ID] = id
        }

    suspend fun saveName(name: String) =
        mDataStore.edit {
            it[KEY_NAME] = name
        }

    suspend fun clear() = mDataStore.edit {
        it.clear()
    }

    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
        private val KEY_ROLE = stringPreferencesKey("key_role")
        private val KEY_NAME = stringPreferencesKey("key_name")
        private val KEY_ID = intPreferencesKey("key_id")
    }

}
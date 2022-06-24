package com.example.currentlocation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.currentlocation.LoginDataStore.Companion.PREF_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.datastore: DataStore<Preferences> by preferencesDataStore(PREF_NAME)
class LoginDataStore(private val context: Context) {

    companion object {
        const val PREF_NAME = "main_pref"
        const val PASSWORD = "password"
        const val USER_NAME = "user_name"
        const val IS_LOGGED_IN = "is_logged_in"
        const val ID = "id"

    }



    suspend fun saveLogin(value: Boolean) {

        val dataStoreKey = booleanPreferencesKey(IS_LOGGED_IN)
        context.datastore.edit {
            it[dataStoreKey] = value
        }
    }

    fun isLoggedIn(): Flow<Boolean> = context.datastore.data.map {
        val dataStoreKey = booleanPreferencesKey(IS_LOGGED_IN)
        val isLoggedIn = it[dataStoreKey] ?: false
        isLoggedIn
    }

    suspend fun savePassword(value: String) {
        val dataStoreKey = stringPreferencesKey(PASSWORD)
        context.datastore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getPassword(): Flow<String> = context.datastore.data.map {
        val dataStoreKey = stringPreferencesKey(PASSWORD)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveUserName(value: String) {
        val dataStoreKey = stringPreferencesKey(USER_NAME)
        context.datastore.edit {
            it[dataStoreKey] = value
        }
    }


    fun getUserName(): Flow<String> = context.datastore.data.map {
        val dataStoreKey = stringPreferencesKey(USER_NAME)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }
  suspend fun saveId(value: String) {
        val dataStoreKey = stringPreferencesKey(ID)
        context.datastore.edit {
            it[dataStoreKey] = value
        }
    }


    fun getId(): Flow<String> = context.datastore.data.map {
        val dataStoreKey = stringPreferencesKey(ID)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun clearValues() {
        context.datastore.edit {
            it.clear()
        }
    }

}
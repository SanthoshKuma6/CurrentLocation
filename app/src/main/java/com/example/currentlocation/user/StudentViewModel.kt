package com.example.currentlocation.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class StudentViewModel(private val repository: UserRepository) : ViewModel() {

    fun verifyUserLogin( username: String,password:String) : Flow<UserTable?>? {

      val  readAllData = repository.verifyUserLogin(username,password)
        return readAllData
    }



    fun findByEmail(email:String): Flow<UserTable?>? {
        val readAllData= repository.findByEmail(email)
        return readAllData
    }

    fun register(user: UserTable) = viewModelScope.launch {
        repository.register(user)
    }



    fun insertLocation(locationTable: LocationTable) = viewModelScope.launch {
        repository.insertLocation(locationTable)
    }


    fun getUserLocation(id:String) : Flow<List<LocationTable>> {

        val  readAllData = repository.getUserLocation(id)
        return readAllData
    }
}
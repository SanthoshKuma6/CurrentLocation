package com.example.currentlocation.user

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDataBase: UserDataBase) {

    suspend fun register(user: UserTable)= userDataBase.studentDao().register(user)

    suspend fun insertLocation(locationTable: LocationTable)= userDataBase.studentDao().insertLocation(locationTable)


    fun verifyUserLogin(email: String, password: String): Flow<UserTable?>? {
        return userDataBase.studentDao().verifyUserLogin(email, password)
    }



    fun findByEmail(email:String): Flow<UserTable?>? {
        val readAllData= userDataBase.studentDao().findByEmail(email)
        return readAllData
    }


    fun getUserLocation(id:String,): Flow<List<LocationTable>> {
        return userDataBase.studentDao().getUserLocation(id)
    }

}
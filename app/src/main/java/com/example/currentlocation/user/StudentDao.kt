package com.example.currentlocation.user

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


@Dao
interface StudentDao {


    @Query("SELECT * FROM user_table where email LIKE  :email")
    fun findByEmail(email: String?):Flow<UserTable?>?

    @Insert
    suspend fun register(user: UserTable)

    @Insert
    suspend fun insertLocation(location: LocationTable)

    @Query("select * from location_table where userId like :id ")
    fun getUserLocation(id:String):Flow<List<LocationTable>>

    @Query("SELECT * FROM user_table where email LIKE  :email AND password LIKE :password")
    fun verifyUserLogin(email: String?, password: String?): Flow<UserTable?>?


}
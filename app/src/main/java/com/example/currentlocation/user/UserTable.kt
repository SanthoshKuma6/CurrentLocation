package com.example.currentlocation.user

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class UserTable (
    val email:String,
    val password:String,
    @PrimaryKey(autoGenerate = true)
    val id :Int=0
)
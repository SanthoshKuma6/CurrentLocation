package com.example.currentlocation.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationTable (
    val lat:String,
    val lan:String,
    val userId:String,
    @PrimaryKey(autoGenerate = true)
    val id :Int=0

        )
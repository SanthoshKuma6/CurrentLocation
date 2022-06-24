package com.example.currentlocation.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [UserTable::class,LocationTable::class], version = 1, exportSchema = false)
abstract class UserDataBase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    companion object {

        @Volatile
        private var INSTANCE: UserDataBase? = null

        fun getDatabase(context: Context): UserDataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, UserDataBase::class.java, "student_database").build()
                INSTANCE = instance
                instance
            }
        }

    }


}
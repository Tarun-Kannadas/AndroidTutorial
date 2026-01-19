package com.example.loginform.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.loginform.data.User
import com.example.loginform.dao.UserDao

@Database(entities = [User::class], version = 1)
abstract class LoginDatabase : RoomDatabase()
{
    abstract fun UserDao(): UserDao

    companion object
    {
        @Volatile
        private var INSTANCE: LoginDatabase ?= null

        fun getDatabase(context: Context): LoginDatabase
        {
            return INSTANCE ?: synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoginDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
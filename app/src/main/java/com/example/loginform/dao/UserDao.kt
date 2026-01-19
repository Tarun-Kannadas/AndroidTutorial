package com.example.loginform.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.loginform.data.User

@Dao
interface UserDao{

    @Insert
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun loginUser(username: String, password: String): User?
}
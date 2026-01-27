package com.example.loginform.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.loginform.data.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun loginUser(username: String, password: String): User?

    @Query("SELECT * FROM users")
    suspend fun listUsers(): List<User>

    @Delete
    suspend fun deleteUser(user: User)
}
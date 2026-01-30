package com.example.loginform.repository

import com.example.loginform.dao.UserDao
import com.example.loginform.data.User

class UserRepository(private val userDao: UserDao) {

    // Logic for registering a new user
    suspend fun registerUser(user: User) {
        userDao.registerUser(user)
    }

    // Authenticate user for login
    suspend fun authUser(username: String, pass: String): User? {
        return userDao.loginUser(username, pass)
    }

    // Fetch all users for the recycler view
    suspend fun getAllUsers(): List<User> {
        return userDao.listUsers()
    }

    suspend fun deleteUser(user: User)
    {
        userDao.deleteUser(user)
    }

    suspend fun updateUser(user: User)
    {
        userDao.updateUser(user)
    }
}
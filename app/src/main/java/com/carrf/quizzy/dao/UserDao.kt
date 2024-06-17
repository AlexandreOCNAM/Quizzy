package com.carrf.quizzy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.carrf.quizzy.data.entities.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): User?

    @Query("SELECT * FROM User LIMIT 1")
    fun getCurrentUser(): User?

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUserById(userId: Int): User?
}

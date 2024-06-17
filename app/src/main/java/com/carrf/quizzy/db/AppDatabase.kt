package com.carrf.quizzy.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.carrf.quizzy.dao.QuizDao
import com.carrf.quizzy.dao.UserDao
import com.carrf.quizzy.data.entities.Question
import com.carrf.quizzy.data.entities.Quiz
import com.carrf.quizzy.data.entities.User

@Database(entities = [User::class, Quiz::class, Question::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun quizDao(): QuizDao
}

package com.carrf.quizzy.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    @PrimaryKey val questionId: Int,
    val quizId: Int,
    val text: String,
    val option1: String,
    val option2: String,
    val correctAnswer: String
)

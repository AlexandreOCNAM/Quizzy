package com.carrf.quizzy.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quiz(
    @PrimaryKey val quizId: Int,
    val title: String
)

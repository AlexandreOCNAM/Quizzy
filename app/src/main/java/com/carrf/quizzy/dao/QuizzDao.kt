package com.carrf.quizzy.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.carrf.quizzy.data.entities.Question
import com.carrf.quizzy.data.entities.Quiz

@Dao
interface QuizDao {
    @Insert
    fun insert(quiz: Quiz): Long

    @Insert
    fun insertAll(vararg questions: Question)

    @Query("SELECT * FROM Quiz")
    fun getAllQuizzes(): List<Quiz>

    @Query("SELECT * FROM Question WHERE quizId = :quizId")
    fun getQuestionsForQuiz(quizId: Int): List<Question>
}

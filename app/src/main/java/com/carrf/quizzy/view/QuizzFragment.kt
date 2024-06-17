package com.carrf.quizzy.view

import com.carrf.quizzy.R
import com.carrf.quizzy.data.entities.Question
import com.carrf.quizzy.db.AppDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room

class QuizFragment : Fragment() {

    private lateinit var db: AppDatabase
    private var questionIndex = 0
    private lateinit var questions: List<Question>
    private var currentQuizId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quizz, container, false)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "quiz-db"
        ).allowMainThreadQueries().build()

        currentQuizId = arguments?.getInt("QUIZ_ID", -1) ?: -1
        if (currentQuizId != -1) {
            questions = db.quizDao().getQuestionsForQuiz(currentQuizId)
            if (questions.isNotEmpty()) {
                displayQuestion(view)
            }
        }

        val nextButton: Button = view.findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            val selectedOption: RadioButton? = view.findViewById(view.findViewById<RadioGroup>(R.id.options_group).checkedRadioButtonId)
            if (selectedOption != null) {
                val correctAnswer = questions[questionIndex].correctAnswer
                if (selectedOption.text == correctAnswer) {
                    Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Incorrect!", Toast.LENGTH_SHORT).show()
                }

                questionIndex++
                if (questionIndex < questions.size) {
                    displayQuestion(view)
                } else {
                    Toast.makeText(context, "Quiz Completed!", Toast.LENGTH_SHORT).show()
                    showAllQuizzes()
                }
            } else {
                Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun displayQuestion(view: View) {
        val questionText: TextView = view.findViewById(R.id.question_text)
        val option1: RadioButton = view.findViewById(R.id.option1)
        val option2: RadioButton = view.findViewById(R.id.option2)

        val question = questions[questionIndex]
        questionText.text = question.text
        option1.text = question.option1
        option2.text = question.option2
    }

    private fun showAllQuizzes() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .addToBackStack(null)
            .commit()
    }
}

package com.carrf.quizzy.view

import android.content.Context
import com.carrf.quizzy.db.AppDatabase
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.carrf.quizzy.R
import com.carrf.quizzy.data.entities.Question
import com.carrf.quizzy.data.entities.Quiz
import com.carrf.quizzy.data.entities.User

class LoginFragment : Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "quiz-db"
        ).allowMainThreadQueries().build()

        // Ajouter quelques utilisateurs de test
        if (db.userDao().getUserByUsernameAndPassword("test", "test") == null) {
            db.userDao().insert(User(username = "test", password = "test"))
        }

        // Ajouter quelques quiz de test sur la Formule 1
        if (db.quizDao().getAllQuizzes().isEmpty()) {
            val f1Quiz1 = Quiz(quizId = 0, title = "Formula 1 Basics")
            val f1Quiz1Id = db.quizDao().insert(f1Quiz1).toInt()

            db.quizDao().insertAll(
    Question(questionId = 0, quizId = f1Quiz1Id, text = "Who won the F1 World Championship in 2021?", option1 = "Lewis Hamilton", option2 = "Max Verstappen", correctAnswer = "Max Verstappen"),
    Question(questionId = 1, quizId = f1Quiz1Id, text = "What is the highest class of single-seater auto racing sanctioned by the FIA?", option1 = "Formula 1", option2 = "Formula 2", correctAnswer = "Formula 1"),
    Question(questionId = 2, quizId = f1Quiz1Id, text = "How many teams compete in the 2023 Formula 1 season?", option1 = "10", option2 = "12", correctAnswer = "10"),
    Question(questionId = 3, quizId = f1Quiz1Id, text = "Which country hosts the Monaco Grand Prix?", option1 = "France", option2 = "Monaco", correctAnswer = "Monaco")
)

val f1Quiz2 = Quiz(quizId = 1, title = "F1 Circuits and Rules")
val f1Quiz2Id = db.quizDao().insert(f1Quiz2).toInt()

db.quizDao().insertAll(
    Question(questionId = 4, quizId = f1Quiz2Id, text = "Which circuit hosts the British Grand Prix?", option1 = "Silverstone", option2 = "Brands Hatch", correctAnswer = "Silverstone"),
    Question(questionId = 5, quizId = f1Quiz2Id, text = "What color flag indicates the end of a session?", option1 = "Yellow", option2 = "Checkered", correctAnswer = "Checkered"),
    Question(questionId = 6, quizId = f1Quiz2Id, text = "How many laps are there in the Monaco Grand Prix?", option1 = "78", option2 = "60", correctAnswer = "78"),
    Question(questionId = 7, quizId = f1Quiz2Id, text = "Which tire color indicates the softest compound?", option1 = "Red", option2 = "White", correctAnswer = "Red")
)
        }


        val usernameEditText: EditText = view.findViewById(R.id.username)
        val passwordEditText: EditText = view.findViewById(R.id.password)
        val loginButton: Button = view.findViewById(R.id.login_button)
        val quizzesList: ListView = view.findViewById(R.id.quizzes_list)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val user = db.userDao().getUserByUsernameAndPassword(username, password)
            if (user != null) {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                quizzesList.visibility = View.VISIBLE
                loadQuizzes(quizzesList)
            } else {
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun loadQuizzes(listView: ListView) {
        val quizzes = db.quizDao().getAllQuizzes()
        val quizTitles = quizzes.map { it.title }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, quizTitles)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedQuiz = quizzes[position]
            val fragment = QuizFragment().apply {
                arguments = Bundle().apply {
                    putInt("QUIZ_ID", selectedQuiz.quizId)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
